package com.terminaldriver.tn5250j.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.tn5250j.Session5250;
import org.tn5250j.event.ScreenListener;
import org.tn5250j.event.SessionChangeEvent;
import org.tn5250j.event.SessionListener;
import org.tn5250j.framework.common.SessionManager;

import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;

import lombok.Getter;
import lombok.Setter;
import static com.terminaldriver.tn5250j.util.Wait.sleep;

public class TerminalDriver {

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
	Keys keys = new Keys(this);
	
	final TerminalDriverSessionListener terminalDriverSessionListener = new TerminalDriverSessionListener();
	final TerminalDriverScreenListener terminalDriverScreenListener = new TerminalDriverScreenListener();

	public TerminalDriver() {
		super();
	}

	public void connectTo(String host, int port) {
		this.host = host;
		this.port = port;
		createConn(host, port);
	}
	
	public String getScreenText(){
		return new String(session.getScreen().getCharacters());
	}
	
	public ScreenDataContainer getCurrentScreenData(){
		return new ScreenDataContainer(session.getScreen());
	}

	public void dumpScreen(){
		session.getScreen().dumpScreen();
	}
	
	public void sendKeys(String keys){
		session.getScreen().sendKeys(keys);
	}
	
	private Session5250 createConn(String host, int port) {

		Properties sessionProperties = new Properties();
		sessionProperties.put("SESSION_HOST", host);
		sessionProperties.put("SESSION_HOST_PORT", String.valueOf(port));
		sessionProperties.put("SESSION_CODE_PAGE",codePage);

		session = SessionManager.instance().openSession(sessionProperties, "", "");

		session.addSessionListener(terminalDriverSessionListener);
		session.connect();
		
		for(int i = 1;i < 200 && !session.isConnected();i++) {
			sleep(100);
		}
		waitForScreen(5000);
		
		session.getScreen().addScreenListener(terminalDriverScreenListener);
		return session;
	}

	public void addScreenListener(ScreenListener listener) {
		session.getScreen().addScreenListener(listener);
	}

	public void addSessionListener(SessionListener listener) {
		session.addSessionListener(listener);
	}

	public void close() {
		session.disconnect();
	}
	
	public boolean waitForScreen(long timeOutMillis){
		final long screenChange = terminalDriverScreenListener.getLastScreenChange();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		while(screenChange == terminalDriverScreenListener.getLastScreenChange() && System.currentTimeMillis() < stopTime){
			sleep(100);
		}
		sleep(50);
		return System.currentTimeMillis() < stopTime;
	}
	public boolean waitForUpdate(long timeOutMillis){
		terminalDriverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		boolean isChanged = terminalDriverScreenListener.waitForUpdate(stopTime);
		sleep(50);
		return isChanged;
	}
	public boolean waitForField(By by,long timeOutMillis){
		terminalDriverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		boolean isChanged = false;
		do{
			for(ScreenField field: getScreenFields()){
				if(by.matches(field)){
					return true;
				}
			}
			isChanged = terminalDriverScreenListener.waitForUpdate(stopTime);
		}while(isChanged);
		return false;
	}
	public boolean waitFor(By by,long timeOutMillis){
		terminalDriverScreenListener.markUpdate();
		final long stopTime = System.currentTimeMillis() + timeOutMillis;
		boolean isChanged = false;
		do{
			if(by.findElement(this) != null){
				return true;
			}
			isChanged = terminalDriverScreenListener.waitForUpdate(stopTime);
		}while(isChanged);
		return false;
	}
	
	public ScreenElement findElement(By by){
		return by.findElement(this);
	}
	public List<ScreenElement> findElements(By by){
		return by.findElements(this);
	}
	
	public ScreenField findFieldById(int id){
		for(org.tn5250j.framework.tn5250.ScreenField fielditem:getRawScreenFields()){
			if(fielditem.getFieldId() == id){
				return new ScreenField(fielditem);
			}
		}
		return null;
	}
	
	public List<ScreenElement> findFieldsById(int id){
		List<ScreenElement> items = new ArrayList<ScreenElement>();
		items.add(findFieldById(id));
		return items;
	}
	
