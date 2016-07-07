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
import com.terminaldriver.tn5250j.mock.MockScreenUtil;


public class TestScreenObjectFactory_FindScreenTextBlock {
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
	
	@FindBy
	String nextText;
	
	@Test
	@FindBy(row=8,column=3,length=9)
	public void testFindByPositionAndLength() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals("8=Display",screenField.value);
		FindBy nextTextFindBy = getFinder("nextText");
		ScreenTextBlock screenField2 = ScreenObjectFactory.applyFindScreenTextBlock(driver,nextTextFindBy,screenFields, screenField);
		assertNotNull(screenField2);
		assertEquals("description  9=Save  13=Change text  14=Compile  15=Create module...",screenField2.getString());
	}
	@Test
	@FindBy(text="Work with Members Using PDM",row=8,column=3,length=9)
	public void testFindByPositionLengthAndTextNoMatch() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNull(screenField);
	}
	@Test
	@FindBy(text="8=Display",row=8,column=3,length=9)
	public void testFindByPositionLengthAndTextMatch() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals("8=Display",screenField.value);
	}
	
	@Test
	@FindBy(row=8,column=2)
	public void testFindByPosition() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals("8=Display description  9=Save  13=Change text  14=Compile  15=Create module...",screenField.value);
	}
	
	@Test
	@FindBy(row=8,column=3)
	public void testFindByPositionClose() throws Exception {
		ScreenTextBlock screenField = ScreenObjectFactory.applyFindScreenTextBlock(driver,findBy,screenFields, currentScreenField);
		assertNotNull(screenField);
		assertEquals("8=Display description  9=Save  13=Change text  14=Compile  15=Create module...",screenField.value);
	}
	
	public static FindBy getFinder(String name) throws NoSuchFieldException, SecurityException{
		return TestScreenObjectFactory_FindScreenTextBlock.class.getDeclaredField(name).getAnnotation(FindBy.class);
	}

}
