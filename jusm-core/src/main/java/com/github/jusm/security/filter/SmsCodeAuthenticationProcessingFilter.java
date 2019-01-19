package com.github.jusm.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.github.jusm.security.token.MobileCodeAuthenticationToken;

/**
 * 短信验证码登录使用的过滤器
 */
public class SmsCodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
	public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

	private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
	private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;
	private boolean postOnly = true;

	public SmsCodeAuthenticationProcessingFilter() {
		super(new AntPathRequestMatcher("/sms-sign", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String mobile = obtainMobile(request);
		String code = obtainCode(request);

		if (mobile == null) {
			mobile = "";
		}

		if (code == null) {
			code = "";
		}

		mobile = mobile.trim();
		code = code.trim();

		AbstractAuthenticationToken authRequest = new MobileCodeAuthenticationToken(mobile, code);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}

	protected String obtainCode(HttpServletRequest request) {
		return request.getParameter(codeParameter);
	}

	protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}