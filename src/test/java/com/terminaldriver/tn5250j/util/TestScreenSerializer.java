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
			"    \"col\" : 10,\r\n" + 
			"    \"length\" : 57,\r\n" + 
			"    \"row\" : 1,\r\n" + 
			"    \"text\" : \"                Work with Members Using PDM\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 70,\r\n" + 
			"    \"length\" : 9,\r\n" + 
			"    \"row\" : 1,\r\n" + 
			"    \"text\" : \"PUB1\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 18,\r\n" + 
			"    \"row\" : 3,\r\n" + 
			"    \"text\" : \"File  . . . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 20,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 3,\r\n" + 
			"    \"text\" : \"QRPGLESRC\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 2,\r\n" + 
			"    \"length\" : 16,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"Library . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 22,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"EBERLYRH1\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 43,\r\n" + 
			"    \"length\" : 23,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"Position to  . . . . .\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 68,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 4,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 6,\r\n" + 
			"    \"text\" : \"Type options, press Enter.\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 73,\r\n" + 
			"    \"row\" : 7,\r\n" + 
			"    \"text\" : \"2=Edit         3=Copy  4=Delete 5=Display       6=Print     7=Rename\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 79,\r\n" + 
			"    \"row\" : 8,\r\n" + 
			"    \"text\" : \"8=Display description  9=Save  13=Change text  14=Compile  15=Create module...\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 79,\r\n" + 
			"    \"row\" : 10,\r\n" + 
			"    \"text\" : \"Opt  Member      Type        Text\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 3,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 5,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"FILLER\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 17,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 29,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 51,\r\n" + 
			"    \"row\" : 11,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 3,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 5,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"POWERLIST\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 17,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 29,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 51,\r\n" + 
			"    \"row\" : 12,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 3,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 5,\r\n" + 
			"    \"field\" : 96,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"TEST\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 17,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 11,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"RPGLE\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 29,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 51,\r\n" + 
			"    \"row\" : 13,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 66,\r\n" + 
			"    \"length\" : 13,\r\n" + 
			"    \"row\" : 19,\r\n" + 
			"    \"text\" : \"      Bottom\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 20,\r\n" + 
			"    \"text\" : \"Parameters or command\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 5,\r\n" + 
			"    \"row\" : 21,\r\n" + 
			"    \"text\" : \"===>\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 5,\r\n" + 
			"    \"field\" : 64,\r\n" + 
			"    \"length\" : 74,\r\n" + 
			"    \"row\" : 21,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"GRN_UL\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 22,\r\n" + 
			"    \"text\" : \"F3=Exit          F4=Prompt             F5=Refresh            F6=Create\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 78,\r\n" + 
			"    \"row\" : 23,\r\n" + 
			"    \"text\" : \"F9=Retrieve      F10=Command entry     F23=More options      F24=More keys\",\r\n" + 
			"    \"attr\" : \"BLU\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 1,\r\n" + 
			"    \"length\" : 77,\r\n" + 
			"    \"row\" : 24,\r\n" + 
			"    \"text\" : \"                                         (C) COPYRIGHT IBM CORP. 1981, 2003.\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  }, {\r\n" + 
			"    \"col\" : 77,\r\n" + 
			"    \"length\" : 2,\r\n" + 
			"    \"row\" : 24,\r\n" + 
			"    \"text\" : \"\",\r\n" + 
			"    \"attr\" : \"WHT\"\r\n" + 
			"  } ],\r\n" + 
			"  \"rows\" : 24\r\n" + 
			"}";
}
