package com.github.jusm.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.github.jusm.security.JwtAccessDecisionManager;

/**
 * 该过滤器的主要作用就是通过spring著名的IoC生成securityMetadataSource。
 * securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService。
 * 该MyInvocationSecurityMetadataSourceService的作用提从数据库提取权限和资源，装配到HashMap中，
 * 供Spring Security使用，用于权限校验。
 * 
 */
public class UsmSecurityInterceptorFilter extends AbstractSecurityInterceptor implements Filter {

	private static final Logger logger = Logger.getLogger(UsmSecurityInterceptorFilter.class);

	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	public UsmSecurityInterceptorFilter(FilterInvocationSecurityMetadataSource securityMetadataSource,
			JwtAccessDecisionManager jwtAccessDecisionManager, AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
		super.setAccessDecisionManager(jwtAccessDecisionManager);
		this.securityMetadataSource = securityMetadataSource;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void destroy() {
		logger.debug("filter===========================end");
	}

	public void init(FilterConfig filterconfig) throws ServletException {
		logger.debug("filter===============init=============");
	}
}