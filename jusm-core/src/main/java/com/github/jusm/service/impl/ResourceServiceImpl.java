package com.github.jusm.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.context.UsmContext;
import com.github.jusm.entity.Resource;
import com.github.jusm.exception.UploadFileException;
import com.github.jusm.exception.UsmException;
import com.github.jusm.repository.ResourceRepository;
import com.github.jusm.service.ResourceService;
import com.github.jusm.util.Conts;
import com.github.jusm.util.UsmUtil;

@Service
public class ResourceServiceImpl implements ResourceService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private UsmProperties usmProperties;

	@Override
	public boolean verifySigned(String token) {
		if (StringUtils.isNotBlank(token)) {
			Resource findOne = resourceRepository.findByToken(token);
			return (findOne != null && findOne.getExpiration() > System.currentTimeMillis() && findOne.isEnabled());
		}
		return false;
	}

	@Override
	public String generateURI(String name) {
		try {
			String token = RandomStringUtils.randomAlphabetic(32);
			Resource findOne = resourceRepository.findByFileName(name);
			String localPath = usmProperties.getOutsideProtectedRes();
			if (UsmUtil.isWinOS()) {
				localPath = Conts.DEFAULT_DIST + localPath;
			}
			File srcFile = new File(localPath + findOne.getSrcFolder());
			if (srcFile.exists() && srcFile.isDirectory()) {
				File destFile = new File(localPath + token);
				FileUtils.copyDirectory(srcFile, destFile);
				findOne.setToken(token);
				findOne.setExpiration(Long.MAX_VALUE);
				findOne.setEnabled(true);
				Resource save = resourceRepository.save(findOne);
				return Conts.DEFAULT_ACCESS_LOCAL + save.getToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "#403";
		}
		return "#404";
	}

	/**
	 * 这里绝不能放在数据库里面操作 多用户并发操作会出问题 可以放入redis user Session-id 做数据隔离
	 * 
	 * @see com.github.jusm.service.ResourceService#generateURI(long)
	 */
	@Override
	public String generateURI(long id) {
		try {
			String token = RandomStringUtils.randomAlphabetic(32);
			Resource findOne = resourceRepository.findOne(id);
			String localPath = UsmContext.getLocalPath();
			String tempPath = UsmContext.getTempPath();
			File srcFile = new File(localPath + findOne.getSrcFolder());
			if (srcFile.exists() && srcFile.isDirectory()) {
				File destFile = new File(tempPath + token);
				FileUtils.copyDirectory(srcFile, destFile);
				findOne.setToken(token);
				findOne.setExpiration(10000L);
				findOne.setEnabled(true);
				Resource save = resourceRepository.save(findOne);
				return Conts.DEFAULT_ACCESS_LOCAL + save.getToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "#403";
		}
		return "#404";
	}

	@Override
	public String generateURI(long id, Date expiration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateURI(long id, long expiration) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		System.out.println(FileUtils.getUserDirectory().getAbsolutePath());
		System.out.println(FileUtils.getUserDirectory().getPath());
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(32);
		System.out.println(randomAlphanumeric);
	}

	@Override
	public String upload(MultipartFile multipartFile, String uploadPath) {
		try {
			String originalFilename = multipartFile.getOriginalFilename();
			String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(32);
			FileUtils.forceMkdir(new File(uploadPath + randomAlphanumeric));
			File dest = new File(uploadPath + randomAlphanumeric + "/" + originalFilename);
			multipartFile.transferTo(dest);
			return dest.getAbsolutePath();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new UsmException("上传失败", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UsmException("上传失败", e);
		}
	}

	@Override
	public boolean storage(Resource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String parseURI(String token) {
		if (StringUtils.isNotBlank(token)) {
			Resource res = resourceRepository.findByToken(token);
			if (res != null && res.isEnabled()) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						String tempPath = usmProperties.getTemp();
						if (UsmUtil.isWinOS()) {
							tempPath = Conts.DEFAULT_DIST + tempPath;
						}
						File destFile = new File(tempPath + token);
						logger.debug(destFile.getPath() + res.getFileName());
						if (destFile.isDirectory()) {
							try {
								FileUtils.deleteDirectory(destFile);
								res.setEnabled(false);
								resourceRepository.save(res);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}, res.getExpiration());
				return "forward:" + Conts.DEFAULT_OUT_RES_FILE_PATH + res.getToken() + "/" + res.getFileName();
			}
		}
		return "redirect:/404";
	}

	@Override
	public String storage(String base64Image, String uploadPath) {
		
		
		return null;
	}

}
