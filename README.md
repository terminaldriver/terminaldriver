# terminaldriver
A screen driver for junit automation.
Provides an implementation of TerminalDriver to control a 5250 session.  Provides annotations to facilitate creation of 'Page Objects'.


To use with maven:

      <dependencies>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.12</version>
          <scope>test</scope>
        </dependency>
	      <dependency>
    	    <groupId>com.terminaldriver.tn5250j</groupId>
	        <artifactId>terminaldriver-tn5250j</artifactId>
    	    <version>0.0.1-SNAPSHOT</version>
	      </dependency>
	      <dependency>
		      <groupId>org.projectlombok</groupId>
	    	  <artifactId>lombok</artifactId>
		      <version>1.16.8</version>
		      <scope>provided</scope>
	      </dependency>    
      </dependencies>
      
  Use lombok (optional) to reduce the amount of boiler plate.
  
  
  Example code:
  
    @Test
     public void testApp() throws Exception
    {
	 
        TerminalDriver driver = new TerminalDriver();
        driver.connectTo("server", 23);
        driver.dumpScreen();
		
		    Login login = ScreenObjectFactory.createPage(Login.class, driver);
		}
		
		
		@IdentifyBy(
		{	@FindBy(text=">> Welcome to the best known public AS/400| <<",row=1)	}
)

    @Data
    public class Login {

	    TerminalDriver driver;
	
	    @FindBy
	    ScreenField userid;
	
	    @FindBy
	    ScreenField password;
	
    	@FindBy(row=3,attribute=ScreenAttribute.BLU)
	    ScreenTextBlock systemName;

	    public MainMenu login(final String userid, final String password){
		    this.userid.setString(userid);
		    this.password.setString(password);
		    driver.keys().enter();
		    driver.waitForScreen(5000);
		    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
	   }
	 	  MainMenu mainMenu = new MainMenu();
	  	 ScreenObjectFactory.initElements(mainMenu, driver);
  		 return mainMenu;
    	}
    }
