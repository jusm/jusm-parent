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

import com.github.jusm.security.UsmRequestMatcher;
import com.github.jusm.service.ParameterService;

public class UsmInstallFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UrlPathHelper helper = new UrlPathHelper();

	/**
	 * 系统参数服务
	 */
	private ParameterService parameterService;

	/**
	 * 系统路径
	 */
	private String contextPath;

	/**
	 * 安装路径 以 / 开始
	 */
	private String installPath;

	
	private String indexPath;

	/**
	 * 
	 */
	private String initPath;

	@Value("${server.ssl.enabled:false}")
	private boolean enabled;

	private RequestMatcher installRequestMatcher;

	private RequestMatcher initRequestMatcher;

	private UsmRequestMatcher usmRequestMatcher;

	public UsmInstallFilter(ParameterService parameterService, String indexPath, String installPath, String initPath,
			String contextPath, UsmRequestMatcher usmRequestMatcher) {
		this.parameterService = parameterService;
		this.installPath = installPath;
		this.initPath = initPath;
		this.indexPath = indexPath;
		this.contextPath = contextPath;
		this.installRequestMatcher = new AntPathRequestMatcher(installPath, "GET");
		this.initRequestMatcher = new AntPathRequestMatcher(initPath, "POST");
		this.usmRequestMatcher = usmRequestMatcher;
	}

	/**
	 * <pre>
	 *  如果是安装页面路径或者初始化请求
	 *  如果已经安装直接跳转到主页 
	 *  true : 直接跳过本类的 doFilterInternal 方法 
	 *  false : 会调用本类的 doFilterInternal  方法
	 *  </pre>
	 */
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		if (usmRequestMatcher.getWebIgnoreRequestMatcher().matches(request)) {
			return true;
		} else if (parameterService.isSetup()) {
			if (installRequestMatcher.matches(request) || initRequestMatcher.matches(request)) {
				return false;
			} else {
				return true;
			}
		} else {
			return installRequestMatcher.matches(request) || initRequestMatcher.matches(request);
		}

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (!parameterService.isSetup()) {
			String install = getCompleteURL(request, installPath);
			logger.warn("系统未初始化请先初始化");
			response.sendRedirect(install);
		} else {
			String uri = helper.getLookupPathForRequest(request);
			if (initPath.equals(uri) || installPath.equals(uri)) {
				response.sendRedirect(getCompleteURL(request, indexPath));
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

	private String getCompleteURL(HttpServletRequest request, String installPath) {
		String scheme = enabled ? "https://" : "http://";
		String url = scheme + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() // 端口号
				+ contextPath;
		String install = url.endsWith("/") ? (url + installPath.substring(1)) : (url + installPath);
		return install;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

}
