package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

/**
 *  检验异常
 */
public class RepeatException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 152065222283791254L;
	
	public RepeatException(String msg) {
		super(ReturnCode.REPEAT_ERROR,msg);
	}
}
