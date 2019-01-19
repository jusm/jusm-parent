package com.github.jusm.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.alibaba.fastjson.JSON;
import com.github.jusm.model.ReturnCode;

public class BizException extends RuntimeException{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReturnCode result;
	
	public BizException(ReturnCode result, String data) {
		super(data);
		this.result= result;
	}
	
	public ReturnCode getResult() {
		return result;
	}

	public void setResult(ReturnCode result) {
		this.result = result;
	}
	
	
	
}