	public ScreenElement findElementByLabelText(String label, com.terminaldriver.tn5250j.obj.By.ByLabelText.Position position){
		//TODO
		return null;
	}
	public List<ScreenElement> findElementsByLabelText(String label, com.terminaldriver.tn5250j.obj.By.ByLabelText.Position position){
		List<ScreenElement> items = new ArrayList<ScreenElement>();
		items.add(findElementByLabelText(label,position));
		return items;
	}

	public ScreenElement findElementByText(String text){
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if(text != null && field.getString() != null && text.trim().equals(field.getString().trim())){
				return field;
			}
		}
		return null;
	}
	public List<ScreenElement> findElementsByText(String text){
		List<ScreenElement> items = new ArrayList<ScreenElement>();
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if(text != null && field.getString() != null && text.trim().equals(field.getString().trim())){
				items.add(field);
			}
		}
		return items;
	}

	public ScreenElement findElementByAttribute(ScreenAttribute attribute){
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if(attribute != ScreenAttribute.UNSET && !field.attr.equals(attribute.getCode())){
				return field;
			}
		}
		return null;
	}
	public List<ScreenElement> findElementsByAttribute(ScreenAttribute attribute){
		List<ScreenElement> items = new ArrayList<ScreenElement>();
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if(attribute != ScreenAttribute.UNSET && !field.attr.equals(attribute.getCode())){
				items.add(field);
			}
		}
		return items;
	}
	public ScreenElement findElementByPosition(final Integer row, final Integer column){
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if((column == null || field.startCol()==column) && (row == null || field.startRow()==row)){
				return field;
			}
		}
		return null;
	}
	public List<ScreenElement> findElementsByPosition(final Integer row, final Integer column){
		List<ScreenElement> items = new ArrayList<ScreenElement>();
		ScreenFieldReader reader = new ScreenFieldReader(session.getScreen());
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if((column == null || field.startCol()==column) && (row == null || field.startRow()==row)){
				items.add(field);
			}
		}
		return items;
	}
	
	public List<ScreenField> getScreenFields(){
		List<ScreenField> retval = new ArrayList<ScreenField>();
		for(org.tn5250j.framework.tn5250.ScreenField field: getRawScreenFields()){
			retval.add(new ScreenField(field));
		}
		return retval;
	}
	private List<org.tn5250j.framework.tn5250.ScreenField> getRawScreenFields(){
		return Arrays.asList(session.getScreen().getScreenFields().getFields());
	}
	
	
	public class TerminalDriverScreenListener implements ScreenListener{
		
		@Setter
		boolean SuppressFullScreenEmpty = true;
		
		/**
		 * Time in milliseconds of the last time the full screen was changed
		 */
		@Getter
		long lastScreenChange = 0;
		@Getter
		long lastScreenUpdate = 0;
		
		public void onScreenChanged(int arg0, int row1, int col1, int row2, int col2) {
				
			if(row1==0 && col1==0 && row2>=23 && col2>=79){
				//Suppress notification of a completely empty screen. Assuming content will follow promptly.
				if(SuppressFullScreenEmpty && getScreenText().trim().isEmpty()){
					return;
				}
				//Auto close messages window.
				final ScreenElement element = findElement(By.and(By.row(1),By.attribute(ScreenAttribute.WHT)));
				if(element != null && element.getString().trim().equals("Display Program Messages")){
					final ScreenElement pressEnterText= findElement(By.text("Press Enter to continue."));
					if(pressEnterText != null){
						System.out.println("Closing messages window");
						keys().enter();
					}
				}
				lastScreenChange  = System.currentTimeMillis();
			}
			lastScreenUpdate  = System.currentTimeMillis();
			System.out.println(String.format("screen changed %s %s,%s x %s,%s @ %s",arg0,row1,col1,row2,col2,new Date().toString()));
		}

		public void onScreenSizeChanged(int arg0, int arg1) {}
		
		long markedUpdate =0;
		public void markUpdate(){
			markedUpdate=lastScreenUpdate;
		}
		public boolean waitForUpdate(long untilTime){
			while(System.currentTimeMillis() < untilTime){
				if(markedUpdate != lastScreenUpdate){
					return true;
				}
				sleep(100);
			}
			return false;
		}
	}

	public static class TerminalDriverSessionListener implements SessionListener{
		
		public void onSessionChanged(SessionChangeEvent arg0) {
		}	
	}
	
	public Keys keys(){
		return keys;
	}

}
