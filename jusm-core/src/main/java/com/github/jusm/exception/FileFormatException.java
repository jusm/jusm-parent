package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class FileFormatException extends BizException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 152065222283191254L;
	
	public FileFormatException(String msg) {
		super(ReturnCode.FILE_FORMAT_ERROR,msg);
	}
	

}
