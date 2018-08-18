package com.github.jusm.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.alibaba.fastjson.JSON;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;

/**
 * 接入处理程序
 */
public class UsmAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		if (isAjax(request)) {
			response.setContentType("application/json");
			response.getWriter().println(JSON.toJSONString(R.result(ReturnCode.UNAUTHORIZED_ERROR)));
		} else {
			response.sendRedirect("403");
		}
	}

	public boolean isAjax(HttpServletRequest request) {
		return StringUtils.isNotBlank(request.getHeader("X-Requested-With"));
	}

}