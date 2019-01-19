package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class TransferLimitException extends BizException {

	private static final long serialVersionUID = 1L;

	public TransferLimitException(String data) {
		super(ReturnCode.TRANSFER_LIMIT_EXCEPTION, data);
	}

}
