package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class ParseShortURLException extends BizException {

	private static final long serialVersionUID = 152065222283191254L;

	public ParseShortURLException(String msg) {
		super(ReturnCode.PARSE_SHORTURL_ERROR, msg);
	}

}
