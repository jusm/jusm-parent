package com.github.jusm.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 当用户试图访问安全的REST资源而不提供任何凭据时，就会调用此选项。 我们应该发送401的未经授权的响应，因为没有“登录页”重定向到
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -8970718410437077606L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		// This is invoked when user tries to access a secured REST resource without
		// supplying any credentials
		// We should just send a 401 Unauthorized response because there is no 'login
		// page' to redirect to
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
