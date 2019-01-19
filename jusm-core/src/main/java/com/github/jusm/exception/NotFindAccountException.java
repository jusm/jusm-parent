package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class NotFindAccountException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFindAccountException(String data) {
		super(ReturnCode.NOT_FIND_BANKACCOUNT_EXCEPTION, data);
	}

}
