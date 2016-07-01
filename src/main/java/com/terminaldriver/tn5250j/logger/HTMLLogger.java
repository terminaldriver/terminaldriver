package com.terminaldriver.tn5250j.logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.annotation.ScreenAttribute;

/**
 * Utility class to log the session
 * 
 * @author eberlyrh
 *
 */
public class HTMLLogger {

	public static String getHTML(Screen5250 screen) {
		StringBuilder sb = new StringBuilder();
		RowReader rowReader = new RowReader(screen);
		String row;
		sb.append("<div>");
		sb.append("<pre>");
		while ((row = rowReader.readRow()) != null) {
			sb.append("<span class=\"console\">");
			sb.append(row);
			sb.append("<br>");
			sb.append("</span>");
		}
		sb.append("</pre>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class RowReader {
		Screen5250 screen;
		int cols;
		int pos = 0;
		String screenChars;
		String attributes;

		public RowReader(Screen5250 screen) {
			this.screen = screen;
			cols = screen.getColumns();
			screenChars = new String(screen.getScreenAsChars());
			final char attr[] = new char[screenChars.length()];
			screen.GetScreen(attr, attr.length, TN5250jConstants.PLANE_ATTR);
			attributes = new String(attr);
		}

		public String readRow() {
			if (pos + cols <= screenChars.length()) {
				final String row = screenChars.substring(pos, pos + cols);
				final String rowAttr = attributes.substring(pos, pos + cols);
				pos += cols;
				final StringBuilder sb = new StringBuilder();
				char currentAttr = ' ';
				
				sb.append("<span class=\"greenText\">");
				for (int i = 0; i < cols; i++) {
					if (currentAttr != rowAttr.charAt(i)) {
						currentAttr = rowAttr.charAt(i);
						sb.append("</span>").append("<span");
						for (ScreenAttribute attr : ScreenAttribute.values()) {
							if (attr.getCode() != null && currentAttr == attr.getCode().charAt(0)) {
								sb.append(" class=\"").append(doClass(attr)).append("\"");
								break;
							}
						}
						sb.append(">");
					}
					sb.append(StringEscapeUtils.escapeHtml(String.valueOf(row.charAt(i))));
				}
				sb.append("</span>");
				return sb.toString();
			}
			return null;
		}
		
		String doClass(ScreenAttribute attr){
			final StringBuilder sb = new StringBuilder();
			sb.append(attr.getColor()).append("Text");
			if (attr.isUnderLine()) {
				sb.append(" underline");
			}
			return sb.toString();
		}
	}
}
