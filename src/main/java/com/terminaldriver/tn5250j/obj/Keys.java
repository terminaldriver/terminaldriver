package com.terminaldriver.tn5250j.obj;

public class Keys{
	
	TerminalDriver driver;
	
	public Keys(TerminalDriver driver) {
		super();
		this.driver = driver;
	}

	public Keys press(Key key){
		driver.getSession().getScreen().sendKeys(key.toString());
		return this;
	}
	
	public Keys enter(){
		press(Key.ENTER);
		return this;
	}
	public Keys fieldExit(){
		press(Key.FIELD_EXIT);
		return this;
	}
	public Keys f3(){
		command(3);
		return this;
	}
	
	public Keys command(int keyCommand){
		if(keyCommand < 1 || keyCommand > 24){
			throw new RuntimeException("Command key " + keyCommand + " invalid.");
		}
		driver.getSession().getScreen().sendKeys("[PF" + String.valueOf(keyCommand) + "]");
		return this;
	}
}