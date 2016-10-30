package com.terminaldriver.tn5250j.obj;

import static com.terminaldriver.tn5250j.Assert.assertScreen;
import static com.terminaldriver.tn5250j.util.ScreenUtils.applyFind;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.Table;
import com.terminaldriver.tn5250j.exceptions.ObjectInitException;

public class ScreenObjectFactory {

	public static <E> E createPage(final Class<E> clazz, final TerminalDriver driver) {
		E object = null;
		try {
			object = clazz.newInstance();
			initElements(object, driver);
		} catch (final Exception e) {
			throw new ObjectInitException(e);
		}
		return object;
	}

	public static boolean initElements(final Object page, final TerminalDriver driver) {
		final Class<?> clazz = page.getClass();
		assertScreen(clazz, driver);
		final Field[] fields = clazz.getDeclaredFields();
		boolean foundAll = true;
		ScreenElement currentScreenField = null;
		for (final Field field : fields) {

			if (field.isAnnotationPresent(FindBy.class)) {
				final FindBy info = field.getAnnotation(FindBy.class);
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					final ScreenElement newScreenField = applyFind(field.getType(), driver, info,
							currentScreenField == null? 0:currentScreenField.endPos() + 1);
					if (newScreenField != null) {
						field.set(page, newScreenField);
						currentScreenField = newScreenField;
					} else {
						foundAll = false;
					}
				} catch (final Exception e) {
					throw new ObjectInitException(e);
				}
				continue;
			}

			if (field.isAnnotationPresent(Table.class)) {
				final Table info = field.getAnnotation(Table.class);
				try {
					currentScreenField = applyTableAnnotation(driver, page, info, field, currentScreenField);
				} catch (final Exception e) {
					throw new ObjectInitException(e);
				}
				continue;
			}

			// Set the driver if present.
			if (field.getType().equals(TerminalDriver.class)) {
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					if (field.get(page) == null) {
						field.set(page, driver);
					}
				} catch (final IllegalArgumentException e) {
					throw new ObjectInitException(e);
				} catch (final IllegalAccessException e) {
					throw new ObjectInitException(e);
				}
				continue;
			}
		}
		return foundAll;
	}

	static ScreenElement applyTableAnnotation(final TerminalDriver driver, final Object page, final Table info,
			final Field field, final ScreenElement currentScreenFieldParm)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		final List<org.tn5250j.framework.tn5250.ScreenField> screenFields = Arrays
				.asList(driver.getSession().getScreen().getScreenFields().getFields());

		ScreenElement currentScreenField = currentScreenFieldParm;

		if (info.row() > 0 && (currentScreenField == null || info.row() > currentScreenField.startRow())) {
			for (final org.tn5250j.framework.tn5250.ScreenField screenField : screenFields) {
				if (screenField.getStartRow() == info.row()) {
					currentScreenField = new ScreenTextBlock(driver, " ", info.row() - 2, 80, 1, " ");
				}
			}
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		if (field.get(page) == null) {
			field.set(page, new ArrayList<Object>());
		}
		@SuppressWarnings("unchecked")
		final List<Object> list = (List<Object>) field.get(page);
		final Class<?> newType = info.type();
		Object tableObject = newType.newInstance();
		boolean foundAllTable = true;
		ScreenElement saveCurrentScreenField = currentScreenField;
		while (foundAllTable == true) {
			for (final Field newfield : newType.getDeclaredFields()) {
				if (newfield.isAnnotationPresent(FindBy.class)) {
					if (!newfield.isAccessible()) {
						newfield.setAccessible(true);
					}
					final FindBy findInfo = newfield.getAnnotation(FindBy.class);
					final ScreenElement newScreenField = applyFind(newfield.getType(), driver, findInfo,
							currentScreenField == null? 0: currentScreenField.endPos() + 1);
					if (newScreenField == null) {
						foundAllTable = false;
						currentScreenField = saveCurrentScreenField;
					} else {
						newfield.set(tableObject, newScreenField);
						currentScreenField = newScreenField;
					}
				}
			}
			if (foundAllTable) {
				list.add(tableObject);
				tableObject = newType.newInstance();
				saveCurrentScreenField = currentScreenField;
			}
		}
		return currentScreenField;
	}

}
