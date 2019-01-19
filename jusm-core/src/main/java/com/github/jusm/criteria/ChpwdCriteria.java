package com.github.jusm.criteria;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "修改密码")
public class ChpwdCriteria {

	@NotBlank(message = "登录用户名必填")
	private String username;

	@NotBlank(message = "旧密码必填")
	private String oldPwd;

	@NotBlank(message = "新密码必填")
	private String newPwd;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	

}
