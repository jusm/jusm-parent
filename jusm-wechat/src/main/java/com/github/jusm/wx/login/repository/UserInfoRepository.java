package com.github.jusm.wx.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.wx.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

	UserInfo findByOpenid(String openid);
	
}
