package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class FailureOfRechargeException extends BizException {

	private static final long serialVersionUID = 1L;

	public FailureOfRechargeException(String data) {
		super(ReturnCode.NOT_SUFFICIENT_BALANCE, data);
	}

}