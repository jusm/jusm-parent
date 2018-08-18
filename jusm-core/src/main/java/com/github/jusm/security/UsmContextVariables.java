package com.github.jusm.security;

import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.web.ServerProperties;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.service.ParameterService;

import static com.github.jusm.util.Conts.*;

/**
 * 这个类的作用主要是讲后台应用范围内数据传递给前端页面
 */
public class UsmContextVariables {

	private final ServletContext servletContext;

	private final AntPatternProperties antPatternProperties;

	private final UsmProperties usmProperties;

	private final ParameterService parameterService;

	private final ServerProperties serverProperties;

	public UsmContextVariables(ServletContext servletContext, AntPatternProperties antPatternProperties,
			UsmProperties usmProperties, ParameterService parameterService, ServerProperties serverProperties) {
		this.servletContext = servletContext;
		this.antPatternProperties = antPatternProperties;
		this.usmProperties = usmProperties;
		this.parameterService = parameterService;
		this.serverProperties = serverProperties;
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_INITIALIZE, antPatternProperties.getInit());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_SIGNIN, antPatternProperties.getLogin());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_SIGNOUT, antPatternProperties.getSignOut());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_REGISTER, antPatternProperties.getRegister());
		servletContext.setAttribute(USM_CONTEXT_FULLNAME, usmProperties.getFullname());
		servletContext.setAttribute(USM_CONTEXT_SHORTNAME, usmProperties.getShortname());
		String contextPath = serverProperties.getContextPath() == null ? "/" : serverProperties.getContextPath();
		servletContext.setAttribute(USM_CONTEXT_PATH, contextPath);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public AntPatternProperties getAntPatternProperties() {
		return antPatternProperties;
	}

	public UsmProperties getUsmProperties() {
		return usmProperties;
	}

	public ParameterService getParameterService() {
		return parameterService;
	}

	public ServerProperties getServerProperties() {
		return serverProperties;
	}

}