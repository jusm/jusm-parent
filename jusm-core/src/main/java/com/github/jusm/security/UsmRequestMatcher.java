package com.github.jusm.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class UsmRequestMatcher {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public UsmRequestMatcher(String[] permitAllAntPatterns, String[] webIngoreAntPatterns,
			String[] authorizedPathPatterns) {
		initRequestMatcher(permitAllAntPatterns, webIngoreAntPatterns, authorizedPathPatterns);
	}

	private void initRequestMatcher(String[] permitAllAntPatterns, String[] webIngoreAntPatterns,
			String[] authorizedPathPatterns) {
		List<RequestMatcher> securityRequestMatchers = new ArrayList<>();
		for (int i = 0; i < authorizedPathPatterns.length; i++) {
			securityRequestMatchers.add(new AntPathRequestMatcher(authorizedPathPatterns[i]));
		}
		setAuthorizeRequestMatcher(new OrRequestMatcher(securityRequestMatchers));
		List<RequestMatcher> publicRequestMatchers = new ArrayList<>();
		for (String string : permitAllAntPatterns) {
			RequestMatcher pathRequestMatcher = new AntPathRequestMatcher(string);
			publicRequestMatchers.add(pathRequestMatcher);
		}
		setPermitAllRequestMatcher(new OrRequestMatcher(publicRequestMatchers));

		List<RequestMatcher> staticUrl = new ArrayList<>();
		for (int i = 0; i < webIngoreAntPatterns.length; i++) {
			logger.debug("Add staticsPathPatterns" + webIngoreAntPatterns[i]);
			RequestMatcher pathGetRequestMatcher = new AntPathRequestMatcher(webIngoreAntPatterns[i], "GET");
			RequestMatcher pathPostRequestMatcher = new AntPathRequestMatcher(webIngoreAntPatterns[i], "POST");
			staticUrl.add(pathGetRequestMatcher);
			staticUrl.add(pathPostRequestMatcher);
		}
		setWebIgnoreRequestMatcher(new OrRequestMatcher(staticUrl));
	}

	/**
	 * 需要鉴权的请求
	 */
	private OrRequestMatcher authorizeRequestMatcher;

	/**
	 * 允许所有角色访问的请求 任然走security框架
	 */
	private OrRequestMatcher permitAllRequestMatcher;

	/**
	 * web静态资源 不走security框架filter
	 */
	private RequestMatcher webIgnoreRequestMatcher;

	/**
	 * {@link AntPatternProperties}
	 * {@value /api/login,/api/init-root,/api/register,...}
	 * 
	 * @return
	 */
	public OrRequestMatcher getPermitAllRequestMatcher() {
		return permitAllRequestMatcher;
	}

	/**
	 * JWT-Based support CORS RESTful Web 服务
	 * 
	 * @return
	 */
	public OrRequestMatcher getAuthorizeRequestMatcher() {
		return authorizeRequestMatcher;
	}

	public void setAuthorizeRequestMatcher(OrRequestMatcher authorizeRequestMatcher) {
		this.authorizeRequestMatcher = authorizeRequestMatcher;
	}

	public void setPermitAllRequestMatcher(OrRequestMatcher permitAllRequestMatcher) {
		this.permitAllRequestMatcher = permitAllRequestMatcher;
	}

	/**
	 * <p>
	 * eg. /statics/**,/static/** ..../
	 * </p>
	 * 
	 * @return
	 */
	public RequestMatcher getWebIgnoreRequestMatcher() {
		return webIgnoreRequestMatcher;
	}

	public void setWebIgnoreRequestMatcher(RequestMatcher webIgnoreRequestMatcher) {
		this.webIgnoreRequestMatcher = webIgnoreRequestMatcher;
	}
}
