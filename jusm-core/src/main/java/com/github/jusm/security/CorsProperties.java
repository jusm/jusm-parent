package com.github.jusm.security;

import com.github.jusm.util.Conts;

/**
 * 跨域资源共享需要的配置信息
 */
public class CorsProperties {

	private String path = Conts.DEFAULT_REST_API_ANT_PATTERN;

	private String pathPattern = path + "/**";

	private String allowedOrigins = "*";

	private String[] allowedHeaders = { "Content-Type", Conts.TOKENHEADERKEY };

	private String[] allowedMethods = { "POST", "GET", "PUT", "DELETE", "OPTIONS" };

	private boolean allowCredentials = false;

	private Long maxAge = 2592000L;

	public String getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;
	}

	public String getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public String[] getAllowedHeaders() {
		return allowedHeaders;
	}

	public void setAllowedHeaders(String[] allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}

	public String[] getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(String[] allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public boolean isAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public Long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Long maxAge) {
		this.maxAge = maxAge;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
