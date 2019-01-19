package com.github.jusm.util;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

	public static final String FILEEXT = "jpg,jpeg,gif,png";// 上传图片格式
	public static final int MAXSIZE = 10;// 上传图片大小

	/**
	 * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
	 *
	 * @param imageFile
	 * @return
	 */
	public static boolean isImage(MultipartFile imageFile) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageFile.getInputStream());
			if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			img = null;
		}
	}

	/**
	 * 大小校验
	 * 
	 * @param file
	 * @param imageSize
	 * @return
	 */
	public static boolean checkImageSize(MultipartFile file, int imageSize) {
		boolean result = false;
		if ((file.getSize() / 1024 / 1024) <= imageSize) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断后缀名
	 * 
	 * @param file
	 * @return
	 */
	public static boolean checkImageSuffix(MultipartFile file) {
		String hj = file.getOriginalFilename();
		hj = hj.substring(hj.lastIndexOf("."));
		if (FILEEXT.indexOf(hj.substring(1).toLowerCase()) == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检验图片
	 * 
	 * @param file
	 * @return
	 */
	public static boolean valid(MultipartFile file) {
		// 检查是否为图片，和图片大小 < 10M
		return isImage(file) && checkImageSize(file, 10) && checkImageSuffix(file);

	}

	public static boolean validImgList(List<MultipartFile> files) {
		for (MultipartFile file : files) {
			if (!isImage(file) || !checkImageSize(file, MAXSIZE) || !checkImageSuffix(file)) {
				return false;
			}
		}
		return true;
	}
}
