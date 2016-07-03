package com.terminaldriver.tn5250j.obj;

import com.terminaldriver.tn5250j.TerminalDriver;

public class Keys {

	TerminalDriver driver;

	public Keys(final TerminalDriver driver) {
		super();
		this.driver = driver;
	}

	public Keys press(final Key key) {
		driver.sendKeys(key.toString());
		return this;
	}

	public Keys enter() {
		press(Key.ENTER);
		return this;
	}

	public Keys fieldExit() {
		press(Key.FIELD_EXIT);
		return this;
	}

	public Keys f3() {
		command(3);
		return this;
	}

	public Keys command(final int keyCommand) {
		if (keyCommand < 1 || keyCommand > 24) {
			throw new RuntimeException("Command key " + keyCommand + " invalid.");
		}
		driver.getSession().getScreen().sendKeys("[PF" + String.valueOf(keyCommand) + "]");
		return this;
	}
}