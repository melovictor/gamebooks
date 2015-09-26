package hu.zagor.gamebooks.mvc.logout.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link LogoutController}.
 * @author Tamas_Szekeres
 */
@Test
public class LogoutControllerTest {

    private LogoutController underTest;

    private IMocksControl mockControl;
    private HttpSession session;
    private MdcHandler mdcHandler;
    private EnvironmentDetector environmentDetector;
    private PlayerUser user;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        session = mockControl.createMock(HttpSession.class);
        mdcHandler = mockControl.createMock(MdcHandler.class);
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        underTest = new LogoutController();
        Whitebox.setInternalState(underTest, "mdcHandler", mdcHandler);
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
        user = new PlayerUser(3, "FireFoX", true);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testLoginGetShouldCleanUserStore() {
        // GIVEN
        expect(session.getAttribute("user")).andReturn(user);
        logger.info("Logging out user '{}'.", "FireFoX");
        session.invalidate();
        environmentDetector.setSeleniumTesting(false);
        mockControl.replay();
        // WHEN
        final String returned = underTest.loginGet(session);
        // THEN
        Assert.assertEquals(returned, "redirect:login");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
