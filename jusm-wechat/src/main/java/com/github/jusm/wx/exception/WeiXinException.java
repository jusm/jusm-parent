package com.github.jusm.wx.exception;

import com.github.jusm.exception.UsmException;
import com.github.jusm.model.ReturnCode;

public class WeiXinException extends UsmException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WeiXinException(String data, Throwable e) {
		super(ReturnCode.WEIXIN_ACCESSTOKEN_FAILED, data, e);
	}

	public WeiXinException(String data) {
		super(ReturnCode.WEIXIN_ACCESSTOKEN_FAILED, data);
	}
	
}
