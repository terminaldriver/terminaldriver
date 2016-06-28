package com.terminaldriver.tn5250j.obj;

public class ScreenField extends ScreenElement {

	final org.tn5250j.framework.tn5250.ScreenField screenField;

	public ScreenField(org.tn5250j.framework.tn5250.ScreenField screenField) {
		super();
		this.screenField = screenField;
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

	public void setString(String value) {
		screenField.setString(value);
	}
}
