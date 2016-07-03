package com.terminaldriver.common.logger;

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

	static final String NEW_LINE = System.getProperty("line.separator");

	public static String getHTML(final Screen5250 screen) {
		final StringBuilder sb = new StringBuilder();
		final RowReader rowReader = new RowReader(screen);
		String row;
		sb.append("<div class=\"console\">");
		while ((row = rowReader.readRow()) != null) {
			sb.append("<span class=\"consoleline\">");
			sb.append(row);
			sb.append("</span>").append(NEW_LINE);
		}
		sb.append("</div>").append(NEW_LINE);
		return sb.toString();
	}

	public static class RowReader {
		Screen5250 screen;
		int cols;
		int pos = 0;
		String screenChars;
		String attributes;

		public RowReader(final Screen5250 screen) {
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
				ScreenAttribute currentAttrEnum = ScreenAttribute.GRN;
				sb.append("<pre>");
				sb.append("<span class=\"greenText\">");
				for (int i = 0; i < cols; i++) {
					if (currentAttr != rowAttr.charAt(i)) {
						currentAttr = rowAttr.charAt(i);
						currentAttrEnum = ScreenAttribute.getAttrEnum(currentAttr);
						sb.append("</span>").append("<span");
						sb.append(" class=\"").append(doClass(currentAttrEnum)).append("\"");
						sb.append(">");
					}
					if (currentAttrEnum.isNonDisplay()) {
						sb.append(" ");
					} else {
						sb.append(StringEscapeUtils.escapeHtml(String.valueOf(row.charAt(i))));
					}
				}
				sb.append("</span>");
				sb.append("</pre>");
				return sb.toString();
			}
			return null;
		}

		String doClass(final ScreenAttribute attr) {
			final StringBuilder sb = new StringBuilder();
			sb.append(attr.getColor()).append("Text");
			if (attr.isUnderLine()) {
				sb.append(" underline");
			}
			return sb.toString();
		}
	}
}
