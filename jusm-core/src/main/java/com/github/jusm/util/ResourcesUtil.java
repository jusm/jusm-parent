package com.github.jusm.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.github.jusm.exception.UsmException;


public final class ResourcesUtil {
	
	private ResourcesUtil() {}

	private static Logger logger = LoggerFactory.getLogger(ResourcesUtil.class);

	private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	public static Resource getResource(String path) {
		return resolver.getResource(path);
	}


	public static String getFileContent(String path) {
		try {
			// 获取所有匹配的文件
			Resource resource = resolver.getResource(path);
			// 获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
			InputStream stream = resource.getInputStream();
			return inputStream2String(stream);
		} catch (IOException e) {
			if (logger.isWarnEnabled()) {
				logger.warn("读取文件流失败，写入本地库失败！ " + e);
			}
		}
		throw new UsmException("读取资源文件失败");
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

}
