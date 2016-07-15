package com.terminaldriver.tn5250j.util;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.terminaldriver.tn5250j.TerminalDriver;
import com.terminaldriver.tn5250j.mock.MockScreenUtil;

public class TestScreenSerializer {

	TerminalDriver driver;
	int currentPosition;
	ScreenSerializer serializer;
	
	@Before
	public void setUp() throws Exception{
		final InputStream data = getClass().getResourceAsStream("/com/terminaldriver/tn5250j/util/wrkmbrpdm.json");
		driver = MockScreenUtil.createTestDriver(data);
		serializer = new ScreenSerializer(driver.getSession().getScreen());
	}
	
	@Test
	public void testSerialize() throws Exception{
		String result = serializer.serialize(driver.getSession().getScreen(),true);
		assertEquals(expectedJson,result);
		
	}
	
	String expectedJson = "{\r\n" + 
			"  \"columns\" : 80,\r\n" + 
			"  \"fields\" : [ {\r\n" + 
			"    \"col\" : 12,\r\n" + 
			"    \"length\" : 57,\r\n" + 
			"    \"row\" : 1,\r\n" + 
			"    \"text\" : \"                Work with Members Using PDM\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 72,\r\n" + 
			"    \"length\" : 9,\r\n" + 
			"    \"row\" : 1,\r\n" + 
			"    \"text\" : \"PUB1\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 18,\r\n" + 
			"    \"row\" : 3,\r\n" + 
			"    \"text\" : \"File  . . . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 22,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 3,\r\n" + 
			"    \"text\" : \"QRPGLESRC\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 4,\r\n" + 
			"    \"length\" : 16,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"Library . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 24,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"EBERLYRH1\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 45,\r\n" + 
			"    \"length\" : 23,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"Position to  . . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 70,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 6,\r\n" + 
			"    \"text\" : \"Type options, press Enter.\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 3,\r\n" + 
			"    \"length\" : 73,\r\n" + 
			"    \"row\" : 7,\r\n" + 
			"    \"text\" : \"2=Edit         3=Copy  4=Delete 5=Display       6=Print     7=Rename\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 3,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 8,\r\n" + 
			"    \"text\" : \"8=Display description  9=Save  13=Change text  14=Compile  15=Create module...\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 79,\r\n" + 
			"    \"row\" : 10,\r\n" + 
			"    \"text\" : \"Opt  Member      Type        Text\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 2,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 7,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"FILLER\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 19,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 31,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 49,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 2,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 7,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"POWERLIST\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 19,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 31,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 49,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 2,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 7,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"TEST\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 19,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 10,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 31,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 49,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 68,\r\n" + 
			"    \"length\" : 13,\r\n" + 
			"    \"row\" : 19,\r\n" + 
			"    \"text\" : \"      Bottom\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 20,\r\n" + 
			"    \"text\" : \"Parameters or command\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 5,\r\n" + 
			"    \"row\" : 21,\r\n" + 
			"    \"text\" : \"===>\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 7,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 73,\r\n" + 
			"    \"row\" : 21,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 22,\r\n" + 
			"    \"text\" : \"F3=Exit          F4=Prompt             F5=Refresh            F6=Create\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 23,\r\n" + 
			"    \"text\" : \"F9=Retrieve      F10=Command entry     F23=More options      F24=More keys\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 77,\r\n" + 
			"    \"row\" : 24,\r\n" + 
			"    \"text\" : \"                                         (C) COPYRIGHT IBM CORP. 1981, 2003.\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 79,\r\n" + 
			"    \"length\" : 2,\r\n" + 
			"    \"row\" : 24,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  } ],\r\n" + 
			"  \"rows\" : 24\r\n" + 
			"}";
	
	
	@Test
	public void testSerializeXML() throws Exception{
		String result = serializer.serializeXML(driver.getSession().getScreen());
		assertEquals(expectedXml,result);
		
	}
	
