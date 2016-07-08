package com.terminaldriver.tn5250j;

import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.obj.By;
import com.terminaldriver.tn5250j.util.Find;
import com.terminaldriver.tn5250j.util.ScreenUtils;

public class Assert {

	public static void assertScreen(final Object page, final TerminalDriver driver) {
		assertScreen(page.getClass(), driver);
	}

	public static void assertScreen(final Class<?> clazz, final TerminalDriver driver) {
		final FindBy result = ScreenUtils.checkScreen(clazz, driver);
		if (result != null) {
			throw new AssertionError("Page does not match: " + Find.toString(result));
		}
	}

	public static void assertBy(final By by, final TerminalDriver driver) {
		if (driver.findElement(by) == null) {
			throw new AssertionError("By not found: " + by);
		}
	}

}
