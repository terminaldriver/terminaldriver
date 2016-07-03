package com.terminaldriver.tn5250j.util;

public class Wait {

	public static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
		}
	}
}
