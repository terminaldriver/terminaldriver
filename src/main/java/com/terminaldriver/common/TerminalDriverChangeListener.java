package com.terminaldriver.common;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.obj.ScreenField;

public interface TerminalDriverChangeListener {

	public void fieldSetString(TerminalDriver driver, ScreenField screenField, String value);

	public void sendKeys(TerminalDriver driver, String keys2);

	public void screenSizeChanged(TerminalDriver driver, int cols, int rows);

	public void screenPartialsUpdate(TerminalDriver driver, int row1, int col1, int row2, int col2);

	public void screenChanged(TerminalDriver driver);
	
	public void note(String note);
}