	final String expectedXml ="<screen>\r\n" + 
			"  <field text=\"                Work with Members Using PDM\" row=\"1\" col=\"12\" attr=\"WHT\" length=\"57\"/>\r\n" + 
			"  <field text=\"PUB1\" row=\"1\" col=\"72\" length=\"9\"/>\r\n" + 
			"  <field text=\"File  . . . . . .\" row=\"3\" col=\"2\" length=\"18\"/>\r\n" + 
			"  <field text=\"QRPGLESRC\" row=\"3\" col=\"22\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"Library . . . .\" row=\"4\" col=\"4\" length=\"16\"/>\r\n" + 
			"  <field text=\"EBERLYRH1\" row=\"4\" col=\"24\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"Position to  . . . . .\" row=\"4\" col=\"45\" length=\"23\"/>\r\n" + 
			"  <field text=\"\" row=\"4\" col=\"70\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"Type options, press Enter.\" row=\"6\" col=\"2\" attr=\"BLU\" length=\"78\"/>\r\n" + 
			"  <field text=\"2=Edit         3=Copy  4=Delete 5=Display       6=Print     7=Rename\" row=\"7\" col=\"3\" attr=\"BLU\" length=\"73\"/>\r\n" + 
			"  <field text=\"8=Display description  9=Save  13=Change text  14=Compile  15=Create module...\" row=\"8\" col=\"3\" attr=\"BLU\" length=\"78\"/>\r\n" + 
			"  <field text=\"Opt  Member      Type        Text\" row=\"10\" col=\"2\" attr=\"WHT\" length=\"79\"/>\r\n" + 
			"  <field text=\"\" row=\"11\" col=\"2\" attr=\"GRN_UL\" field=\"64\" length=\"2\"/>\r\n" + 
			"  <field text=\"FILLER\" row=\"11\" col=\"7\" field=\"96\" length=\"10\"/>\r\n" + 
			"  <field text=\"RPGLE\" row=\"11\" col=\"19\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"\" row=\"11\" col=\"31\" attr=\"GRN_UL\" field=\"64\" length=\"49\"/>\r\n" + 
			"  <field text=\"\" row=\"12\" col=\"2\" attr=\"GRN_UL\" field=\"64\" length=\"2\"/>\r\n" + 
			"  <field text=\"POWERLIST\" row=\"12\" col=\"7\" field=\"96\" length=\"10\"/>\r\n" + 
			"  <field text=\"RPGLE\" row=\"12\" col=\"19\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"\" row=\"12\" col=\"31\" attr=\"GRN_UL\" field=\"64\" length=\"49\"/>\r\n" + 
			"  <field text=\"\" row=\"13\" col=\"2\" attr=\"GRN_UL\" field=\"64\" length=\"2\"/>\r\n" + 
			"  <field text=\"TEST\" row=\"13\" col=\"7\" field=\"96\" length=\"10\"/>\r\n" + 
			"  <field text=\"RPGLE\" row=\"13\" col=\"19\" attr=\"GRN_UL\" field=\"64\" length=\"10\"/>\r\n" + 
			"  <field text=\"\" row=\"13\" col=\"31\" attr=\"GRN_UL\" field=\"64\" length=\"49\"/>\r\n" + 
			"  <field text=\"      Bottom\" row=\"19\" col=\"68\" attr=\"WHT\" length=\"13\"/>\r\n" + 
			"  <field text=\"Parameters or command\" row=\"20\" col=\"2\" length=\"78\"/>\r\n" + 
			"  <field text=\"===&gt;\" row=\"21\" col=\"2\" length=\"5\"/>\r\n" + 
			"  <field text=\"\" row=\"21\" col=\"7\" attr=\"GRN_UL\" field=\"64\" length=\"73\"/>\r\n" + 
			"  <field text=\"F3=Exit          F4=Prompt             F5=Refresh            F6=Create\" row=\"22\" col=\"2\" attr=\"BLU\" length=\"78\"/>\r\n" + 
			"  <field text=\"F9=Retrieve      F10=Command entry     F23=More options      F24=More keys\" row=\"23\" col=\"2\" attr=\"BLU\" length=\"78\"/>\r\n" + 
			"  <field text=\"                                         (C) COPYRIGHT IBM CORP. 1981, 2003.\" row=\"24\" col=\"2\" attr=\"WHT\" length=\"77\"/>\r\n" + 
			"  <field text=\"\" row=\"24\" col=\"79\" attr=\"WHT\" length=\"2\"/>\r\n" + 
			"</screen>\r\n";
}
