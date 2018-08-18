package com.github.jusm.redis;

/**
 * Redis所有Keys
 */
public final class RedisKeys {

	public static final String NEED_TOKEN_APICODE = "USM:config:NEED_TOKEN_APICODE";

	public static final String NEED_APP_TOKEN_APICODE = "USM:config:NEED_APP_TOKEN_APICODE";

	public static String getSysConfigKey(String key) {
		return "USM:MODULE:SYS:" + key;
	}

	/**
	 * @param username
	 *            用户名
	 * @return 存放用户因退出失效的token
	 */
	public static String getExpiredTokenKey(String username) {
		return USM_MODULE_SYS_EXPIRED_TOKEN + username;
	}

	public static final String USM_MODULE_SYS_EXPIRED_TOKEN = "USM:MODULE:SYS:EXPIRED_TOKEN:";

	public static final String USM_MODULE_SYS_TOKEN = "USM:MODULE:SYS:TOKEN:";

	public static final String USM_MODULE_PARCEL_SMSCODE = "USM:MODULE:PARCEL:SMSCODE:";

	public static final String USM_MODULE_PARCEL_WECHAT_ACCESS_TOKEN = "USM:MODULE:PARCEL:WECHAT:ACCESSTOKEN";

	public static final String USM_MODULE_PARCEL_SMSCODE_COUNT = "USM:MODULE:ADS:SMSCODE:COUNT:";

}
