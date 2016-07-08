package com.terminaldriver.tn5250j.exceptions;

import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;

import lombok.Getter;

public class FieldNotEditableException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	final ScreenElement screenElement;

	public FieldNotEditableException(final ScreenField field) {
		super(String.format("Field %s [%s] is not editable",field.getFieldId(),field.getString()));
		this.screenElement = field;
	}
	public FieldNotEditableException(final ScreenTextBlock field) {
		super(String.format("Text block %sx%s [%s] is not editable",field.startRow(),field.startCol(),field.getString()));
		this.screenElement = field;
	}
	
}
