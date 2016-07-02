package com.terminaldriver.tn5250j.util;

import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenDataContainer;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;

public class ScreenFieldReader implements TN5250jConstants {
	final ScreenDataContainer sc;
	int cols;
	int rows;
	int currentRow = 1;
	int currentCol = 1;
	final TerminalDriver driver;

	public ScreenFieldReader(TerminalDriver driver) {
		Screen5250 screen = driver.getSession().getScreen();
		sc = new ScreenDataContainer(screen);
		rows = screen.getRows();
		cols = screen.getColumns();
		this.driver = driver;
	}

	public void seek(int pos) {
		currentRow = pos2row(pos);
		currentCol = pos2col(pos);
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
			if (sc.isAttr[bufferChar(currentRow, currentCol)] == 1) {
				advance();
			}
			int startBuffer = bufferChar(currentRow, currentCol);
			while (advance() && sc.isAttr[bufferChar(currentRow, currentCol)] != 1) {
			}
			int endBuffer = bufferChar(currentRow, currentCol);
			if (endBuffer == startBuffer) {
				return null;
			}
			String content = new String(sc.text).substring(startBuffer, endBuffer);
			ScreenTextBlock retval = new ScreenTextBlock(driver,content, pos2row(startBuffer), pos2col(startBuffer),
					endBuffer - startBuffer, Character.valueOf(sc.attr[startBuffer]).toString());
			return retval;
		} catch (ArrayIndexOutOfBoundsException e) {
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

	private int bufferChar(int row, int col) {
		return ((row - 1) * cols) + col - 1;
	}

	private int pos2row(int pos) {
		return Math.max(1, pos / cols + 1);
	}

	private int pos2col(int pos) {
		return Math.max(1, pos % cols);
	}

}