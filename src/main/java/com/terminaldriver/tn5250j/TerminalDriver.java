package com.terminaldriver.tn5250j;

import static com.terminaldriver.tn5250j.util.Wait.sleep;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.tn5250j.Session5250;
import org.tn5250j.event.ScreenListener;
import org.tn5250j.event.ScreenOIAListener;
import org.tn5250j.event.SessionChangeEvent;
import org.tn5250j.event.SessionKeysListener;
import org.tn5250j.event.SessionListener;
import org.tn5250j.framework.common.SessionManager;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenOIA;

import com.terminaldriver.common.TerminalDriverChangeListener;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.obj.By;
import com.terminaldriver.tn5250j.obj.KeyStrokes;
import com.terminaldriver.tn5250j.obj.ScreenDataContainer;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;
import com.terminaldriver.tn5250j.util.ScreenUtils;

import lombok.Getter;
import lombok.Setter;

public class TerminalDriver implements Closeable {

	@Getter
	@Setter
	String codePage = "37";

	@Getter
	Session5250 session;

	@Getter
	String host;

	@Getter
	int port;

	@Getter
	KeyStrokes keys = new KeyStrokes(this);

	final TerminalDriverSessionListener driverSessionListener = new TerminalDriverSessionListener();
	final TerminalDriverScreenListener driverScreenListener = new TerminalDriverScreenListener();
	final TerminDriverScreenOIAListener driverScreenOIAListener = new TerminDriverScreenOIAListener();
	final List<TerminalDriverChangeListener> listeners = new ArrayList<TerminalDriverChangeListener>();
	long markedUpdate;

	private SessionKeysListener driverSessionKeysListener = new TerminalDriverSessionKeysListener();;

	public TerminalDriver() {
		super();
	}

	public void connectTo(final String host, final int port) {
		this.host = host;
		this.port = port;
		createConn(host, port);
	}

	public String getScreenText() {
		return new String(session.getScreen().getCharacters());
	}

	public int getScreenColumns() {
		return session.getScreen().getColumns();
	}

	public int getScreenRows() {
		return session.getScreen().getRows();
	}

	public ScreenDataContainer getCurrentScreenData() {
		return new ScreenDataContainer(session.getScreen());
	}

	public void dumpScreen() {
		session.getScreen().dumpScreen();
	}

	public void sendKeys(final String keys) {
		//fireSendKeys(keys);
		session.getScreen().sendKeys(keys);
	}

	private Session5250 createConn(final String host, final int port) {
		final Properties sessionProperties = new Properties();
		sessionProperties.put("SESSION_HOST", host);
		sessionProperties.put("SESSION_HOST_PORT", String.valueOf(port));
		sessionProperties.put("SESSION_CODE_PAGE", codePage);

		session = SessionManager.instance().openSession(sessionProperties, "", "");

		session.addSessionListener(driverSessionListener);

		session.getScreen().addScreenListener(driverScreenListener);
		session.getScreen().addSessionKeysListener(driverSessionKeysListener);
		session.getScreen().getOIA().addOIAListener(driverScreenOIAListener);
		session.connect();

		for (int i = 1; i < 200 && !session.isConnected(); i++) {
			sleep(100);
		}
		waitForScreen(5000);
		waitForInputNotInhibited(5000);

		return session;
	}
	
	public void setSession(Session5250 session){
		this.session=session;
		session.addSessionListener(driverSessionListener);
		session.getScreen().addSessionKeysListener(driverSessionKeysListener);

		session.getScreen().addScreenListener(driverScreenListener);
		session.getScreen().getOIA().addOIAListener(driverScreenOIAListener);
	}

	public void addScreenListener(final ScreenListener listener) {
		session.getScreen().addScreenListener(listener);
	}

	public void addSessionListener(final SessionListener listener) {
		session.addSessionListener(listener);
	}

	public void close() throws IOException {
		session.disconnect();
		closeListeners();
	}
	public void closeListeners() throws IOException {
		for (final TerminalDriverChangeListener listener : listeners) {
			if (listener instanceof Closeable) {
				((Closeable) listener).close();
			}
		}
	}

	public boolean waitForScreen(final long timeOutMillis) {
		final long screenChange = driverScreenListener.getLastScreenChange();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		while (System.currentTimeMillis() < stopTime) {
			if (screenChange != driverScreenListener.getLastScreenChange()) {
				sleep(50);
				return true;
			}
			sleep(100);
		}
		// sleep(50);
		return false;
	}

	public boolean waitForInputNotInhibited(final long timeOutMillis) {
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		while (System.currentTimeMillis() < stopTime) {
			if (acceptingInput()) {
				sleep(50);
				return true;
			}
			sleep(100);
		}
		return false;
	}

	public boolean acceptingInput() {
		return session.getScreen().getOIA().getInputInhibited() == ScreenOIA.INPUTINHIBITED_NOTINHIBITED;
	}

