package com.github.jusm.security;

import com.github.jusm.util.Conts;

public class JwtProperties {

	private String tokenPrefix = Conts.TOKEN_PREFIX;

	private String tokenHeaderKey = Conts.TOKENHEADERKEY;

	private String secret = "haoran";

	private Long expiration = Conts.DEFAULT_MAX_AGE;

	private boolean validation = true;

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public String getTokenHeaderKey() {
		return tokenHeaderKey;
	}

	public void setTokenHeaderKey(String tokenHeaderKey) {
		this.tokenHeaderKey = tokenHeaderKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

}
