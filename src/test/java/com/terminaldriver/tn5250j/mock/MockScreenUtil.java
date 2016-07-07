package com.terminaldriver.tn5250j.mock;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.tn5250j.Session5250;
import org.tn5250j.SessionConfig;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.framework.tn5250.ScreenFields;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terminaldriver.tn5250j.TerminalDriver;
import static org.tn5250j.TN5250jConstants.*;
public class MockScreenUtil {

	public static TerminalDriver createTestDriver(InputStream data) throws JsonParseException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		final Map<String, Object> props = new ObjectMapper().readValue(data,
				new TypeReference<HashMap<String, Object>>() {
				});
		
		final Screen5250 screen= new Screen5250(){
			public synchronized int GetScreen(char buffer[], int bufferLength, int from, //NOPMD
					int length, int plane)
			{
				if (plane ==PLANE_TEXT){
					System.out.println(props.get("text"));
					copy(props.get("text"),buffer);
				}
				if (plane ==PLANE_ATTR){
					System.out.println(props.get("attr"));
					copy(props.get("attr"),buffer);
				}
				if (plane ==PLANE_IS_ATTR_PLACE){
					System.out.println(props.get("isAttr"));
					copy(props.get("isAttr"),buffer);
				}
				if (plane ==PLANE_COLOR){
					copy(props.get("color"),buffer);
				}
				if (plane ==PLANE_EXTENDED){
					copy(props.get("extended"),buffer);
				}
				if (plane ==PLANE_EXTENDED_GRAPHIC){
					copy(props.get("graphic"),buffer);
				}
				if (plane ==PLANE_FIELD){
					copy(props.get("field"),buffer);
				}
				return bufferLength;
			}

			private void copy(Object object, char[] buffer) {
				if(object instanceof String){
					int to = Math.min(((String) object).length(), buffer.length);
					for(int i=0; i<to; i++){
						buffer[i]=((String) object).charAt(i);
					}
				}
			}
		};
		Field field = Screen5250.class.getDeclaredField("screenFields");
		if(!field.isAccessible()){
			field.setAccessible(true);
		}
		field.set(screen, new MockScreenFields(screen));
		
		final Session5250 session = new Session5250(new Properties(),"","",new SessionConfig("", "")){
			public Screen5250 getScreen(){
				return screen;
			}
		};
		TerminalDriver driver = new TerminalDriver();
		Field sessionfield = TerminalDriver.class.getDeclaredField("session");
		if(!sessionfield.isAccessible()){
			sessionfield.setAccessible(true);
		}
		sessionfield.set(driver, session);
		
		@SuppressWarnings("unchecked")
		List<Map<?,?>> fields = (List<Map<?,?>>)props.get("fields");
		for(Map<?, ?> fieldInfo: fields){
			((MockScreenFields)screen.getScreenFields()).setField((Integer)fieldInfo.get("attr"), (Integer)fieldInfo.get("startRow"), (Integer)fieldInfo.get("startCol"), (Integer)fieldInfo.get("length"), (Integer)fieldInfo.get("FFW1"), (Integer)fieldInfo.get("FFW2"), (Integer)fieldInfo.get("FCW1"), (Integer)fieldInfo.get("FCW2"));
		}
		return driver;
	}
	
	public static class MockScreenFields extends ScreenFields{

		public MockScreenFields(Screen5250 arg0) {
			super(arg0);
		}

		@Override
		public ScreenField setField(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
			return super.setField(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		}
	}
	}