	public boolean waitForUpdate(final long timeOutMillis) {
		driverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		final boolean isChanged = driverScreenListener.waitForUpdate(stopTime);
		return isChanged;
	}

	public boolean waitForField(final By by, final long timeOutMillis) {
		driverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		boolean isChanged = false;
		do {
			for (final ScreenField field : getScreenFields()) {
				if (by.matches(field)) {
					return true;
				}
			}
			isChanged = driverScreenListener.waitForUpdate(stopTime);
		} while (isChanged);
		return false;
	}

	public boolean waitFor(final By by, final long timeOutMillis) {
		driverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		boolean isChanged = false;
		do {
			if (by.findElement(this) != null) {
				return true;
			}
			isChanged = driverScreenListener.waitForUpdate(stopTime);
		} while (isChanged);
		return false;
	}

	public ScreenElement findElement(final By by) {
		return by.findElement(this);
	}

	public List<ScreenElement> findElements(final By by) {
		return by.findElements(this);
	}

	public ScreenField findFieldById(final int id) {
		for (final org.tn5250j.framework.tn5250.ScreenField fielditem : getRawScreenFields()) {
			if (fielditem.getFieldId() == id) {
				return new ScreenField(this, fielditem);
			}
		}
		return null;
	}

	public List<ScreenElement> findFieldsById(final int id) {
		final List<ScreenElement> items = new ArrayList<ScreenElement>();
		items.add(findFieldById(id));
		return items;
	}

	public ScreenElement findElementByLabelText(final String label,
			final com.terminaldriver.tn5250j.obj.By.ByLabelText.Position position) {
		// TODO
		return null;
	}

	public List<ScreenElement> findElementsByLabelText(final String label,
			final com.terminaldriver.tn5250j.obj.By.ByLabelText.Position position) {
		final List<ScreenElement> items = new ArrayList<ScreenElement>();
		items.add(findElementByLabelText(label, position));
		return items;
	}

