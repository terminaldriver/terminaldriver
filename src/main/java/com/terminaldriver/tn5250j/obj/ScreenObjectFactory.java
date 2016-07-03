package com.terminaldriver.tn5250j.obj;

import static com.terminaldriver.tn5250j.util.Find.findMatches;
import static com.terminaldriver.tn5250j.util.ScreenUtils.assertScreen;
import static com.terminaldriver.tn5250j.util.ScreenUtils.pos2row;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.tn5250j.framework.tn5250.Screen5250;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.obj.ScreenElement;
import com.terminaldriver.tn5250j.obj.ScreenField;
import com.terminaldriver.tn5250j.obj.ScreenTextBlock;
import com.terminaldriver.tn5250j.util.ScreenFieldReader;

public class ScreenObjectFactory {

	public static <E> E  createPage(Class<E> clazz, TerminalDriver driver){
		E object = null;
		try {
			object = clazz.newInstance();
			initElements(object,driver);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}
	public static boolean initElements(Object page, TerminalDriver driver){
		final Screen5250 screen = driver.getSession().getScreen();
		final Class<?> clazz = page.getClass();
		assertScreen(clazz,driver);
		final Field[] fields = clazz.getDeclaredFields();
		boolean foundAll=true;
		List<org.tn5250j.framework.tn5250.ScreenField> screenFields = Arrays.asList(screen.getScreenFields().getFields());
		ScreenElement currentScreenField=null;
	    for (Field field : fields) {
	    	FindBy info = field.getAnnotation(FindBy.class);
	    	 
	        if (field.isAnnotationPresent(FindBy.class)) {
	           try {
	        		  if(!field.isAccessible())
	        			  field.setAccessible(true);
	        	   ScreenElement newScreenField = applyFind(field.getType(),driver,info,screenFields,currentScreenField);
	        	  if(newScreenField != null){
	        		  field.set(page, newScreenField);
	        		  currentScreenField=newScreenField;
	        	  }else{
	        		  foundAll=false;
	        	  }
	           } catch (Exception e) {
	        	   e.printStackTrace();
	           }
	        }
	        
	        //Set the driver if present.
	        try {
      		  if(!field.isAccessible())
    			  field.setAccessible(true);
				if(field.getType().equals(TerminalDriver.class) && field.get(page) == null){
					field.set(page, driver);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	    } 
	    return foundAll;
	}

	private static ScreenElement applyFind(Class<?> targetClazz, TerminalDriver driver, FindBy info, List<org.tn5250j.framework.tn5250.ScreenField> screenFields, ScreenElement currentScreenField) {
		if(targetClazz.equals(ScreenField.class)){
			return applyFindScreenField(driver, info,screenFields,currentScreenField);
		}else{
			return applyFindScreenTextBlock(driver, info,screenFields,currentScreenField);
		}
	}
	private static ScreenField applyFindScreenField(final TerminalDriver driver, FindBy info, List<org.tn5250j.framework.tn5250.ScreenField> screenFields, ScreenElement currentScreenField) {
		int currentPosition=0;
		if(currentScreenField instanceof ScreenField){
			currentPosition = screenFields.indexOf(((ScreenField) currentScreenField).getUnderlyingScreenField())+1;
		}else if(currentScreenField instanceof ScreenTextBlock){
			for(org.tn5250j.framework.tn5250.ScreenField field: screenFields){
				if(field.startRow()>currentScreenField.startRow()){
					break;
				}
				if(field.startRow()==currentScreenField.startRow() && field.startCol()>currentScreenField.startCol()){
					break;
				}
				currentPosition=screenFields.indexOf(field);
			}
		}
		while(currentPosition < screenFields.size()){
			ScreenField thisScreen = new ScreenField(driver,screenFields.get(currentPosition++));
			if(findMatches(info,thisScreen)){
				return thisScreen;
			}
		}
		return null;
	}
	
	private static ScreenTextBlock applyFindScreenTextBlock(TerminalDriver driver, FindBy info, List<org.tn5250j.framework.tn5250.ScreenField> screenFields, ScreenElement currentScreenField) {
		Screen5250 screen = driver.getSession().getScreen();
		int currentPosition=0;
		if(currentScreenField != null){
			currentPosition = currentScreenField.endPos();
		}
		if(info.row()>0 && pos2row(currentPosition,screen.getColumns()) != info.row()){
			currentPosition = (info.row()-1)*screen.getColumns();
		}
		if(info.row()>0 && info.column()>0){
			currentPosition = (info.row()-1)*screen.getColumns() + info.column();
		}
		ScreenFieldReader reader = new ScreenFieldReader(driver);
		reader.seek(currentPosition);
		ScreenTextBlock field = null;
		while ((field = reader.readField()) != null){
			if(findMatches(info,field)){
				return field;
			}
		}
		return null;
	}
	
}
