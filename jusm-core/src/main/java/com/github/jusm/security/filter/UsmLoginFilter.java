package com.github.jusm.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import com.github.jusm.util.Conts;
import com.github.jusm.util.RSA;
import com.github.jusm.util.RSA.KeyPairs;

public class UsmLoginFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 工具
	 */
	private final UrlPathHelper pathHelper = new UrlPathHelper();

	/**
	 * 是否启动动态码
	 */
	private boolean verificationCode;

	/**
	 * 密码输错次数
	 */
	private int loginFailureTimes;

	/**
	 * 系统路径
	 */
	private String contextPath;

	private RequestMatcher loginPageRequestMatcher;

	public UsmLoginFilter(boolean verificationCode, int loginFailureTimes, String loginPath) {
		this.verificationCode = verificationCode;
		this.loginFailureTimes = loginFailureTimes;
		this.loginPageRequestMatcher = new AntPathRequestMatcher(loginPath, "GET");
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		/*
		String requestURI = request.getRequestURI();
		String string = request.getRequestURL().toString();
		logger.info(requestURI + string);
		String lookupPathForRequest = pathHelper.getLookupPathForRequest(request);
		String parameter = request.getParameter("l");
		logger.info(lookupPathForRequest + parameter);
		*/
		return !loginPageRequestMatcher.matches(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Object failtureTimes = WebUtils.getSessionAttribute(request, Conts.SESSION_USM_LOGIN_FAILURE_TIMES_KEY);
		Integer times = 0;
		if (null != failtureTimes) {
			times = Integer.valueOf(failtureTimes.toString());
		}
		WebUtils.setSessionAttribute(request, Conts.SESSION_USM_VERIFICATIONCODE_ENABLED_KEY,
				verificationCode && times > loginFailureTimes);
		// 设置登录密码的钥
		KeyPairs generateKey = RSA.generateKey();
		logger.info("pubKey: " + generateKey.getPubKey());
		logger.info("priKey: " + generateKey.getPriKey());
		WebUtils.setSessionAttribute(request, Conts.SESSION_USM_PASSWORD_PUB_KEY, generateKey.getPubKey());
		WebUtils.setSessionAttribute(request, Conts.SESSION_USM_PASSWORD_PRI_KEY, generateKey.getPriKey());
		filterChain.doFilter(request, response);
	}

	public boolean isVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(boolean verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

}
