package hu.zagor.gamebooks.mvc.logout.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
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

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        session = mockControl.createMock(HttpSession.class);
        mdcHandler = mockControl.createMock(MdcHandler.class);
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        underTest = new LogoutController();
        Whitebox.setInternalState(underTest, "mdcHandler", mdcHandler);
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testLoginGetShouldCleanUserStore() {
        // GIVEN
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, null);
        mdcHandler.cleanUserId(session);
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
