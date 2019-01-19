package com.github.jusm.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.entity.User;
import com.github.jusm.service.UserService;

public class UsmLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
//		清除缓存
		if(authentication.getPrincipal() instanceof User) {
			User user  = (User)authentication.getPrincipal();
			SpringContextHolder.getBean(UserService.class).cacheEvictById(user.getId());
		}
	}

}
