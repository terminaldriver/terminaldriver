package com.terminaldriver.tn5250j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.Table;
import com.terminaldriver.tn5250j.mock.MockScreenUtil;

import lombok.Data;


public class TestScreenObjectFactory_FindTable {
	TerminalDriver driver;
	ScreenElement currentScreenField;
	@Rule public TestName testName = new TestName();
	FindBy findBy;
	
	@Before
	public void setUp() throws Exception{
		final InputStream data = getClass().getResourceAsStream("/com/terminaldriver/tn5250j/util/wrkmbrpdm.json");
		driver = MockScreenUtil.createTestDriver(data);
		currentScreenField=null;
		findBy = getClass().getMethod(testName.getMethodName()).getAnnotation(FindBy.class);
	}
	
	
	@Table(type=WorkListItem.class,row=11)
	List<WorkListItem> table1;
	
	@Test
	@FindBy(row=8,column=3)
	public void testTable1() throws Exception {
		Field field = getClass().getDeclaredField("table1");
		ScreenElement newCurrentScreenField = ScreenObjectFactory.applyTableAnnotation(driver,this,field.getAnnotation(Table.class),field, currentScreenField);
		assertNotNull(table1);
		assertEquals(3, table1.size());
		
	}
	
	public static FindBy getFinder(String name) throws NoSuchFieldException, SecurityException{
		return TestScreenObjectFactory_FindTable.class.getDeclaredField(name).getAnnotation(FindBy.class);
	}

	
	@Data
	public static class WorkListItem{
		
		@FindBy
		ScreenField selector;
		@FindBy
		ScreenField memberName;
		@FindBy
		ScreenField type;
		@FindBy
		ScreenField text;
		
	}
}