	public ScreenElement findElementByText(final String text) {
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (text != null && field.getString() != null && text.trim().equals(field.getString().trim())) {
				return field;
			}
		}
		return null;
	}

	public List<ScreenElement> findElementsByText(final String text) {
		final List<ScreenElement> items = new ArrayList<ScreenElement>();
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (text != null && field.getString() != null && text.trim().equals(ScreenUtils.scrubZeros(field.getString().trim()))) {
				items.add(field);
			}
		}
		return items;
	}

	public ScreenElement findElementByAttribute(final ScreenAttribute attribute) {
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (attribute == ScreenAttribute.UNSET || field.getAttr().equals(attribute.getCode())) {
				return field;
			}
		}
		return null;
	}

	public List<ScreenElement> findElementsByAttribute(final ScreenAttribute attribute) {
		final List<ScreenElement> items = new ArrayList<ScreenElement>();
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null) {
			if (attribute == ScreenAttribute.UNSET || field.getAttr().equals(attribute.getCode())) {
				items.add(field);
			}
		}
		return items;
	}

	public ScreenElement findElementByPosition(final Integer row, final Integer column, final Integer length) {
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		// If both row and column are specified, return a (potential) partial
		// text block
		if (row != null && column != null) {
			if (length != null) {
				return reader.read(row, column, length);
			} else {
				return reader.readField(row, column);
			}
		}
		while ((field = reader.readField()) != null) {
			if ((column == null || field.startCol() == column) && (row == null || field.startRow() == row)) {
				return field;
			}
		}
		return null;
	}

	public List<ScreenElement> findElementsByPosition(final Integer row, final Integer column, final Integer length) {
		final List<ScreenElement> items = new ArrayList<ScreenElement>();
		final ScreenFieldReader reader = new ScreenFieldReader(this);
		ScreenTextBlock field = null;
		if (row != null && column != null) {
			items.add(findElementByPosition(row, column, length));
		} else {
			while ((field = reader.readField()) != null) {
				if ((column == null || field.startCol() == column) && (row == null || field.startRow() == row)) {
					items.add(field);
				}
			}
		}
		return items;
	}

	public List<ScreenField> getScreenFields() {
		final List<ScreenField> retval = new ArrayList<ScreenField>();
		for (final org.tn5250j.framework.tn5250.ScreenField field : getRawScreenFields()) {
			retval.add(new ScreenField(this, field));
		}
		return retval;
	}
	
	public ScreenField getScreenFieldAt(final int row, final int col) {
		for (final org.tn5250j.framework.tn5250.ScreenField field : getRawScreenFields()) {
			if(field.startRow() == row && field.startCol()>=col && field.startCol()+field.getLength() >= col){
				return new ScreenField(this, field);
			}
		}
		return null;
	}
	
	public ScreenField getScreenFieldAt(final int pos) {
		for (final org.tn5250j.framework.tn5250.ScreenField field : getRawScreenFields()) {
			if(field.startPos() <= pos && field.endPos() >= pos){
				return new ScreenField(this, field);
			}
		}
		return null;
	}

	private List<org.tn5250j.framework.tn5250.ScreenField> getRawScreenFields() {
		return Arrays.asList(session.getScreen().getScreenFields().getFields());
	}

	public class TerminalDriverSessionKeysListener implements SessionKeysListener {

		public void fieldStringSet(Screen5250 arg0, org.tn5250j.framework.tn5250.ScreenField screenField, String value) {
			fireFieldSetString(screenField,value);
		}

		public void keysSent(Screen5250 arg0, String keys) {
			fireSendKeys(keys);
		}

		public void cursorMoved(Screen5250 arg0, int pos) {
			fireCursorMoved(pos);
		}
		
	}
	public class TerminalDriverScreenListener implements ScreenListener {

		@Setter
		boolean suppressFullScreenEmpty = true;

		/**
		 * Time in milliseconds of the last time the full screen was changed
		 */
		@Getter
		long lastScreenChange;
		/**
		 * Time in milliseconds of the last time the screen was partially
		 * updated
		 */
		@Getter
		long lastScreenUpdate;

		public void onScreenChanged(final int arg0, final int row1, final int col1, final int row2, final int col2) {

			if (row1 == 0 && col1 == 0 && row2 >= 23 && col2 >= 79) {
				fireScreenChanged();
				// Suppress notification of a completely empty screen. Assuming
				// content will follow promptly.
				if (suppressFullScreenEmpty && getScreenText().trim().isEmpty()) {
					return;
				}
				// Auto close messages window.
				final ScreenElement element = findElement(By.and(By.row(1), By.attribute(ScreenAttribute.WHT)));
				if (element != null && (element.getString().trim().equals("Display Program Messages")
						 || element.getString().trim().equals("Display Messages"))
						&& acceptingInput()) {
					fireNote("Closing messages window");
					keys().enter();
				}
				lastScreenChange = System.currentTimeMillis();
			} else {
				fireScreenPartialsUpdate(row1, col1, row2, col2);
			}
			lastScreenUpdate = System.currentTimeMillis();
		}

		public void onScreenSizeChanged(final int cols, final int rows) {
			fireScreenSizeChanged(cols, rows);
		}

		public void markUpdate() {
			markedUpdate = lastScreenUpdate;
		}

		public boolean waitForUpdate(final long untilTime) {
			while (System.currentTimeMillis() < untilTime) {
				if (markedUpdate != lastScreenUpdate) {
					return true;
				}
				sleep(100);
			}
			return false;
		}
	}

	public static class TerminalDriverSessionListener implements SessionListener {

		public void onSessionChanged(final SessionChangeEvent arg0) {
		}
	}

	public class TerminDriverScreenOIAListener implements ScreenOIAListener {

		public void onOIAChanged(final ScreenOIA arg0, final int arg1) {
			if(arg1==ScreenOIAListener.OIA_CHANGED_INPUTINHIBITED){
				fireInputInhibited(arg0.getInputInhibited() != ScreenOIA.INPUTINHIBITED_NOTINHIBITED);
			}
		}
	}

	public KeyStrokes keys() {
		return keys;
	}

	public void fireFieldSetString(final org.tn5250j.framework.tn5250.ScreenField screenField, final String inputValue) {
		String value = inputValue;
		if (ScreenAttribute.getAttrEnum((char) screenField.getAttr()).isNonDisplay()) {
			value = value.replaceAll(".", "*");
		}
		ScreenField myScreenField = new ScreenField(this, screenField);
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.fieldSetString(this, myScreenField, value);
		}
	}

	private void fireSendKeys(final String keys) {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.sendKeys(this, keys);
		}
	}

	private void fireScreenSizeChanged(final int cols, final int rows) {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.screenSizeChanged(this, cols, rows);
		}
	}

	private void fireScreenPartialsUpdate(final int row1, final int col1, final int row2, final int col2) {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.screenPartialsUpdate(this, row1, col1, row2, col2);
		}
	}

	private void fireScreenChanged() {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.screenChanged(this);
		}
	}

	private void fireNote(final String note) {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.note(note);
		}
	}
	
	private void fireInputInhibited(final boolean inhibited) {
		for (final TerminalDriverChangeListener listener : listeners) {
			listener.inputInhibited(inhibited);
		}
	}
	
	private void fireCursorMoved(final int pos){
		for (final TerminalDriverChangeListener listener : listeners) {
			int col = ScreenUtils.pos2col(pos+1, getScreenColumns());
			int row = ScreenUtils.pos2row(pos+1, getScreenColumns());
			listener.cursorMoved(this,row,col);
		}
	}

	public void addTerminalDriverChangeListener(final TerminalDriverChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeTerminalDriverChangeListener(final TerminalDriverChangeListener listener) {
		listeners.remove(listener);
	}
}
