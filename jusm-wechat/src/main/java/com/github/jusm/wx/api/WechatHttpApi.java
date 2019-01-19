package com.github.jusm.wx.api;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.exception.UsmException;
import com.github.jusm.http.DefaultJusmClient;
import com.github.jusm.redis.RedisKeys;
import com.github.jusm.redis.RedisRepository;
import com.github.jusm.wx.api.response.Jscode2sessionResponse;
import com.github.jusm.wx.api.response.Template;
import com.github.jusm.wx.api.response.TemplateListResponse;
import com.github.jusm.wx.api.response.WeixinResponse;
import com.github.jusm.wx.exception.WeiXinException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
 * 
 * @author haoran.wen
 *
 */
public class WechatHttpApi {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 微信api url
	 */
	public static class URL {

		/**
		 * 
		 */
		public static final String API = "https://api.weixin.qq.com";

		private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

		private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

		/**
		 * 发送客服消息给用户。详细规则见 发送客服消息
		 */
		private static final String SEND_CUSTOMER_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

		/**
		 * 组合模板并添加至帐号下的个人模板库 POST
		 */
		private static final String ADD_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/wxopen/template/add?access_token=ACCESS_TOKEN";

		/**
		 * 删除帐号下的某个模板 POST
		 */
		private static final String DELETE_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/wxopen/template/del?access_token=ACCESS_TOKEN";

		/**
		 * 获取模板库某个模板标题下关键词库 POST
		 */
		private static final String GET_TEMPLATE_LIBRARY_BY_ID = "https://api.weixin.qq.com/cgi-bin/wxopen/template/library/get?access_token=ACCESS_TOKEN";

		/**
		 * 获取小程序模板库标题列表 POST
		 */
		private static final String GET_TEMPLATE_LIBRARY_LIST = "https://api.weixin.qq.com/cgi-bin/wxopen/template/library/list?access_token=ACCESS_TOKEN";

		/**
		 * 获取帐号下已存在的模板列表 POST
		 */
		private static final String GET_TEMPLATE_LIST = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token=ACCESS_TOKEN";

		/**
		 * 发送模板消息 POST
		 */
		private static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";

		private String jscode2session;

		private String accsessToken;

		private URL(String appid, String appsecret) {
			this.jscode2session = JSCODE2SESSION_URL.replaceAll("APPID", appid).replaceAll("SECRET", appsecret);
			this.accsessToken = ACCESS_TOKEN_URL.replaceAll("APPID", appid).replaceAll("SECRET", appsecret);
		}

