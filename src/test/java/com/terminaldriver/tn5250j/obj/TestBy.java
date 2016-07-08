package com.terminaldriver.tn5250j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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


public class TestBy {
	TerminalDriver driver;
	List<org.tn5250j.framework.tn5250.ScreenField> screenFields;
	ScreenElement currentScreenField;
	@Rule public TestName testName = new TestName();
	By by;
	
	@Before
	public void setUp() throws Exception{
		final InputStream data = getClass().getResourceAsStream("/com/terminaldriver/tn5250j/util/wrkmbrpdm.json");
		driver = MockScreenUtil.createTestDriver(data);
		screenFields = Arrays.asList(driver.getSession().getScreen().getScreenFields().getFields());
		currentScreenField=null;
	}
	
	@Test
	public void testFindByColor() throws Exception {
		ScreenElement element = driver.findElement(By.attribute(ScreenAttribute.WHT));
		assertNotNull(element);
		assertEquals(1,element.startRow());
		assertEquals("                Work with Members Using PDM             ",element.getString());
	}
	
	@Test
	public void testFindsByColor() throws Exception {
		List<ScreenElement> elements = driver.findElements(By.attribute(ScreenAttribute.WHT));
		assertNotNull(elements);
		assertEquals(5,elements.size());
		assertEquals("                Work with Members Using PDM             ",elements.get(0).getString());
	}
			
	@Test
	public void testFindByRow() throws Exception {
		ScreenElement element = driver.findElement(By.row(3));
		assertNotNull(element);
		assertEquals(3,element.startRow());
		assertEquals(1,element.startCol());
		assertEquals("File  . . . . . .",element.getString());
	}
	@Test
	public void testFindsByRow() throws Exception {
		List<ScreenElement> elements = driver.findElements(By.row(3));
		assertNotNull(elements);
		assertEquals(4,elements.size());
		assertEquals(3,elements.get(0).startRow());
		assertEquals(1,elements.get(0).startCol());
		assertEquals("File  . . . . . .",elements.get(0).getString());
	}

	@Test
	public void testFindByPositionExact() throws Exception {
		ScreenElement element = driver.findElement(By.position(3,2));
		assertNotNull(element);
		assertEquals(3,element.startRow());
		assertEquals("File  . . . . . .",element.getString());
	}
	@Test
	public void testFindByPositionPartial() throws Exception {
		ScreenElement element = driver.findElement(By.position(3,3));
		assertNotNull(element);
		assertEquals(3,element.startRow());
		assertEquals("ile  . . . . . .",element.getString());
	}
	@Test
	public void testFindByTextBlock() throws Exception {
		ScreenElement element = driver.findElement(By.textBlock(3,2,4));
		assertNotNull(element);
		assertEquals(3,element.startRow());
		assertEquals(2,element.startCol());
		assertEquals("File",element.getString());
	}
	@Test
	public void testFindByText() throws Exception {
		ScreenElement element = driver.findElement(By.text("Work with Members Using PDM"));
		assertNotNull(element);
		assertEquals(1,element.startRow());
		assertEquals(11,element.startCol());
		assertEquals("                Work with Members Using PDM             ",element.getString());
	}			
	public static FindBy getFinder(String name) throws NoSuchFieldException, SecurityException{
		return TestBy.class.getDeclaredField(name).getAnnotation(FindBy.class);
	}

}
