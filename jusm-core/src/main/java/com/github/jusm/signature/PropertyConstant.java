package com.github.jusm.signature;

/**
 * 配置文件常量类
 * @author lyj 2015年12月9日
 *
 */
public class PropertyConstant {

	public static final String[] configFileNames = { "/com/kye/openapi/META-INF/config.properties" };
	public static final PropertyManager propertyManager = PropertyManager.load(
			configFileNames, PropertyConstant.class);
 
}
