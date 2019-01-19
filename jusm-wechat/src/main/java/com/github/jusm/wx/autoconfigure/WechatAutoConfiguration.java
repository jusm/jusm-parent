package com.github.jusm.wx.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.github.jusm.wx.api.WechatHttpApi;
import com.github.jusm.wx.controller.WechatController;
import com.github.jusm.wx.service.UserInfoService;

/**
 * 由于路径所以本工程jusm-wechat 除 repository想放入springbean容器内必须在此类配置bean
 * 而不能再配 @Controller @RestController @Service @Component等注解
 */
@EnableConfigurationProperties(WechatProperties.class)
public class WechatAutoConfiguration {

	@Autowired
	private WechatProperties wechatProperties;

	@Bean
	public WechatHttpApi wechatHttpApi() {
		String appid = wechatProperties.getAppid();
		String appsecret = wechatProperties.getAppsecret();
		return new WechatHttpApi(appid, appsecret);
	}

	@Bean
	public WechatController authController() {
		return new WechatController();
	}

	@Bean
	public UserInfoService userInfoService() {
		return new UserInfoService();
	}

}
