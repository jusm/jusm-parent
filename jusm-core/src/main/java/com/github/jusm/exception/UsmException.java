package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

/**
 * 自定义异常
 */
public class UsmException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private ReturnCode resultCode;
	
	public UsmException(String data) {
		super(data);
		this.resultCode= ReturnCode.FAILTURE;
	}
	
	public UsmException(String data,Throwable e) {
		super(data,e);
		this.resultCode= ReturnCode.FAILTURE;
	}
	
	public UsmException(ReturnCode resultCode, String data) {
		super(data);
		this.resultCode= resultCode;
	}
	
	public UsmException(ReturnCode resultCode, String data,Throwable e) {
		super(data,e);
		this.resultCode= resultCode;
	}
	
	public ReturnCode getReturnCode() {
		return resultCode;
	}

	public void setReturnCode(ReturnCode resultCode) {
		this.resultCode = resultCode;
	}
	


}
