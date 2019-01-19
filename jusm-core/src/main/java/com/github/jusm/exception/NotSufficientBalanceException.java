package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class NotSufficientBalanceException extends BizException {

	private static final long serialVersionUID = 1L;

	public NotSufficientBalanceException(String data) {
		super(ReturnCode.NOT_SUFFICIENT_BALANCE, data);
	}

}
