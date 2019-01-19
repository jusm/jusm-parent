package com.github.jusm.web.xxs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * XSS过滤处理
 * 
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	// 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
	protected HttpServletRequest orgRequest;
	public void setOrgRequest(HttpServletRequest orgRequest) {
		this.orgRequest = orgRequest;
	}

	protected String originalBody;
	public String getOriginalBody() {
		return originalBody;
	}

	public void setOriginalBody(String originalBody) {
		this.originalBody = originalBody;
	}

	public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		orgRequest = request;

	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 非json类型，直接返回
		if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))
				|| MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))) {
			// 为空，直接返回
			String json = IOUtils.toString(super.getInputStream(), "utf-8");
			if (StringUtils.isBlank(json)) {
				return super.getInputStream();
			}
			setOriginalBody(json);
			// xss过滤 TODO
			final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
			return new ServletInputStream() {
				@Override
				public boolean isFinished() {
					return true;
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener readListener) {
				}

				@Override
				public int read() throws IOException {
					return bis.read();
				}
			};
		}
		return super.getInputStream();

	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(xssEncode(name));
		if (StringUtils.isNotBlank(value)) {
			value = xssEncode(value);
		}
		return StringEscapeUtils.escapeHtml4(value);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null && values.length > 0) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				escapseValues[i] = StringEscapeUtils.escapeHtml4(xssEncode(values[i]));
			}
			return escapseValues;
		}
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (String key : parameters.keySet()) {
			String[] values = parameters.get(key);
			for (int i = 0; i < values.length; i++) {
				values[i] = xssEncode(values[i]);
			}
			map.put(key, values);
		}
		return map;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(xssEncode(name));
		if (StringUtils.isNotBlank(value)) {
			value = xssEncode(value);
		}
		return StringEscapeUtils.escapeHtml4(value);
		// return value;
	}

	@Override
	public String getQueryString() {
		return StringEscapeUtils.escapeHtml4(super.getQueryString());
	}

	protected String xssEncode(String input) {
//		return htmlFilter.filter(input);
		return input;
	}

	/**
	 * 获取最原始的request
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * 获取最原始的request
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
		if (request instanceof XssHttpServletRequestWrapper) {
			return ((XssHttpServletRequestWrapper) request).getOrgRequest();
		}
		return request;
	}

}
