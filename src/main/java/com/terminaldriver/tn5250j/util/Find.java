package com.terminaldriver.tn5250j.util;

import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.How;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.ScreenElement;

public class Find {

	public static boolean findMatches(final FindBy info, final ScreenElement element) {
		if (info.row() != 0 && element.startRow() != info.row()) {
			return false;
		}
		if (info.column() != 0 && element.startCol() != info.column()) {
			if (element.startCol() > info.column() || element.startCol() + element.getLength() <= info.column()) {
				return false;
			}
		}
		if (info.length() != 0 && element.getLength() != info.length()) {
			return false;
		}
		if (info.attribute() != ScreenAttribute.UNSET && !element.getAttr().equals(info.attribute().getCode())) {
			return false;
		}
		return info.text().equals("") || info.text().trim().equals(element.getString().trim());
	}

	public static String toString(final FindBy findBy) {
		final StringBuilder sb = new StringBuilder();
		sb.append("FindBy [");
		if (findBy.how() != How.UNSET) {
			sb.append(findBy.how()).append(" using ").append(findBy.using()).append(", ");
		}
		if (findBy.attribute() != ScreenAttribute.UNSET) {
			sb.append(findBy.attribute()).append(", ");
		}
		if (findBy.row() != 0 || findBy.column() != 0) {
			sb.append(String.format("position: %s x %s", findBy.row(), findBy.column())).append(", ");
		}
		if (!findBy.labelText().equals("")) {
			sb.append(String.format("label: %s ", findBy.labelText())).append(", ");
		}
		if (!findBy.text().equals("")) {
			sb.append(String.format("text: %s ", findBy.text())).append(", ");
		}
		sb.append(']');
		return sb.toString();
	}

}
