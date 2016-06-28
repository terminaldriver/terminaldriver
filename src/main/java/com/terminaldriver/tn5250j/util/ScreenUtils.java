package com.terminaldriver.tn5250j.util;

import static com.terminaldriver.tn5250j.util.Find.findMatches;

import java.util.NoSuchElementException;

import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.IdentifyBy;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.obj.TerminalDriver;

public class ScreenUtils {

	public static boolean verifyScreen(final Object page, final TerminalDriver driver) {
		return verifyScreen(page.getClass(), driver);
	}

	public static boolean verifyScreen(final Class<?> clazz, final TerminalDriver driver) {
		FindBy result = checkScreen(clazz, driver);
		return result == null;
	}

	public static void assertScreen(final Object page, final TerminalDriver driver) {
		assertScreen(page.getClass(), driver);
	}

	public static void assertScreen(final Class<?> clazz, final TerminalDriver driver) {
		FindBy result = checkScreen(clazz, driver);
		if (result != null) {
			throw new NoSuchElementException("Page does not match: " + Find.toString(result));
		}
	}

	private static FindBy checkScreen(final Class<?> clazz, final TerminalDriver driver) {
		final Screen5250 screen = driver.getSession().getScreen();
		final IdentifyBy info = clazz.getAnnotation(IdentifyBy.class);
		if (info == null) {
			return null;
		}

		int currentPosition = 0;
		ScreenFieldReader reader = new ScreenFieldReader(screen);
		for (FindBy findBy : info.value()) {
			if (findBy.row() > 0 && pos2row(currentPosition, screen.getColumns()) != findBy.row()) {
				currentPosition = (findBy.row() - 1) * screen.getColumns();
			}
			if (findBy.row() > 0 && findBy.column() > 0) {
				currentPosition = (findBy.row() - 1) * screen.getColumns() + findBy.column();
			}
			reader.seek(currentPosition);

			ScreenTextBlock field = null;
			boolean found = false;
			while ((field = reader.readField()) != null) {
				if (findMatches(findBy, field)) {
					found = true;
					currentPosition = field.endPos();
					break;
				}
			}
			if (!found) {
				return findBy;
			}
		}
		return null;
	}

	public static int pos2row(int pos, int cols) {
		return Math.max(1, pos / cols + 1);
	}

	public static int pos2col(int pos, int cols) {
		return Math.max(1, pos % cols);
	}
}
