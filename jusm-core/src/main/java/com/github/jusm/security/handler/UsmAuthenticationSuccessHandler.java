package com.github.jusm.security.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.github.jusm.entities.User;

/**
 * 登录授权成功后操作控制，如果是直接点击登录的情况下，根据授权权限跳转不同页面； 否则跳转到原请求页面
 * 
 */
public class UsmAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Map<String, String> authDispatcherMap;
	private RequestCache requestCache = new HttpSessionRequestCache();

	private String contextPath = "/";

	public UsmAuthenticationSuccessHandler(String contextPath) {
		if (contextPath == null || "".equals(contextPath.trim())) {
			this.contextPath = "/";
		} else {
			this.contextPath = contextPath;
		}
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@PostConstruct
	private void init() {
		authDispatcherMap = new ConcurrentHashMap<>();
		if (!contextPath.endsWith("/")) {
			contextPath += "/";
		}
		authDispatcherMap.put("role_admin", "/console");
		authDispatcherMap.put("role_actuator", "/console");
		authDispatcherMap.put("role_dba", "/console");
		authDispatcherMap.put("role_caller", "/swagger-ui.html");
		authDispatcherMap.put("role_user", "/console");
	}

	// @Autowired
	// private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 获取用户权限
		Collection<? extends GrantedAuthority> authCollection = authentication.getAuthorities();
		if (authCollection.isEmpty()) {
			return;
		}

		// 认证成功后，获取用户信息并添加到session中
		User user = (User) authentication.getPrincipal();
		// User user = userService.findByUsername(userDetails.getUsername());
		request.getSession().setAttribute("user", user);
		String url = null;
		// 从别的请求页面跳转过来的情况，savedRequest不为空
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null && StringUtils.isNotBlank(savedRequest.getRedirectUrl())) {
			url = savedRequest.getRedirectUrl();
		}else if (user != null && user.getDefaultRole() != null) {
			url = authDispatcherMap.get(user.getDefaultRole().getAuthority());
		} else {
			url = "/index";
		}
		getRedirectStrategy().sendRedirect(request, response, url); 
	}

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	public Map<String, String> getAuthDispatcherMap() {
		return authDispatcherMap;
	}

	public void setAuthDispatcherMap(Map<String, String> authDispatcherMap) {
		this.authDispatcherMap = authDispatcherMap;
	}

}