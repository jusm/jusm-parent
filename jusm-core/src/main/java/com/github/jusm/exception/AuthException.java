package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class AuthException extends SysException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7959518151166532242L;

	public AuthException(String data) {
		super(ReturnCode.AUTH_ERROR, data);
	}

}
