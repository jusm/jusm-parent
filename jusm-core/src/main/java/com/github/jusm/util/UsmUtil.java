package com.github.jusm.util;

public final class UsmUtil {
	
	

	/**
	 * 是不是Windows操作系统
	 */
	public static boolean isWinOS() {
		return getOSName().toLowerCase().startsWith("win");
	}

	/**
	 * 获取操作系统名称
	 * 
	 * @return
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}
}
