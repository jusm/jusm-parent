package com.github.jusm.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

public class UsmLocaleResolver implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String lang = request.getParameter("l");
		if (StringUtils.isNotBlank(lang)) {
			String[] split = lang.split("_");
			Locale locale = new Locale(split[0], split[1]);
			return locale;
		}
		return Locale.getDefault();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

	}

}
