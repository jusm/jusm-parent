package com.github.jusm.web.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.jusm.util.Conts;

/**
 * 跨域资源共享需要的配置信息
 */
@ConfigurationProperties(prefix = "usm.cors", ignoreUnknownFields = true)
public class CorsProperties {

	/**
	 * 是否启动跨越资源共享功能
	 */
	private boolean enabled = false;

	private String pathPattern = Conts.DEFAULT_REST_API_ANT_PATTERN;

	private String[] allowedOrigins = {"*"};

	private String[] allowedHeaders = { "Content-Type", Conts.TOKENHEADERKEY };

	private String[] allowedMethods = { "POST", "GET", "PUT", "DELETE", "OPTIONS" };

	private boolean allowCredentials = false;

	private Long maxAge = Conts.DEFAULT_MAX_AGE;//30天
	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;
	}

	public String[] getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String[] allowedOrigins) {
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
}
