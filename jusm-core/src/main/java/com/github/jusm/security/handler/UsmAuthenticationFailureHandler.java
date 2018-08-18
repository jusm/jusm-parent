package com.github.jusm.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.WebUtils;

import com.github.jusm.util.Conts;

/**
 * 登录失败控制
 * 
 */
public class UsmAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public UsmAuthenticationFailureHandler() {
		this("/login?error=true");
	}

	public UsmAuthenticationFailureHandler(String failureUrl) {
		super(failureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// 保存错误次数
		Object failtureTimes = WebUtils.getSessionAttribute(request, Conts.SESSION_USM_LOGIN_FAILURE_TIMES_KEY);
		Integer times = 1;
		if (null == failtureTimes) {
			WebUtils.setSessionAttribute(request, Conts.SESSION_USM_LOGIN_FAILURE_TIMES_KEY, times);
		} else {
			times = Integer.valueOf(failtureTimes.toString()) + 1;
			WebUtils.setSessionAttribute(request, Conts.SESSION_USM_LOGIN_FAILURE_TIMES_KEY, times);
			WebUtils.setSessionAttribute(request, Conts.SESSION_USM_LOGIN_FAILURE_MSG_KEY, exception);
		}
		super.onAuthenticationFailure(request, response, exception);
	}
}