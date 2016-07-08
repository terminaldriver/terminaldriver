package com.terminaldriver.tn5250j.util;

import static com.terminaldriver.tn5250j.util.ScreenUtils.pos2col;
import static com.terminaldriver.tn5250j.util.ScreenUtils.pos2row;
import static com.terminaldriver.tn5250j.util.ScreenUtils.rowcol2pos;

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
		currentRow = pos2row(pos, cols);
		currentCol = pos2col(pos, cols);
	}

	public ScreenTextBlock read(final int row, final int col, final int length) {
		final int offset = rowcol2pos(row, col, cols);
		seek(offset + length);
		final String value = new String(screenContainer.text, offset, length);
		return new ScreenTextBlock(driver, value, row, col, length, String.valueOf(screenContainer.attr[offset]));
	}

	public ScreenTextBlock readField(final int row, final int col) {
		final int offset = rowcol2pos(row, col, cols);
		seek(offset);
		ScreenTextBlock field = readField();
		if (field.startCol() < col) {
			final String value = new String(screenContainer.text, offset,
					field.getLength() - (col - field.startCol()) + 1);
			field = new ScreenTextBlock(driver, value, row, col, value.length(),
					String.valueOf(screenContainer.attr[offset]));
		}
		return field;
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
			if (screenContainer.isAttr[getCurrentPosition()] == 1) {
				advance();
			}
			final int startBuffer = getCurrentPosition();
			while (advance() && screenContainer.isAttr[getCurrentPosition()] != 1) {
			}
			final int endBuffer = getCurrentPosition();
			if (endBuffer == startBuffer) {
				return null;
			}
			final String content = new String(screenContainer.text).substring(startBuffer, endBuffer);
			final ScreenTextBlock retval = new ScreenTextBlock(driver, content, pos2row(startBuffer, cols),
					pos2col(startBuffer, cols), endBuffer - startBuffer,
					Character.valueOf(screenContainer.attr[startBuffer]).toString());
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

	public int getCurrentPosition() {
		return rowcol2pos(currentRow, currentCol, cols);
	}

}