		public String ofSendCustomerMessage(String accessToken) {
			return SEND_CUSTOMER_MESSAGE.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofAddTemplate(String accessToken) {
			return ADD_TEMPLATE.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofDeleteTemplate(String accessToken) {
			return DELETE_TEMPLATE.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofGetTemplateLibraryList(String accessToken) {
			return GET_TEMPLATE_LIBRARY_LIST.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofGetTemplateLibraryById(String accessToken) {
			return GET_TEMPLATE_LIBRARY_BY_ID.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofGetTemplateList(String accessToken) {
			return GET_TEMPLATE_LIST.replaceAll("ACCESS_TOKEN", accessToken);
		}

		public String ofSendTemplateMessage(String accessToken) {
			return SEND_TEMPLATE_MESSAGE.replaceAll("ACCESS_TOKEN", accessToken);
		}

		/**
		 * 获取jscode2session的url
		 * 
		 * @return
		 */
		public String ofJscode2session() {
			return jscode2session;
		}

		/**
		 * 获取accsessToken的url
		 * 
		 * @return
		 */
		public String ofAccessToken() {
			return accsessToken;
		}

	}

	private String appid;

	private String appsecret;

	private URL url;

	private DefaultJusmClient defaultJusmClient;

	@Autowired
	private RedisRepository redisRepository;

	public WechatHttpApi() {
		this.appid = "wxff290cefacb9ba11";
		this.appsecret = "e6f77aaff55c3355a009aa346a9b3309";
		this.url = new URL(appid, appsecret);
		this.defaultJusmClient = new DefaultJusmClient();
	}

	public  WechatHttpApi(String appid, String appsecret) {
		this.appid = appid;
		this.appsecret = appsecret;
		this.url = new URL(appid, appsecret);
		this.defaultJusmClient = new DefaultJusmClient();
	}

	/**
	 * 客服接口-发消息 发送文本消息时，支持添加可跳转小程序的文字链
	 * <a href="http://www.qq.com" data-miniprogram-appid="appid"
	 * data-miniprogram-path="pages/index/index">点击跳小程序</a>
	 * <ol>
	 * <li>data-miniprogram-appid 项，填写小程序appid，则表示该链接跳转小程序；</li>
	 * <li>data-miniprogram-path项，填写小程序路径，路径与app.json中保持一致，可带参数；</li>
	 * <li>对于不支持data-miniprogram-appid 项的客户端版本，如果有herf项，则仍然保持跳href中的链接；</li>
	 * <li>小程序发带小程序文字链的文本消息，data-miniprogram-appid必须是该小程序的appid。</li>
	 * </ol>
	 * 
	 * @param openId
	 * @param text
	 * @param content
	 * @return
	 */
	public WeixinResponse sendCustomerMessage(String openId, String text, String content) {
		try {
			String a = "{ \"touser\":" + openId + ", \"msgtype\": \"" + text + "\", \"text\": { \"content\": " + content
					+ "  } }";
			Response response = defaultJusmClient.execute("weixin", "add_template", url.ofAddTemplate(getAccessToken()),
					a, HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("发送客服消息给用户失败", e);
		}
		return null;
	}

	/**
	 * 登录凭证校验。通过 wx.login() 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程
	 * 
	 * @param jsCode
	 *            通过 wx.login() 接口获得临时登录凭证 code
	 */
	public Jscode2sessionResponse jscode2session(String jsCode) {

		String activeProfile = SpringContextHolder.getActiveProfile();
		if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
			Jscode2sessionResponse jr = new Jscode2sessionResponse();
			if (jsCode.startsWith("staff")) {
				jr.setOpenid(jsCode);
				jr.setSessionKey("aaaaaaaD+1FEd7UG+f2mpg==");
				return jr;
			} else if (jsCode.startsWith("boss")) {
				jr.setOpenid(jsCode);
				jr.setSessionKey("aaaaaaaD+1FEd7UG+f2mpg==");
				return jr;
			} else if (jsCode.startsWith("cust")) {
				jr.setOpenid(jsCode);
				jr.setSessionKey("aaaaaaaD+1FEd7UG+f2mpg==");
				return jr;
			}
		}

		String url = this.url.ofJscode2session().replaceAll("JSCODE", jsCode);
		Response response = defaultJusmClient.execute("weixin", "jscode2session", url, HttpMethod.GET);
		if (response.isSuccessful()) {
			try {
				String json = new String(response.body().bytes());
				Jscode2sessionResponse result = JSON.parseObject(json, Jscode2sessionResponse.class);
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getAccessToken(boolean reObtain) {
		if (redisRepository != null) {
			Long expire = redisRepository.getExpire(RedisKeys.USM_MODULE_PARCEL_WECHAT_ACCESS_TOKEN);
			if (!reObtain && expire != null && expire.longValue() > 0) {
				String token = redisRepository.get(RedisKeys.USM_MODULE_PARCEL_WECHAT_ACCESS_TOKEN, expire - 10);
				return token;
			}
		}
		Response response = defaultJusmClient.execute("weixin", "access_token", url.ofAccessToken(), HttpMethod.GET);
		ResponseBody body = response.body();
		if (response.isSuccessful()) {
			try {
				String responseBody = new String(body.bytes());
				JSONObject jo = JSONObject.parseObject(responseBody);
				if (jo.containsKey("access_token")) {
					String accessToken = jo.getString("access_token");
					long expires = jo.getLongValue("expires_in");
					if (redisRepository != null) {
						redisRepository.set(RedisKeys.USM_MODULE_PARCEL_WECHAT_ACCESS_TOKEN, accessToken, expires);
					}
					return accessToken;
				} else {
					throw new WeiXinException("Not contains key access_token!");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new WeiXinException("获取微信access_token失败", e);
			}
		} else {
			throw new UsmException("获取微信access_token失败:" + body);
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public String getUserInfo(String openid) {
		String token = redisRepository.get(RedisKeys.USM_MODULE_PARCEL_WECHAT_ACCESS_TOKEN, 7000L);

		return token;
	}

	public void addTemplate() {
		try {
			String a = "{ \"id\": \"AT0002\",  \"keyword_id_list\": [3, 4, 5] }";
			Response response = defaultJusmClient.execute("weixin", "add_template", url.ofAddTemplate(getAccessToken()),
					a, HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("添加消息模板失败", e);
		}
	}

	public void deleteTemplate(String templateId) {
		try {
			String a = "{\"template_id\": " + templateId + "}";
			Response response = defaultJusmClient.execute("weixin", "delete_template",
					url.ofDeleteTemplate(getAccessToken()), a, HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug("添加消息模板失败", e);
		}
	}

	public List<Template> getTemplateList(int offset, int count) {
		try {
			String accessToken = getAccessToken(false);
			String ofTemplate = url.ofGetTemplateList(accessToken);
			String a = "{\"offset\": " + offset + ",  \"count\": " + count + "}";
			Response response = defaultJusmClient.execute("weixin", "get_template_list", ofTemplate, a,
					HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
				TemplateListResponse templateListResponse = JSONObject.parseObject(string, TemplateListResponse.class);
				if (templateListResponse.getErrcode() == 0 && "ok".equals(templateListResponse.getErrmsg())) {
					List<Template> list = templateListResponse.getList();
					return list;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("查询消息模板失败", e);
		}
		return java.util.Collections.emptyList();
	}

	/**
	 * 获取模板库某个模板标题下关键词库
	 * 
	 * @param id
	 *            AT0002
	 * @return
	 */
	public List<Template> getTemplateLibraryById(String id) {
		try {
			String accessToken = getAccessToken(false);
			String ofTemplate = url.ofGetTemplateLibraryList(accessToken);
			String a = "{\"id\": " + id + "}";
			Response response = defaultJusmClient.execute("weixin", "get_template_library_by_id", ofTemplate, a,
					HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
				TemplateListResponse templateListResponse = JSONObject.parseObject(string, TemplateListResponse.class);
				if (templateListResponse.getErrcode() == 0 && "ok".equals(templateListResponse.getErrmsg())) {
					List<Template> list = templateListResponse.getList();
					return list;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("查询消息模板失败", e);
		}
		return java.util.Collections.emptyList();
	}

	public List<Template> getTemplateLibraryList(int offset, int count) {
		try {
			String accessToken = getAccessToken(false);
			String ofTemplate = url.ofGetTemplateLibraryList(accessToken);
			String a = "{\"offset\": " + offset + ",  \"count\": " + count + "}";
			Response response = defaultJusmClient.execute("weixin", "get_template_library_list", ofTemplate, a,
					HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
				TemplateListResponse templateListResponse = JSONObject.parseObject(string, TemplateListResponse.class);
				if (templateListResponse.getErrcode() == 0 && "ok".equals(templateListResponse.getErrmsg())) {
					List<Template> list = templateListResponse.getList();
					return list;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("查询消息模板失败", e);
		}
		return java.util.Collections.emptyList();
	}

	public void sendTemplateMessage(String openid, String templateId, String index, String formId) {
		try {
			String accessToken = getAccessToken(false);
			String ofTemplate = url.ofSendTemplateMessage(accessToken);
			String a = "{\r\n" + "  \"touser\": \"OPENID\",\r\n" + "  \"template_id\": \"TEMPLATE_ID\",\r\n"
					+ "  \"page\": \"index\",\r\n" + "  \"form_id\": \"FORMID\",\r\n" + "  \"data\": {\r\n"
					+ "    \"keyword1\": {\r\n" + "      \"value\": \"339208499\"\r\n" + "    },\r\n"
					+ "    \"keyword2\": {\r\n" + "      \"value\": \"2015年01月05日 12:30\"\r\n" + "    },\r\n"
					+ "    \"keyword3\": {\r\n" + "      \"value\": \"腾讯微信总部\"\r\n" + "    },\r\n"
					+ "    \"keyword4\": {\r\n" + "      \"value\": \"广州市海珠区新港中路397号\"\r\n" + "    }\r\n" + "  },\r\n"
					+ "  \"emphasis_keyword\": \"keyword1.DATA\"\r\n" + "}";
			Response response = defaultJusmClient.execute("weixin", "send_template_message", ofTemplate, a,
					HttpMethod.POST);
			ResponseBody body = response.body();
			if (response.isSuccessful()) {
				String string = body.string();
				logger.debug(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("查询消息模板失败", e);
		}
	}

	public String getAccessToken() {
		return getAccessToken(false);
	}
}
