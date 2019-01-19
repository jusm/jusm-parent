package com.github.jusm.wx.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.exception.BizException;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.wx.entity.UserInfo;
import com.github.jusm.wx.repository.UserInfoRepository;
import com.github.jusm.wx.util.AES;

/**
 * 
 * 不用 @service 写在了自动配置类里了
 *
 */
public class UserInfoService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	public UserInfo findByOpenid(String openid) {
		UserInfo userInfo = userInfoRepository.findByOpenid(openid);
		return userInfo;
	}

	/**
	 * 同步用户session信息 sessionKey
	 * 
	 * @param openid
	 * @return
	 */
	public UserInfo syncSessionKey(final String openid, final String sessionKey) {
		UserInfo userInfo = userInfoRepository.findByOpenid(openid);
		if (userInfo != null) {
			userInfo.setOpenid(openid);
			userInfo.setSessionKey(sessionKey);
			userInfoRepository.save(userInfo);
		}
		return userInfo;
	}

	public static void main(String[] args) {
		JSONObject jsonObject = JSONObject.parseObject("{\r\n" + "		\"openId\": \"OPENID\",\r\n"
				+ "		\"nickName\": \"NICKNAME\",\r\n" + "		\"gender\": 1,\r\n" + "		\"city\": \"CITY\",\r\n"
				+ "		\"province\": \"PROVINCE\",\r\n" + "		\"country\": \"COUNTRY\",\r\n"
				+ "		\"avatarUrl\": \"AVATARURL\",\r\n" + "	  	\"unionId\": \"UNIONID\",\r\n"
				+ "	  	\"watermark\": {\r\n" + "	    	\"appid\": \"APPID\",\r\n"
				+ "	    	\"timestamp\": 2312231\r\n" + "		} \r\n" + "		}");

		JSONObject jsonObject2 = jsonObject.getJSONObject("watermark");
		String openid = jsonObject2.getString("appid");
		System.out.println(openid);

	}

	/**
	 * <code>
	 * {
		"openId": "OPENID",
		"nickName": "NICKNAME",
		"gender": GENDER,
		"city": "CITY",
		"province": "PROVINCE",
		"country": "COUNTRY",
		"avatarUrl": "AVATARURL",
	  	"unionId": "UNIONID",
	  	"watermark": {
	    	"appid": "APPID",
	    	"timestamp": TIMESTAMP
		} 
		}
		</code>
	 * 
	 * @param json
	 * @return
	 */
	public UserInfo syncUserInfo(final String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		// JSONObject jsonObject2 = jsonObject.getJSONObject("watermark");
		String openid = jsonObject.getString("openId");
		UserInfo userInfo = userInfoRepository.findByOpenid(openid);
		if (userInfo != null) {
			System.out.println("yes yes yes !");
			userInfo.setLanguage(jsonObject.getString("language"));
			userInfo.setNickName(jsonObject.getString("nickName"));
			userInfo.setUnionid(jsonObject.getString("unionId"));
			userInfo.setGender(jsonObject.getIntValue("gender"));
			userInfo.setProvince(jsonObject.getString("province"));
			userInfo.setCountry(jsonObject.getString("country"));
			userInfo.setCity(jsonObject.getString("city"));
			userInfo.setAvatarUrl(jsonObject.getString("avatarUrl"));
			userInfoRepository.save(userInfo);
		}
		return userInfo;
	}

	/**
	 * 微信获取信息
	 * 
	 * @param encrypted
	 * @param session_key
	 * @param iv
	 * @return
	 * @throws IOException
	 */
	public String decodeUserInfo(String encrypted, String session_key, String iv) {
		String json;
		try {
			json = AES.decrypt(encrypted, session_key, iv);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(ReturnCode.FAILTURE, "解码失败");
		}
		return json;
	}

	public UserInfo save(UserInfo userInfo) {
		return userInfoRepository.save(userInfo);
	}

	public UserInfo findOne(String userId) {
		return userInfoRepository.findOne(userId);
	}

	public long count() {
		return userInfoRepository.count();
	}

}
