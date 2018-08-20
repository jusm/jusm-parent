package com.github.jusm.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.github.jusm.model.R;
import com.github.jusm.redis.RedisKeys;
import com.github.jusm.redis.RedisTemplateFacade;
import com.github.jusm.security.CurrentUser;
import com.github.jusm.security.JwtTokenHandler;
import com.github.jusm.service.LogoutService;
import com.github.jusm.web.WebContextHolder;

@Service
public class LogoutServiceImpl implements LogoutService {

	@Autowired
	private RedisTemplateFacade redisTemplateFacade;

	@Autowired
	private JwtTokenHandler jwtTokenHandler;

	public R logout() {
		String username = CurrentUser.getUser().getUsername();
		String key = RedisKeys.getExpiredTokenKey(username);
		String value = WebContextHolder.getRequest().getHeader(jwtTokenHandler.getTokenHeaderKey());
		redisTemplateFacade.add(key, value.substring(jwtTokenHandler.getTokenPrefix().length()), jwtTokenHandler.getExpiration());
		return R.success("退出成功!");
	}
}
