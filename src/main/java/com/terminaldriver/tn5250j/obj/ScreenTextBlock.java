package com.terminaldriver.tn5250j.obj;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.exceptions.FieldNotEditableException;

import lombok.Getter;

public class ScreenTextBlock extends ScreenElement {

	final String value;
	final int startRow;
	final int startColumn;
	final int length;
	final String attr;
	@Getter
	final TerminalDriver driver;

	public ScreenTextBlock(final TerminalDriver driver, final String value, final int startRow, final int startColumn,
			final int length, final String attr) {
		super();
		this.value = value;
		this.startRow = startRow;
		this.startColumn = startColumn;
		this.length = length;
		this.attr = attr;
		this.driver = driver;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public int startRow() {
		return startRow;
	}

	@Override
	public int startCol() {
		return startColumn;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public String getAttr() {
		return attr;
	}

	@Override
	public int startPos() {
		return (startRow - 1) * driver.getScreenColumns() + startColumn;
	}

	@Override
	public int endPos() {
		return startPos() + length;
	}

	@Override
	public void setString(final String value) {
		throw new FieldNotEditableException(this);
	}

	@Override
	public boolean isEditable() {
		return false;
	}
}
