package com.github.jusm.exception;

import com.github.jusm.model.ReturnCode;

public class UploadFileException extends UsmException {

	public UploadFileException(String data) {
		super(ReturnCode.UPLOAD_ERROR, data);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
