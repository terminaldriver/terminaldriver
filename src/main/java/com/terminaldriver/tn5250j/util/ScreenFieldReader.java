package com.terminaldriver.tn5250j.util;

import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenDataContainer;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;

public class ScreenFieldReader implements TN5250jConstants {
	final ScreenDataContainer screenContainer;
	int cols;
	int rows;
	int currentRow = 1;
	int currentCol = 1;
	final TerminalDriver driver;

	public ScreenFieldReader(final TerminalDriver driver) {
		final Screen5250 screen = driver.getSession().getScreen();
		screenContainer = new ScreenDataContainer(screen);
		rows = screen.getRows();
		cols = screen.getColumns();
		this.driver = driver;
	}

	public void seek(final int pos) {
		currentRow = pos2row(pos);
		currentCol = pos2col(pos);
	}
	
	public ScreenTextBlock read(final int row, final int col, final int length){
		final int offset = bufferChar(row,col);
		seek(offset + length);
		String value = new String(screenContainer.text,offset,length);
		return new ScreenTextBlock(driver,value,row,col,length,String.valueOf(screenContainer.attr[offset]));
	}

	public ScreenTextBlock readNotEmptyField() {
		ScreenTextBlock result = readField();
		while (result != null && result.getString().trim().isEmpty()) {
			result = readField();
		}
		return result;
	}

	public ScreenTextBlock readField() {
		try {
			if (screenContainer.isAttr[bufferChar(currentRow, currentCol)] == 1) {
				advance();
			}
			final int startBuffer = bufferChar(currentRow, currentCol);
			while (advance() && screenContainer.isAttr[bufferChar(currentRow, currentCol)] != 1) {
			}
			final int endBuffer = bufferChar(currentRow, currentCol);
			if (endBuffer == startBuffer) {
				return null;
			}
			final String content = new String(screenContainer.text).substring(startBuffer, endBuffer);
			final ScreenTextBlock retval = new ScreenTextBlock(driver, content, pos2row(startBuffer),
					pos2col(startBuffer), endBuffer - startBuffer, Character.valueOf(screenContainer.attr[startBuffer]).toString());
			return retval;
		} catch (final ArrayIndexOutOfBoundsException e) {
			if (currentRow > rows) {
				return null;
			}
			throw e;
		}
	}

	private boolean advance() {
		currentCol++;
		if (currentCol > cols) {
			currentCol = 1;
			currentRow++;
			return false;
		}
		return true;
	}

	private int bufferChar(final int row, final int col) {
		return ((row - 1) * cols) + col - 1;
	}

	private int pos2row(final int pos) {
		return Math.max(1, pos / cols + 1);
	}

	private int pos2col(final int pos) {
		return Math.max(1, pos % cols);
	}

}