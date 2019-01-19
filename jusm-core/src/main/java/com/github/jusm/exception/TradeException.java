package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class TradeException extends BizException {

	private static final long serialVersionUID = 1L;

	public TradeException(String data) {
		super(ReturnCode.ACCOUNT_TRADE_EXCEPTION, data);
	}

}
