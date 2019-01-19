package com.github.jusm.security.provider;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.jusm.util.Conts;
import com.github.jusm.util.RSA;


public class UsernamePasswordAuthenticationProvider extends DaoAuthenticationProvider {
	 
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		Object credentials = authentication.getCredentials();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String priKey = (String) request.getSession().getAttribute(Conts.SESSION_USM_PASSWORD_PRI_KEY);
		request.getSession().removeAttribute(Conts.SESSION_USM_PASSWORD_PRI_KEY);
		String decryptByPriKey = RSA.decryptByPriKey(priKey,String.valueOf(credentials));
		authentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), decryptByPriKey);
		super.additionalAuthenticationChecks(userDetails, authentication);
	}

}
