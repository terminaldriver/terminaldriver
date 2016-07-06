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
				currentScreenField = applyTableAnnotation(driver, page, info, field, screenFields, currentScreenField);
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

	static ScreenElement applyTableAnnotation(final TerminalDriver driver, final Object page, final Table info,
			final Field field, final List<org.tn5250j.framework.tn5250.ScreenField> screenFields,
			ScreenElement currentScreenField) {
		
			if (currentScreenField == null || info.row() > currentScreenField.startRow()){
				if(info.row()>0){
					for(org.tn5250j.framework.tn5250.ScreenField screenField : screenFields){
						if(screenField.startRow() == info.row()){
							currentScreenField = new ScreenTextBlock(driver, " ", info.row()-2, 80, 1, " ");
						}
					}
				}
			}
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			if (field.get(page) == null) {
				field.set(page, new ArrayList<Object>());
			}
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>)field.get(page);
			Class<?> newType = info.type();
			Object tableObject = newType.newInstance();
			boolean foundAllTable = true;
			ScreenElement saveCurrentScreenField = currentScreenField;
			while(foundAllTable == true){
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
							foundAllTable = false;
							currentScreenField = saveCurrentScreenField;
						}
					}
				}
				if(foundAllTable){
					list.add(tableObject);
					tableObject = newType.newInstance();
					saveCurrentScreenField = currentScreenField;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return currentScreenField;
	}

	static ScreenElement applyFind(final Class<?> targetClazz, final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		if (targetClazz.equals(ScreenField.class)) {
			return applyFindScreenField(driver, info, screenFields, currentScreenField);
		} else {
			return applyFindScreenTextBlock(driver, info, screenFields, currentScreenField);
		}
	}

	static ScreenField applyFindScreenField(final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		int currentPosition = 0;
		if (currentScreenField instanceof ScreenField) {
			currentPosition = screenFields.indexOf(((ScreenField) currentScreenField).getUnderlyingScreenField()) + 1;
		} else if (currentScreenField instanceof ScreenTextBlock) {
			for (final org.tn5250j.framework.tn5250.ScreenField field : screenFields) {
				if (field.startRow() == currentScreenField.startRow()
						&& field.startCol() == currentScreenField.startCol()) {
					break;
				}
				currentPosition = screenFields.indexOf(field);
				if (field.startRow() > currentScreenField.startRow()) {
					break;
				}
				if (field.startRow() == currentScreenField.startRow()
						&& field.startCol() > currentScreenField.startCol()) {
					break;
				}
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

	static ScreenTextBlock applyFindScreenTextBlock(final TerminalDriver driver, final FindBy info,
			final List<org.tn5250j.framework.tn5250.ScreenField> screenFields, final ScreenElement currentScreenField) {
		final Screen5250 screen = driver.getSession().getScreen();
		int currentPosition = 0;
		if (currentScreenField != null) {
			currentPosition = currentScreenField.endPos()+1;
		}
		if (info.row() > 0 && pos2row(currentPosition, screen.getColumns()) != info.row()) {
			currentPosition = (info.row() - 1) * screen.getColumns();
		}
		final ScreenFieldReader reader = new ScreenFieldReader(driver);
		if (info.row() > 0 && info.column() > 0) {
			currentPosition = (info.row() - 1) * screen.getColumns() + info.column();
			if(info.length() > 0){
				//Read a specific screen block
				ScreenTextBlock field = reader.read(info.row(), info.column(), info.length());
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

}
