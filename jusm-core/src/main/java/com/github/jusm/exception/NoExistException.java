package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

/**
 *  无记录异常
 */
public class NoExistException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 152065222283791254L;
	
	public NoExistException(String msg) {
		super(ReturnCode.NO_EXIST_ERROR,msg);
	}
}
