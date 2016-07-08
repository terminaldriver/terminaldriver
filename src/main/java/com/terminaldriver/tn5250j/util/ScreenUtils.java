package com.terminaldriver.tn5250j.util;

import static com.terminaldriver.tn5250j.util.Find.findMatches;

import java.util.NoSuchElementException;

import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.IdentifyBy;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;

public class ScreenUtils {

	public static FindBy checkScreen(final Class<?> clazz, final TerminalDriver driver) {
		final Screen5250 screen = driver.getSession().getScreen();
		final IdentifyBy info = clazz.getAnnotation(IdentifyBy.class);
		if (info == null) {
			return null;
		}

		int currentPosition = 0;
		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		for (final FindBy findBy : info.value()) {
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

	public static int pos2row(final int pos, final int cols) {
		return Math.max(1, pos / cols + 1);
	}

	public static int pos2col(final int pos, final int cols) {
		return Math.max(1, pos % cols);
	}
}
