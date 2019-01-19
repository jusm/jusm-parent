package com.github.jusm.wx.login.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.jusm.context.CurrentUser;
import com.github.jusm.entity.User;
import com.github.jusm.model.R;
import com.github.jusm.service.AuthorizeService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.wx.api.WechatHttpApi;
import com.github.jusm.wx.api.response.Jscode2sessionResponse;
import com.github.jusm.wx.entity.UserInfo;
import com.github.jusm.wx.handler.UserInitializationHandler;
import com.github.jusm.wx.model.EncryptedUserInfo;
import com.github.jusm.wx.model.JsCode;
import com.github.jusm.wx.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Wechat" }, protocols = "https", description = "微信服务")
@RequestMapping(value = Conts.DEFAULT_REST_API_PATH, produces = "application/json;charset=UTF-8")
public class WechatController {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WechatHttpApi wechatHttpApi;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired(required = false)
	private UserInitializationHandler userInitializationHandler;

	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private UserService userService;

	/**
	 * 雇员使用的小程序
	 */
	private static final String appid = "wx6d2456dd6523f8e4";

	/**
	 * 雇员使用的小程序
	 */
	private static final String appsecret = "10dfbf84c55ef72d06f5618d9ac30d0d";

	@ApiOperation(tags = Conts.Tags.WX, value = "回收员微信小程序登录接口", notes = "调用接口获取登录凭证（code）。通过凭证进而换取用户登录态信息，包括用户的唯一标识（openid）及本次登录的会话密钥（session_key）等")
	@PostMapping(value = "dologin/permit-all")
	@Transactional(rollbackFor = Exception.class)
	@ResponseBody
	public R dologin(@RequestBody JsCode jsCode) {

		WechatHttpApi wechatHttpApi = new WechatHttpApi(appid, appsecret);
		Jscode2sessionResponse jscode2session = wechatHttpApi.jscode2session(jsCode.getJsCode());
		final int errcode = jscode2session.getErrcode();
		final String openid = jscode2session.getOpenid();
		final String password = "jusm" + openid;
		if (errcode == 0) {
			UserInfo wxUserInfo = userInfoService.findByOpenid(openid);
			if (wxUserInfo == null) {
				// 直接注册成usm_user
				Map<String, String> map = authorizeService.addAndLoginApiUserIntoUSM(openid, password, null, null, null,
						"role_employee");
				User user = userService.findByUsername(openid);
				wxUserInfo = new UserInfo();
				wxUserInfo.setId(user.getId());
				wxUserInfo.setOpenid(openid);
				wxUserInfo.setSessionKey(jscode2session.getSessionKey());
				wxUserInfo.setUnionid(jscode2session.getUnionid());
				userInfoService.save(wxUserInfo);
				return R.success(map);
			} else {
				userInfoService.syncSessionKey(openid, jscode2session.getSessionKey());
				return R.success(authorizeService.login(openid, password));
			}
		} else if (errcode == -1) {
			return R.failure("系统繁忙，此时请开发者稍候再试");
		} else if (errcode == 40029) {
			return R.failure("无效的jscode");
		} else if (45011 == errcode) {
			return R.failure("频率限制，每个用户每分钟100次");
		} else if (40125 == errcode) {
			return R.failure("不可用的appsecret");
		} else if (40163 == errcode) {
			return R.failure("jscode已经使用");
		} else {
			return R.failure(JSON.toJSONString(jscode2session));
		}
	}

