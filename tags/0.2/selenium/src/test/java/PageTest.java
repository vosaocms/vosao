
public class PageTest extends AbstractTest
{
    public void testPageAdd() throws Exception {
        loginIntoCMS();
        s.click("//a[@href='/cms/pages.jsp']");
        s.waitForPageToLoad("20000");
        waitForPagesLoaded();
        s.click("//ul/li/a[text()='+']");
        s.waitForPageToLoad("20000");
        s.type("//input[@id='title']", "About");
        s.type("//input[@id='friendlyUrl']", "/about");
        s.click("//input[@id='pageSaveButton']");
        s.waitForPageToLoad("20000");
        waitForPagesLoaded();
        assertTrue(s.isTextPresent("About"));
    }
    
    private void waitForPagesLoaded() {
        s.waitForCondition("selenium.browserbot.getCurrentWindow()." +
        		"$('#pages-tree ul li').size() > 0", "30000");
    }
}
