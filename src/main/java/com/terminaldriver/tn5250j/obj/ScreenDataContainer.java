package com.terminaldriver.tn5250j.obj;

import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;

public class ScreenDataContainer implements TN5250jConstants {

	public final char[] text;
	public final char[] attr;
	public final char[] isAttr;
	public final char[] color;
	public final char[] extended;
	public final char[] graphic;
	public final char[] field;
	
	

	public ScreenDataContainer(char[] text, char[] attr, char[] isAttr, char[] color, char[] extended, char[] graphic,
			char[] field) {
		super();
		this.text = text;
		this.attr = attr;
		this.isAttr = isAttr;
		this.color = color;
		this.extended = extended;
		this.graphic = graphic;
		this.field = field;
	}

	public ScreenDataContainer(final Screen5250 screen) {

		final int startRow = 1;
		final int startCol = 1;
		final int endRow = screen.getRows();
		final int endCol = screen.getColumns();
		final int size = ((endCol - startCol) + 1) * ((endRow - startRow) + 1);

		text = new char[size];
		attr = new char[size];
		isAttr = new char[size];
		color = new char[size];
		extended = new char[size];
		graphic = new char[size];
		field = new char[size];

		if (size == screen.getScreenLength()) {
			screen.getScreen(text, size, PLANE_TEXT);
			screen.getScreen(attr, size, PLANE_ATTR);
			screen.getScreen(isAttr, size, PLANE_IS_ATTR_PLACE);
			screen.getScreen(color, size, PLANE_COLOR);
			screen.getScreen(extended, size, PLANE_EXTENDED);
			screen.getScreen(graphic, size, PLANE_EXTENDED_GRAPHIC);
			screen.getScreen(field, size, PLANE_FIELD);
		} else {
			screen.getScreenRect(text, size, startRow, startCol, endRow, endCol, PLANE_TEXT);
			screen.getScreenRect(attr, size, startRow, startCol, endRow, endCol, PLANE_ATTR);
			screen.getScreenRect(isAttr, size, startRow, startCol, endRow, endCol, PLANE_IS_ATTR_PLACE);
			screen.getScreenRect(color, size, startRow, startCol, endRow, endCol, PLANE_COLOR);
			screen.getScreenRect(extended, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED);
			screen.getScreenRect(graphic, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED_GRAPHIC);
			screen.getScreenRect(field, size, startRow, startCol, endRow, endCol, PLANE_FIELD);
		}
	}

}