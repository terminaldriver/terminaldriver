package com.terminaldriver.tn5250j.obj;

import javax.activity.InvalidActivityException;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.exceptions.InvalidKeyException;

public class KeyStrokes {

	TerminalDriver driver;

	public KeyStrokes(final TerminalDriver driver) {
		super();
		this.driver = driver;
	}

	public KeyStrokes press(final Key key) {
		driver.sendKeys(key.toString());
		return this;
	}

	public KeyStrokes enter() {
		press(Key.ENTER);
		return this;
	}

	public KeyStrokes fieldExit() {
		press(Key.FIELD_EXIT);
		return this;
	}

	public KeyStrokes f3() {
		command(3);
		return this;
	}

	public KeyStrokes command(final int keyCommand) {
		if (keyCommand < 1 || keyCommand > 24) {
			throw new InvalidKeyException("Command key " + keyCommand + " invalid.");
		}
		driver.getSession().getScreen().sendKeys("[PF" + String.valueOf(keyCommand) + "]");
		return this;
	}
}