package com.terminaldriver.tn5250j.exceptions;

public class ObjectInitException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectInitException(Exception e){
		super(e);
	}

}
