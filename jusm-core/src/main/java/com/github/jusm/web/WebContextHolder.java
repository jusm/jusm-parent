package com.github.jusm.web;

import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Title: Web上下文工具类
 */
public class WebContextHolder {

	public static String getCompleteURL(String uri) {
		if (org.apache.commons.lang3.StringUtils.isBlank(uri)) {
			return null;
		}
		StringBuilder url = new StringBuilder();
		url.append(getRequest().getScheme()).append("://").append(getRequest().getServerName());
		int serverPort = getRequest().getServerPort();
		if (80 != serverPort) {
			url.append(":").append(serverPort);
		}
		String completeURL = uri.startsWith("/") ? url.toString() + uri : (url.toString() + "/" + uri);
		return completeURL.replace("\\", "/");
	}

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		return response;
	}

	public static String getCookie(String key) {
		if (!StringUtils.isEmpty(key)) {
			Cookie[] cookies = getRequest().getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (key.equals(cookies[i].getName())) {
						return cookies[i].getValue();
					}
				}
			}
		}
		return null;
	}

	public static ServletContext getContext() {
		return getRequest().getServletContext();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static Principal getUserPrincipal() {
		return getRequest().getUserPrincipal();
	}
}
