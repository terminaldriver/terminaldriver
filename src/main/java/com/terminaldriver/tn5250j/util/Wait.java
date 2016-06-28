package com.terminaldriver.tn5250j.util;

public class Wait {

	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
}
