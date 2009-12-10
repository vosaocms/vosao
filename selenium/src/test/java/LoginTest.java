
public class LoginTest extends AbstractTest
{
    public void testLogin() throws Exception {
        s.open("http://localhost:9000/login.jsp");
        s.type("//input[@id='loginEmail']", "admin@test.com");
        s.type("//input[@id='loginPassword']", "admin");
        s.click("//input[@value='Login']");
        s.waitForPageToLoad("20000");
        assertEquals("Vosao", s.getText("//a[@href='/cms']"));
    }

    public void testLoginCms() throws Exception {
        s.open("http://localhost:9000/cms");
        s.type("//input[@id='loginEmail']", "admin@test.com");
        s.type("//input[@id='loginPassword']", "admin");
        s.click("//input[@value='Login']");
        s.waitForPageToLoad("20000");
        assertEquals("Vosao", s.getText("//a[@href='/cms']"));
        s.click("//a[text()='Logout']");
        s.waitForPageToLoad("20000");
        s.open("http://localhost:9000/cms");
        assertTrue(s.isElementPresent("//input[@value='Login']"));
    }
    
}
