package com.github.jusm.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.alibaba.fastjson.JSON;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.util.Conts;

public class UsmAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.setCharacterEncoding("UTF-8");
		if (isAjax(request)) {
			response.setContentType("application/json");
			response.getWriter().println(JSON.toJSONString(R.result(ReturnCode.NOT_LOGGED_IN_ERROR)));
		} else {
			response.sendRedirect(Conts.login);
		}

	}

	public boolean isAjax(HttpServletRequest request) {
		return StringUtils.isNotBlank(request.getHeader("X-Requested-With"));
	}

}
