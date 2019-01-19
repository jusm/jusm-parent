package com.github.jusm.service;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.github.jusm.entity.Resource;

/**
 * 资源访问与存储服务
 */
public interface ResourceService {
	
	boolean storage(Resource resource);

	String  generateURI(long id);
	
	String  generateURI(String name);
	
	public String generateURI(long id,Date expiration );
	
	public String generateURI(long id,long expiration);

	boolean verifySigned(String token);
	
	String parseURI(String token);

	String upload(MultipartFile multipartFile, String uploadPath);
	
	String storage(String base64Image, String uploadPath);
}
