package com.github.jusm.criteria;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "忘记密码")
public class ForgetPwdCriteria {

	@NotBlank(message = "登录用户名必填")
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
