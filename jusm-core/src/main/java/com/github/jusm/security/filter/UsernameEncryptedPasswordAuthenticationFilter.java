package com.github.jusm.security.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.jusm.util.Conts;
import com.github.jusm.util.RSA;

public class UsernameEncryptedPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String priKey = (String) request.getSession().getAttribute(Conts.SESSION_USM_PASSWORD_PRI_KEY);
		return RSA.decryptByPriKey(priKey, request.getParameter(getPasswordParameter()));
	}
}
