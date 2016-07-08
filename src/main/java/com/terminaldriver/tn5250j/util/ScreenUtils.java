package com.terminaldriver.tn5250j.util;

import static com.terminaldriver.tn5250j.util.Find.findMatches;

import java.util.Arrays;
import java.util.List;

import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.IdentifyBy;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;

public class ScreenUtils {

	/**
	 * Verifies that all the @FindBy annotations on the give page object are
	 * present on the current screen.
	 *
	 * @param page
	 * @param driver
	 * @return
	 */
	public static boolean verifyScreen(final Object page, final TerminalDriver driver) {
		return verifyScreen(page.getClass(), driver);
	}

	/**
	 * Verifies that all the @FindBy annotations on the give page object class
	 * are present on the current screen.
	 *
	 * @param clazz
	 * @param driver
	 * @return
	 */
	public static boolean verifyScreen(final Class<?> clazz, final TerminalDriver driver) {
		final FindBy result = ScreenUtils.checkScreen(clazz, driver);
		return result == null;
	}

	/**
	 * Checks that all the @FindBy annotations that identify the give page
	 * object class are present on the current screen.
	 *
	 * @param clazz
	 * @param driver
	 * @return FindBy that does not match if any.
	 */
	public static FindBy checkScreen(final Class<?> clazz, final TerminalDriver driver) {
		final IdentifyBy info = clazz.getAnnotation(IdentifyBy.class);
		if (info == null) {
			return null;
		}

		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		for (final FindBy findBy : info.value()) {
			if (checkFindBy(driver, reader, findBy) == null) {
				return findBy;
			}
		}
		return null;
	}

	/**
	 * Verifies that the given @FindBy is found on the screen
	 *
	 * @param findBy
	 * @param driver
	 * @return
	 */
	public static boolean checkFindBy(final FindBy findBy, final TerminalDriver driver) {
		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		return checkFindBy(driver, reader, findBy) != null;
	}

	private static ScreenElement checkFindBy(final TerminalDriver driver, final ScreenFieldReader reader,
			final FindBy findBy) {
		final Screen5250 screen = driver.getSession().getScreen();
		int currentPosition = reader.getCurrentPosition();
		if (findBy.row() > 0 && pos2row(currentPosition, screen.getColumns()) != findBy.row()) {
			currentPosition = (findBy.row() - 1) * screen.getColumns();
		}
		if (findBy.row() > 0 && findBy.column() > 0) {
			currentPosition = (findBy.row() - 1) * screen.getColumns() + findBy.column();
		}
		reader.seek(currentPosition);

		// Look for fields first
		final ScreenField screenField = applyFindScreenField(driver, findBy, currentPosition);
		if (screenField != null) {
			reader.seek(screenField.endPos() + 1);
			return screenField;
		}
		// Look for text
		return applyFindScreenTextBlock(driver, findBy, reader, currentPosition);
	}

	public static ScreenElement applyFind(final Class<?> targetClazz, final TerminalDriver driver, final FindBy info,
			final int currentPosition) {
		if (targetClazz.equals(ScreenField.class)) {
			return applyFindScreenField(driver, info, currentPosition);
		} else {
			return applyFindScreenTextBlock(driver, info, new ScreenFieldReader(driver), currentPosition);
		}
	}

	public static ScreenField applyFindScreenField(final TerminalDriver driver, final FindBy info,
			final int currentPositionParm) {
		final List<org.tn5250j.framework.tn5250.ScreenField> screenFields = Arrays
				.asList(driver.getSession().getScreen().getScreenFields().getFields());

		int currentIndex = 0;
		while (currentIndex < screenFields.size()) {
			// Forward to where current index is add
			if (screenFields.get(currentIndex).endPos() > currentPositionParm) {
				final ScreenField thisScreen = new ScreenField(driver, screenFields.get(currentIndex++));
				// Make sure it matches
				if (findMatches(info, thisScreen)) {
					return thisScreen;
				}
			}
			currentIndex++;
		}
		return null;
	}

	public static ScreenTextBlock applyFindScreenTextBlock(final TerminalDriver driver, final FindBy info,
			final ScreenFieldReader reader, final int currentPositionParm) {
		final Screen5250 screen = driver.getSession().getScreen();
		int currentPosition = currentPositionParm;
		if (info.row() > 0 && pos2row(currentPosition, screen.getColumns()) != info.row()) {
			currentPosition = (info.row() - 1) * screen.getColumns();
		}

		if (info.row() > 0 && info.column() > 0) {
			currentPosition = (info.row() - 1) * screen.getColumns() + info.column();
			if (info.length() > 0) {
				// Read a specific screen block
				final ScreenTextBlock field = reader.read(info.row(), info.column(), info.length());
				if (findMatches(info, field)) {
					return field;
				}
				return null;
			}
		}
		reader.seek(currentPosition);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (findMatches(info, field)) {
				return field;
			}
		}
		return null;
	}

	public static int pos2row(final int pos, final int cols) {
		return Math.max(1, pos / cols + 1);
	}

	public static int pos2col(final int pos, final int cols) {
		return Math.max(1, pos % cols);
	}

	public static int rowcol2pos(final int row, final int col, final int colsPerRow) {
		return ((row - 1) * colsPerRow) + col - 1;
	}
}
