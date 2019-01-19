package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class AccountLockedException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountLockedException(String data) {
		super(ReturnCode.ACCOUNT_LOCKED_EXCEPTION, data);
	}
}
