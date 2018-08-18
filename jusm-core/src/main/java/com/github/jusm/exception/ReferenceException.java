package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class ReferenceException  extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 152065222283191254L;
	
	public ReferenceException(String msg) {
		super(ReturnCode.REFERENCE_ERROR,msg);
	}
	
	
}