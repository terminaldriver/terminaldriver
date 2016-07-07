package com.terminaldriver.tn5250j.obj;

import lombok.Getter;

public class FieldNotEditableException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	final ScreenField screenField;

	public FieldNotEditableException(final ScreenField field) {
		super(String.format("Field %s [%s] is not editable",field.getFieldId(),field.getString()));
		this.screenField = field;
	}
	
}
