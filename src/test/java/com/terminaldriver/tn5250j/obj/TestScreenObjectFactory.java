package com.terminaldriver.tn5250j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.annotation.FindBy;
import com.terminaldriver.tn5250j.annotation.ScreenAttribute;
import com.terminaldriver.tn5250j.mock.MockScreenUtil;


public class TestScreenObjectFactory {
	TerminalDriver driver;
	List<org.tn5250j.framework.tn5250.ScreenField> screenFields;
	ScreenElement currentScreenField;
	@Rule public TestName testName = new TestName();
	FindBy findBy;
	
	@Before
	public void setUp() throws Exception{
		final InputStream data = getClass().getResourceAsStream("/com/terminaldriver/tn5250j/util/wrkmbrpdm.json");
		driver = MockScreenUtil.createTestDriver(data);
		screenFields = Arrays.asList(driver.getSession().getScreen().getScreenFields().getFields());
		currentScreenField=null;
		findBy = getClass().getMethod(testName.getMethodName()).getAnnotation(FindBy.class);
	}
	
	@Test
	@FindBy(text="Work with Members Using PDM",row=1,attribute=ScreenAttribute.WHT)
	public void testFindByColorTextAndRow() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals(1,screenField.startRow());
		assertEquals("                Work with Members Using PDM             ",screenField.value);
	}
	
	@Test
	@FindBy(text="Work with Members Using PDM",row=2,attribute=ScreenAttribute.WHT)
	public void testFindByColorTextAndRow2() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNull(screenField);
	}
	
	@FindBy(attribute=ScreenAttribute.WHT)
	Object colorWhiteFinder;
	
	@Test
	@FindBy(row=10,attribute=ScreenAttribute.WHT)
	public void testFindByColorAndRow() throws Exception {
		final ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals(10,screenField.startRow());
		assertEquals("Opt  Member      Type        Text                                             ",screenField.value);
		
		final FindBy findBy2 = getFinder("colorWhiteFinder");
		ScreenTextBlock screenField2 = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy2,screenFields, screenField);
		assertNotNull(screenField2);
		assertEquals(19,screenField2.startRow());
		assertEquals("      Bottom",screenField2.value);
		
		ScreenTextBlock screenField3 = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy2,screenFields, screenField2);
		assertNotNull(screenField3);
		assertEquals(24,screenField3.startRow());
		assertEquals("                                         (C) COPYRIGHT IBM CORP. 1981, 2003.",screenField3.value);
		
		ScreenTextBlock screenField4 = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy2,screenFields, screenField3);
		assertNotNull(screenField4);
	}
		
	public static FindBy getFinder(String name) throws NoSuchFieldException, SecurityException{
		return TestScreenObjectFactory.class.getDeclaredField(name).getAnnotation(FindBy.class);
	}

}
