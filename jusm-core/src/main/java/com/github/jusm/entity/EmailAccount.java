package com.github.jusm.entity;

public class EmailAccount {
	// 邮箱用户
	private String username;

	// 邮箱密码
	private String password;

	// 邮箱服务器
	private String place;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

}
