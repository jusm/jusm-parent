package com.github.jusm.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.jusm.exception.ValidException;
import com.github.jusm.model.R;
import com.github.jusm.signature.token.AccessTokenGenerater;

import io.swagger.annotations.ApiOperation;

@Controller
public class JSONToolController {
	
	@ApiOperation(value = "token 生成器的页面", hidden=true)
	@RequestMapping(value = "jsontool.html", method = RequestMethod.GET)
	public String index() {
		return "jsontool"; 
	}
	
	@RequestMapping(value = "/signature", method = RequestMethod.POST)
	@ResponseBody
	public R accessToken(@RequestBody VO vo) {
		if (StringUtils.isBlank(vo.getSecretKey())) {
			return R.failure(new ValidException("请输入正确的秘钥!"));
		}
		return AccessTokenGenerater.getMD5Result(StringUtils.trimToEmpty(vo.getSecretKey()), StringUtils.trimToEmpty(vo.getJsonParams()));
	}
}

class VO{
	private String secretKey;	
	
	private String jsonParams;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}
	
}