	@ApiOperation(tags = Conts.Tags.WX, value = "微信小程序登录接口", notes = "调用接口获取登录凭证（code）。通过凭证进而换取用户登录态信息，包括用户的唯一标识（openid）及本次登录的会话密钥（session_key）等")
	@PostMapping(value = "wx-login/permit-all")
	@Transactional(rollbackFor = Exception.class)
	@ResponseBody
	public R login(@RequestBody JsCode jsCode) {

		Jscode2sessionResponse jscode2session = wechatHttpApi.jscode2session(jsCode.getJsCode());
		final int errcode = jscode2session.getErrcode();
		final String openid = jscode2session.getOpenid();
		final String password = "jusm" + openid;
		if (errcode == 0) {
			UserInfo wxUserInfo = userInfoService.findByOpenid(openid);
			if (wxUserInfo == null) {
				// 直接注册成usm_user
				Map<String, String> map = authorizeService.addAndLoginApiUserIntoUSM(openid, password, null, null, null,
						"role_customer");
				User user = userService.findByUsername(openid);
				wxUserInfo = new UserInfo();
				wxUserInfo.setId(user.getId());
				wxUserInfo.setOpenid(openid);
				wxUserInfo.setSessionKey(jscode2session.getSessionKey());
				wxUserInfo.setUnionid(jscode2session.getUnionid());
				UserInfo save = userInfoService.save(wxUserInfo);
				if (userInitializationHandler != null && save != null) {
					userInitializationHandler.initialize(user);
				}
				return R.success(map);
			} else {
				userInfoService.syncSessionKey(openid, jscode2session.getSessionKey());
				return R.success(authorizeService.login(openid, password));
			}
		} else if (errcode == -1) {
			return R.failure("系统繁忙，此时请开发者稍候再试");
		} else if (errcode == 40029) {
			return R.failure("无效的jscode");
		} else if (45011 == errcode) {
			return R.failure("频率限制，每个用户每分钟100次");
		} else if (40125 == errcode) {
			return R.failure("不可用的appsecret");
		} else if (40163 == errcode) {
			return R.failure("jscode已经使用");
		} else {
			return R.failure(JSON.toJSONString(jscode2session));
		}
	}

	@ApiOperation(tags = Conts.Tags.WX, value = "获取微信用户信息", hidden = true)
	@PostMapping(value = "userinfo/{openid}/permit-all")
	@ResponseBody
	private R getUserInfo(@PathVariable String openid) throws IOException {
		return R.success(wechatHttpApi.getUserInfo(openid));
	}

	@ResponseBody
	@ApiOperation(tags = Conts.Tags.WX, value = "获取微信AccessToken", hidden = true)
	@PostMapping(value = "access-token/permit-all", headers = Conts.TOKENHEADERKEY)
	private R getAccessToken() throws IOException {
		String accessToken = wechatHttpApi.getAccessToken();
		return R.success(accessToken);
	}

	/**
	 * 解密用户敏感数据
	 * 
	 * @param encryptedData
	 *            明文
	 * @param iv
	 *            加密算法的初始向量
	 * @param sessionId
	 *            会话ID
	 * @return
	 */
	@ResponseBody
	@ApiOperation(tags = Conts.Tags.WX, value = "解密微信用户信息")
	@RequestMapping(value = "/userinfo/decode", method = RequestMethod.POST, produces = "application/json", headers = Conts.TOKENHEADERKEY)
	public R decodeUserInfo(@RequestBody EncryptedUserInfo encryptedUserInfo) {

		UserInfo userInfo = userInfoService.findOne(CurrentUser.getUserId());
		if (userInfo == null) {
			return R.failure("没有登录");
		}
		String sessionKey = userInfo.getSessionKey();
		String json = userInfoService.decodeUserInfo(encryptedUserInfo.getEncryptedData(), sessionKey,
				encryptedUserInfo.getIv());
		logger.info("======" + json);
		UserInfo syncUserInfo = userInfoService.syncUserInfo(json);
		return R.success(syncUserInfo);

	}

	@ApiOperation(tags = Conts.Tags.WX, value = "企业付款", notes = "用于企业向微信用户个人付款 \r\n"
			+ " 目前支持向指定微信用户的openid付款。（获取openid参见微信公众平台开发者文档： 网页授权获取用户基本信息）")
	@PostMapping(value = "wx-mmpaymkttransfers", headers = Conts.TOKENHEADERKEY)
	@ResponseBody
	private R bizPay() {
		// https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
		// https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
		return R.success();
	}

	@ApiOperation(tags = Conts.Tags.WX, value = "微信支付")
	@PostMapping(value = "wx-pay", headers = Conts.TOKENHEADERKEY)
	@ResponseBody
	private R pay() {
		// https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
		return R.success();
	}
}
