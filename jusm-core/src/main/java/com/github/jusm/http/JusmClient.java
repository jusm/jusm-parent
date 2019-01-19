package com.github.jusm.http;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.hystrix.UsmHystrixCommandWapper;
import com.github.jusm.model.R;
import com.ning.http.client.Response;

public class JusmClient {
	
	private Logger logger =LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UsmHystrixCommandWapper usmHystrixCommandWapper;
	
	@Autowired
	private HeaderBuilder usmHeaderBuilder;
	
	public R execute(String seriveName, String methodName, String url, String json, HttpMethod httpMethod) throws IOException {
		Map<String, String> headers = usmHeaderBuilder.create(json);
		Response response = usmHystrixCommandWapper.execute(seriveName, methodName, url, headers, json, httpMethod);
		String responseBody = response.getResponseBody();
		logger.info(responseBody);
		R r = JSONObject.parseObject(responseBody, R.class);
		return r;
	}
}
