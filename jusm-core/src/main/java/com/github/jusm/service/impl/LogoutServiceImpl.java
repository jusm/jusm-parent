package com.github.jusm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.jusm.model.R;
import com.github.jusm.redis.RedisKeys;
import com.github.jusm.redis.RedisUtil;
import com.github.jusm.security.CurrentUser;
import com.github.jusm.security.JwtTokenHandler;
import com.github.jusm.service.LogoutService;
import com.github.jusm.web.WebContextHolder;

@Service
public class LogoutServiceImpl implements LogoutService {

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private JwtTokenHandler jwtTokenHandler;

	public R logout() {
		String username = CurrentUser.getUser().getUsername();
		String key = RedisKeys.getExpiredTokenKey(username);
		String value = WebContextHolder.getRequest().getHeader(jwtTokenHandler.getTokenHeaderKey());
		redisUtil.add(key, jwtTokenHandler.getExpiration(), value.substring(jwtTokenHandler.getTokenPrefix().length()));
		return R.success("退出成功!");
	}
}
