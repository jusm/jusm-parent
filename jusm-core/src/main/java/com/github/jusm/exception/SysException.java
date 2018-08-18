package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class SysException extends RuntimeException{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReturnCode result;
	
	public SysException(ReturnCode result, String data) {
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
