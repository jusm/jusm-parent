package com.github.jusm.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;


public class UsmErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		Exception exception = (Exception) requestAttributes.getAttribute("javax.servlet.error.exception",
				RequestAttributes.SCOPE_REQUEST);
		R r = R.failure(exception);
		errorAttributes.put("success", r.isSuccess());
		errorAttributes.put("code", r.getCode());
		errorAttributes.put("data", r.getData());
		errorAttributes.put("msg", r.getMsg());
		if (ReturnCode.UNKNOW_ERROR.getCode().equals(r.getCode())) {
			includeStackTrace = true;
			errorAttributes.putAll(super.getErrorAttributes(requestAttributes, includeStackTrace));
		}
		return errorAttributes;
	}
}
