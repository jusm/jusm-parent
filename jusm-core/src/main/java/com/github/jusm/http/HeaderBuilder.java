package com.github.jusm.http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.exception.UsmException;
import com.github.jusm.model.R;
import com.github.jusm.signature.token.AccessTokenGenerater;


public class HeaderBuilder {

	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String HEAD_KEY_CONTENT_TYPE = "Content-Type";
	public static final String HEAD_KEY_KYE = "kye";
	public static final String HEAD_KEY_ACCESS_TOKEN = "access-token";

	@Value("${usm.http.client.header.contentType:application/json;charset=UTF-8}")
	private String contentType;
	@Value("${usm.http.client.header.kye:10005}")
	private String kye;
	@Value("${usm.http.client.header.accesskey:A63F2C3FF29A606979AE9D25F737B342}")
	private String salt;

	public Map<String, String> create(String json) {
		Map<String, String> headerParam = new HashMap<>();
		headerParam.put(HEAD_KEY_CONTENT_TYPE, contentType);
		headerParam.put(HEAD_KEY_KYE, kye);
		R md5Result = AccessTokenGenerater.getMD5Result(salt, json);
		if(md5Result.isSuccess()) {
			AccessTokenGenerater.SignResult data = (AccessTokenGenerater.SignResult)md5Result.getData();
			String jsonString = JSONObject.toJSONString(data);
			logger.info(jsonString);
			headerParam.put(HEAD_KEY_ACCESS_TOKEN, data.getSign());
			return headerParam;
		} else {
			throw new UsmException("生成签名失败");
		}
	}
}
