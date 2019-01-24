package com.github.jusm.wx.login.model;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;

@ApiModel("加密的用户信息")
public class EncryptedUserInfo {
	
	@NotBlank(message="encryptedData必填")
	private String encryptedData;
	
	@NotBlank(message="iv必填")
	private String iv;
	
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	
}
