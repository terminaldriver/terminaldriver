package com.terminaldriver.tn5250j.exceptions;

public class InvalidKeyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidKeyException(final String message){
		super(message);
	}
}
