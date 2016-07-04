package com.terminaldriver.tn5250j.obj;

import static com.terminaldriver.tn5250j.util.Find.findMatches;
import static com.terminaldriver.tn5250j.util.ScreenUtils.assertScreen;
import static com.terminaldriver.tn5250j.util.ScreenUtils.pos2row;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.Table;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;

public class ScreenObjectFactory {

	public static <E> E createPage(final Class<E> clazz, final TerminalDriver driver) {
		E object = null;
		try {
			object = clazz.newInstance();
			initElements(object, driver);
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static boolean initElements(final Object page, final TerminalDriver driver) {
		final Screen5250 screen = driver.getSession().getScreen();
		final Class<?> clazz = page.getClass();
		assertScreen(clazz, driver);
		final Field[] fields = clazz.getDeclaredFields();
		boolean foundAll = true;
		final List<org.tn5250j.framework.tn5250.ScreenField> screenFields = Arrays
				.asList(screen.getScreenFields().getFields());
		ScreenElement currentScreenField = null;
		for (final Field field : fields) {

			if (field.isAnnotationPresent(FindBy.class)) {
				final FindBy info = field.getAnnotation(FindBy.class);
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					final ScreenElement newScreenField = applyFind(field.getType(), driver, info, screenFields,
							currentScreenField);
					if (newScreenField != null) {
						field.set(page, newScreenField);
						currentScreenField = newScreenField;
					} else {
						foundAll = false;
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				continue;
			}

			if (field.isAnnotationPresent(Table.class)) {
				final Table info = field.getAnnotation(Table.class);
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					if (field.get(page) == null) {
						field.set(page, new ArrayList());
					}
					List list = (List)field.get(page);
					Class<?> newType = info.type();
					Object tableObject = newType.newInstance();
					foundAll = true;
					while(foundAll == true){
						for (final Field newfield : newType.getDeclaredFields()) {
							if (newfield.isAnnotationPresent(FindBy.class)) {
								if (!newfield.isAccessible()) {
									newfield.setAccessible(true);
								}
								final FindBy findInfo = newfield.getAnnotation(FindBy.class);
								final ScreenElement newScreenField = applyFind(newfield.getType(), driver, findInfo, screenFields,
										currentScreenField);
								if (newScreenField != null) {
									newfield.set(tableObject, newScreenField);
									currentScreenField = newScreenField;
								} else {
									foundAll = false;
								}
							}
						}
						if(foundAll){
							list.add(tableObject);
							tableObject = newType.newInstance();
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				continue;
			}

			// Set the driver if present.
			if(field.getType().equals(TerminalDriver.class) ){
				try {
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
						if (field.get(page) == null) {
							field.set(page, driver);
						}
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
				continue;
			}
		}
		return foundAll;
	}

	private static ScreenElement applyFind(final Class<?> targetClazz, final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		if (targetClazz.equals(ScreenField.class)) {
			return applyFindScreenField(driver, info, screenFields, currentScreenField);
		} else {
			return applyFindScreenTextBlock(driver, info, screenFields, currentScreenField);
		}
	}

	private static ScreenField applyFindScreenField(final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		int currentPosition = 0;
		if (currentScreenField instanceof ScreenField) {
			currentPosition = screenFields.indexOf(((ScreenField) currentScreenField).getUnderlyingScreenField()) + 1;
		} else if (currentScreenField instanceof ScreenTextBlock) {
			for (final org.tn5250j.framework.tn5250.ScreenField field : screenFields) {
				if (field.startRow() > currentScreenField.startRow()) {
					break;
				}
				if (field.startRow() == currentScreenField.startRow()
						&& field.startCol() > currentScreenField.startCol()) {
					break;
				}
				currentPosition = screenFields.indexOf(field);
			}
		}
		while (currentPosition < screenFields.size()) {
			final ScreenField thisScreen = new ScreenField(driver, screenFields.get(currentPosition++));
			if (findMatches(info, thisScreen)) {
				return thisScreen;
			}
		}
		return null;
	}

	private static ScreenTextBlock applyFindScreenTextBlock(final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		final Screen5250 screen = driver.getSession().getScreen();
		int currentPosition = 0;
		if (currentScreenField != null) {
			currentPosition = currentScreenField.endPos();
		}
		if (info.row() > 0 && pos2row(currentPosition, screen.getColumns()) != info.row()) {
			currentPosition = (info.row() - 1) * screen.getColumns();
		}
		if (info.row() > 0 && info.column() > 0) {
			currentPosition = (info.row() - 1) * screen.getColumns() + info.column();
		}
		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		reader.seek(currentPosition);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (findMatches(info, field)) {
				return field;
			}
		}
		return null;
	}

}
