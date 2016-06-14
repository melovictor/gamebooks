package hu.zagor.gamebooks.mvc.book.section.controller;

import static org.easymock.EasyMock.expect;
import javax.servlet.http.HttpServletRequest;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookWelcomeController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookWelcomeControllerTest {

    private GenericBookWelcomeController underTest;
    private IMocksControl mockControl;
    private HttpServletRequest request;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new GenericBookWelcomeController();
        request = mockControl.createMock(HttpServletRequest.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testHandleNoCommandShouldReturnRedirect() {
        // GIVEN
        expect(request.getRequestURI()).andReturn("/gamebooks/book/7600966005");
        expect(request.getContextPath()).andReturn("/gamebooks");
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleNoCommand(request);
        // THEN
        Assert.assertEquals(returned, "redirect:/book/7600966005/welcome");
    }

    public void testHandleEmptyShouldReturnRedirect() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleEmpty();
        // THEN
        Assert.assertEquals(returned, "redirect:welcome");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
