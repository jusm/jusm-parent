package com.github.jusm.security.service;

/**
 * 手机短信验证码服务
 */
public interface MobileCodeService {

	default String send(String mobile) {
		return "000000";
	}

	default boolean verify(String mobile, String code) {
		return code != null && code.trim().length() == 6;
	}

}
