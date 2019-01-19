package com.github.jusm.wx.model;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "wx.login 登录时获取的 code")
public class JsCode {

	@NotBlank(message = "必填")
	@ApiModelProperty(required = true, value = "wx.login 登录时获取的 jsCode", example = "test", notes = "没有appid请输入特提示在开发(dev)开发(test)环境下会返回一个模拟数据")
	private String jsCode;

	public String getJsCode() {
		return jsCode;
	}

	public void setJsCode(String jsCode) {
		this.jsCode = jsCode;
	}

}
