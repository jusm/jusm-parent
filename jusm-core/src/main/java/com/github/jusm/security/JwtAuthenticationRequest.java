package com.github.jusm.security;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户信息", description = "JWT用户登录认证")
public class JwtAuthenticationRequest implements Serializable {

	private static final long serialVersionUID = -8445943548965154778L;

	@ApiModelProperty(example = "root", value = "用户名", position = 1, required = true)
	private String username;

	@ApiModelProperty(example = "123456", value = "密码", position = 2, required = true)
	private String password;

	public JwtAuthenticationRequest() {
		super();
	}

	public JwtAuthenticationRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	private void setPassword(String password) {
		this.password = password;
	}
}
