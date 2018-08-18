package com.github.jusm.exception;


/**
 */
public class TokenException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new TokenException.
	 * @param method the unsupported HTTP request method
	 */
	public TokenException(String message) {
		super(message);
	}

}
