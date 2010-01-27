import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public abstract class AbstractTest extends SeleneseTestCase
{
    protected Selenium s;
	
	@Override
	public void setUp() throws Exception {
    	super.setUp("http://localhost:9000/", "*firefox");
    	s = selenium;
	}
    
    protected void loginIntoCMS() {
        s.open("http://localhost:9000/cms");
        s.type("//input[@id='loginEmail']", "admin@test.com");
        s.type("//input[@id='loginPassword']", "admin");
        s.click("//input[@value='Login']");
        s.waitForPageToLoad("20000");
        assertEquals("Vosao", s.getText("//a[@href='/cms']"));
    }
    
}
