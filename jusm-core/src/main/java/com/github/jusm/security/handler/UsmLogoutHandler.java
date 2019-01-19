package com.github.jusm.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.entity.User;
import com.github.jusm.service.UserService;

public class UsmLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 清除缓存
		if (authentication.getPrincipal() instanceof User) {
			User user = (User) authentication.getPrincipal();
			SpringContextHolder.getBean(UserService.class).cacheEvictById(user.getId());
		}
	}

}
