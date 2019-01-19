package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class NotFindBankAccountException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFindBankAccountException(String data) {
		super(ReturnCode.NOT_FIND_ACCOUNT_EXCEPTION, data);
	}

}
