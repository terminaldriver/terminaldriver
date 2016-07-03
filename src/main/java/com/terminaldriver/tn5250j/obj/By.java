package com.terminaldriver.tn5250j.obj;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;

/**
 * Mechanism used to locate elements within a screen.
 *
 */
public abstract class By {
	/**
	 * 
	 * @return a By which matches anything.
	 */
	public static By any() {
		return new ByAny();
	}

	/**
	 * @param id
	 *            The value of the "id" attribute to search for
	 * @return a By which locates screen elements by their ordinal position.
	 */
	public static By id(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cannot find elements with a null id attribute.");
		}

		return new ById(id);
	}

	/**
	 * @param labelText
	 *            The exact text to match against
	 * @return a By which locates A elements by the exact text it displays
	 */
	public static By labelText(final String labelText) {
		if (labelText == null) {
			throw new IllegalArgumentException("Cannot find elements when link text is null.");
		}

		return new ByLabelText(labelText);
	}

	public static By and(final By... by) {
		if (by.length == 0) {
			throw new IllegalArgumentException("Cannot find elements when compound By is empty.");
		}

		return new ByAnd(by);
	}

	public static By attribute(final ScreenAttribute attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("Cannot find elements when attribute is null.");
		}

		return new ByAttribute(attribute);
	}

	public static By text(final String text) {
		if (text == null) {
			throw new IllegalArgumentException("Cannot find elements when text is null.");
		}

		return new ByText(text);
	}

	public static By row(final int row) {
		if (row < 1) {
			throw new IllegalArgumentException("Cannot find elements when row is invalid.");
		}

		return new ByPosition(row, null);
	}

	public static By column(final int column) {
		if (column < 1) {
			throw new IllegalArgumentException("Cannot find elements when column is invalid.");
		}

		return new ByPosition(null, column);
	}

	public static By position(final int row, final int column) {
		if (row < 1) {
			throw new IllegalArgumentException("Cannot find elements when row is invalid.");
		}
		if (column < 1) {
			throw new IllegalArgumentException("Cannot find elements when column is invalid.");
		}

		return new ByPosition(row, column);
	}

	/**
	 * Find a single element. Override this method if necessary.
	 *
	 * @param driver
	 *            A driver to use to find the element
	 * @return The ScreenElement that matches the selector
	 */
	public ScreenElement findElement(final TerminalDriver driver) {
		final List<ScreenElement> allElements = findElements(driver);
		if (allElements == null || allElements.isEmpty()) {
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		}
		return allElements.get(0);
	}

	/**
	 * Find many elements.
	 *
	 * @param driver
	 *            A driver to use to find the element
	 * @return A list of ScreenElements matching the selector
	 */
	public abstract List<ScreenElement> findElements(TerminalDriver driver);

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final By by = (By) o;

		return toString().equals(by.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		// A stub to prevent endless recursion in hashCode()
		return "[unknown locator]";
	}

	public abstract boolean matches(ScreenElement element);

	public static class ById extends By implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		private final String id;

		public ById(final String id) {
			this.id = id;
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			return driver.findFieldsById(Integer.valueOf(id));
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			return driver.findFieldById(Integer.valueOf(id));
		}

		@Override
		public String toString() {
			return "By.id: " + id;
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return element instanceof ScreenField && ((ScreenField) element).getFieldId() == Integer.valueOf(id);
		}
	}

	public static class ByAny extends ById implements Serializable {

		private static final long serialVersionUID = 5341968046120372169L;

		public ByAny() {
			super("1");
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return true;
		}
	}

	public static class ByLabelText extends By implements Serializable {

		private static final long serialVersionUID = 1967414585359739708L;

		public static enum Position {
			LEFT, TOP;
		}

		private final String labelText;
		private final Position position;

		public ByLabelText(final String labelText) {
			this.labelText = labelText;
			this.position = Position.LEFT;
		}

		public ByLabelText(final String labelText, final Position position) {
			this.labelText = labelText;
			this.position = position;
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			return driver.findElementsByLabelText(labelText, position);
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			return driver.findElementByLabelText(labelText, position);
		}

		@Override
		public String toString() {
			return "By.labelText: " + labelText;
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return false; // tODO
		}
	}

	public static class ByText extends By implements Serializable {

		private static final long serialVersionUID = 1967414585359739708L;

		private final String text;

		public ByText(final String text) {
			this.text = text;
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			return driver.findElementsByText(text);
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			return driver.findElementByText(text);
		}

		@Override
		public String toString() {
			return "By.text: " + text;
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return element != null && element.getString().trim().equals(text.trim());
		}
	}

	public static class ByAttribute extends By implements Serializable {

		private static final long serialVersionUID = 1967414585359739708L;

		private final ScreenAttribute attribute;

		public ByAttribute(final ScreenAttribute attribute) {
			this.attribute = attribute;
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			return driver.findElementsByAttribute(attribute);
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			return driver.findElementByAttribute(attribute);
		}

		@Override
		public String toString() {
			return "By.attribute: " + attribute;
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return element.getAttr().equals(attribute.getCode());
		}
	}

	public static class ByPosition extends By implements Serializable {

		private static final long serialVersionUID = 1967414585359739708L;

		private final Integer row;
		private final Integer column;

		public ByPosition(final Integer row, final Integer column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			return driver.findElementsByPosition(row, column);
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			return driver.findElementByPosition(row, column);
		}

		@Override
		public String toString() {
			return String.format("By.position: %s,%s", row, column);
		}

		@Override
		public boolean matches(final ScreenElement element) {
			return (column == null || element.startCol() == column) && (row == null || element.startRow() == row);
		}
	}

	public static class ByAnd extends By implements Serializable {

		private static final long serialVersionUID = 1967414585359739708L;

		private final List<By> bys;

		public ByAnd(final By... by) {
			bys = Arrays.asList(by);
		}

		@Override
		public List<ScreenElement> findElements(final TerminalDriver driver) {
			final List<ScreenElement> x = bys.get(0).findElements(driver);
			for (int i = x.size() - 1; i >= 0; i--) {
				if (!matches(x.get(i))) {
					x.remove(i);
				}
			}
			return x;
		}

		@Override
		public ScreenElement findElement(final TerminalDriver driver) {
			final List<ScreenElement> x = findElements(driver);
			if (x.isEmpty()) {
				return null;
			} else {
				return x.get(0);
			}
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("By.compound {");
			for (final By b : bys) {
				sb.append(b.toString()).append(",");
			}
			sb.append("}{");
			return sb.toString();
		}

		@Override
		public boolean matches(final ScreenElement element) {
			for (final By by : bys) {
				if (!by.matches(element)) {
					return false;
				}
			}
			return true;
		}
	}

}