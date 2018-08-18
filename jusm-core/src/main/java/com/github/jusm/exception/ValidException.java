package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

/**
 *  检验异常
 */
public class ValidException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 152065222283791254L;
	
	public ValidException(String msg) {
		super(ReturnCode.VALID_ERROR,msg);
	}
}
