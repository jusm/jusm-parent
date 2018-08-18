package com.github.jusm.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.WebUtils;

import com.github.jusm.security.handler.UsmAuthenticationFailureHandler;
import com.github.jusm.util.Conts;
import com.google.code.kaptcha.Constants;

/** 
 * 登录动态码
 */
public class KaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public static final String SPRING_SECURITY_FORM_KAPTCHA_KEY = "kaptcha";
	
	private String kaptchaParameter = SPRING_SECURITY_FORM_KAPTCHA_KEY;
	
	
	public KaptchaAuthenticationFilter(String servletPath,String httpMethod, String failureUrl,AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(servletPath, httpMethod));
		setAuthenticationManager(authenticationManager);
		setContinueChainBeforeSuccessfulAuthentication(true);
		setAuthenticationFailureHandler(new UsmAuthenticationFailureHandler(failureUrl));
	}
	
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		Object attribute = WebUtils.getSessionAttribute(request, Conts.SESSION_USM_VERIFICATIONCODE_ENABLED_KEY);
		return attribute != null  && Boolean.valueOf(attribute.toString()) && super.requiresAuthentication(request, response);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
	
		if (this.requiresAuthentication(req, res)) {
			String expect = (String) WebUtils.getSessionAttribute(req, Constants.KAPTCHA_SESSION_KEY);
			String kaptcha = obtainKaptcha(req);
			if (expect != null && !expect.equalsIgnoreCase(kaptcha)) {
				unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("输入的验证码不正确"));
				return;
			}
		}
		chain.doFilter(request, response);
	}


	private String obtainKaptcha(HttpServletRequest request) {
		return request.getParameter(kaptchaParameter);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		return null;
	}
}