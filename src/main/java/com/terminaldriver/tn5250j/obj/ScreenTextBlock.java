package com.terminaldriver.tn5250j.obj;

public class ScreenTextBlock extends ScreenElement {

	final String value;
	final int startRow;
	final int startColumn;
	final int length;
	final String attr;

	public ScreenTextBlock(String value, int startRow, int startColumn, int length, String attr) {
		super();
		this.value = value;
		this.startRow = startRow;
		this.startColumn = startColumn;
		this.length = length;
		this.attr = attr;
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
		return (startRow - 1) * 80 + startColumn;
	}

	@Override
	public int endPos() {
		return startPos() + length;
	}
}
