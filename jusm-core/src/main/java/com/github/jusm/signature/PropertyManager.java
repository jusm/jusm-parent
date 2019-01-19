package com.github.jusm.signature;
/**
 * @author halley.w
 * @version 1.0
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;


public class PropertyManager {
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	private Properties property = new Properties();

	public static PropertyManager load(String[] configFileNames, Class loaderClass){
		return new PropertyManager(configFileNames, loaderClass);
	}

	/**
	 * constructor of PropertyManager which is used to create configure file.
	 */
	private PropertyManager(String[] configFileNames, Class loaderClass) {
		for (int i = 0; i < configFileNames.length; i++) {
			loadFile(configFileNames[i],loaderClass);
		}

	}

	/**
	 * Read configure file form specific file.
	 * 
	 * @param p_fileName
	 */
	private void loadFile(String p_fileName, Class loaderClass) {
		InputStream fin = null;
		try {
			fin = loaderClass.getResourceAsStream(p_fileName);
			property.load(fin);
		} catch (IOException ex) {
			logger.error("读文件失败", ex);
		} finally {
			if(fin != null){
				try {
					fin.close();
				} catch (IOException e) {
					logger.error("读文件失败", e);
				}
			}
		}
	}

	/**
	 * GET configure value by configure name
	 * 
	 * @param p_itemName
	 * @return configure name item's value.
	 */
	public String getString(String p_itemName) {
		return property.getProperty(p_itemName);
	}
	
	public String getString(String p_itemName,String... args) {
		String value = getString(p_itemName);
		if(value!=null && args != null){
			for(int i=0;i<args.length;i++){
				value = value.replace("{"+i+"}", args[i]==null?"":args[i]);
			}
		}
		return value;
	}
	/**
	 * 
	 * @param p_itemName
	 * @return
	 */
	public int getInt(String p_itemName){
		String value = getString(p_itemName);
		int intValue = 0;
		try{
			intValue = new Integer(value).intValue();
		}catch(NumberFormatException e){
			logger.error("", e);
		}
		return intValue;
	}
	
	public Properties getProperties(){
		return property;
	}
}
