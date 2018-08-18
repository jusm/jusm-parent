package com.github.jusm.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.github.jusm.util.Conts;

/**
 * 被认证请求向登录页面跳转的控制 根据被请求所需权限向不同登录页面跳转 <!-- 被认证请求根据所需权限跳转到不同的登录界面 -->
 * <beans:bean id="myAuthenticationEntryPoint" class=
 * "com.mango.jtt.springSecurity.MyAuthenticationEntryPoint">
 * <beans:property name="authEntryPointMap" ref=
 * "loginFormsMap"></beans:property>
 * <beans:constructor-arg name="loginFormUrl" value=
 * "/login"></beans:constructor-arg> </beans:bean>
 * 
 * <!-- 根据不同请求所需权限跳转到不同的登录界面 -->
 * <beans:bean id="loginFormsMap" class="java.util.HashMap">
 * <beans:constructor-arg> <beans:map>
 * <beans:entry key="/user/**" value="/login" />
 * <beans:entry key="/manager/**" value="/manager/login" />
 * <beans:entry key="/**" value="/login" /> </beans:map>
 * </beans:constructor-arg> </beans:bean>
 * 
 */
public class UsmLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public UsmLoginUrlAuthenticationEntryPoint() {
		super(Conts.login);
	}

	private Map<String, String> authEntryPointMap;

	private PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		String requestURI = request.getRequestURI().replace(request.getContextPath(), "");
		for (String url : this.authEntryPointMap.keySet()) {
			if (this.pathMatcher.match(url, requestURI)) {
				return this.authEntryPointMap.get(url);
			}
		}
		return super.determineUrlToUseForThisRequest(request, response, exception);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public Map<String, String> getAuthEntryPointMap() {
		return authEntryPointMap;
	}

	public void setAuthEntryPointMap(Map<String, String> authEntryPointMap) {
		this.authEntryPointMap = authEntryPointMap;
	}

}