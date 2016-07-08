package com.terminaldriver.tn5250j.obj;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.exceptions.FieldNotEditableException;

public class ScreenField extends ScreenElement {

	final org.tn5250j.framework.tn5250.ScreenField screenField;
	final TerminalDriver driver;

	public ScreenField(final TerminalDriver driver, final org.tn5250j.framework.tn5250.ScreenField screenField) {
		super();
		this.screenField = screenField;
		this.driver = driver;
	}

	@Override
	public int startRow() {
		return screenField.startRow();
	}

	public org.tn5250j.framework.tn5250.ScreenField getUnderlyingScreenField() {
		return screenField;
	}

	@Override
	public int startCol() {
		return screenField.startCol();
	}

	@Override
	public int getLength() {
		return screenField.getLength();
	}

	@Override
	public int startPos() {
		return screenField.startPos();
	}

	@Override
	public int endPos() {
		return screenField.endPos();
	}

	public int getFieldId() {
		return screenField.getFieldId();
	}

	@Override
	public String getAttr() {
		return Character.valueOf((char) (screenField.getAttr())).toString();
	}

	@Override
	public String getString() {
		return screenField.getString();
	}

	public void setString(final String value) {
		if(!isEditable()){
			throw new FieldNotEditableException(this);
		}
		driver.fireFieldSetString(this, value);
		screenField.setString(value);
	}
	
	public String toString(){
		return screenField.getString();
	}
	
	public boolean isEditable(){
		return !screenField.isBypassField();
	}
}
