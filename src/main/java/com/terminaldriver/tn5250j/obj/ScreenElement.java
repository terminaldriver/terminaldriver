package com.terminaldriver.tn5250j.obj;

public abstract class ScreenElement {
	public abstract String getString();

	public abstract int startRow();

	public abstract int startCol();

	public abstract int getLength();

	public abstract String getAttr();

	public abstract int startPos();

	public abstract int endPos();
	
	public abstract void setString(final String value);
	
	public abstract boolean isEditable();
}
