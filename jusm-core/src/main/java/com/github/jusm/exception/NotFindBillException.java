package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class NotFindBillException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFindBillException(String data) {
		super(ReturnCode.NOT_FIND_BILL_EXCEPTION, data);
	}

}
