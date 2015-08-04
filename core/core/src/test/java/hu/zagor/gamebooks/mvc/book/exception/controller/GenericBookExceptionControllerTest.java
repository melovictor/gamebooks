package hu.zagor.gamebooks.mvc.book.exception.controller;

import hu.zagor.gamebooks.exception.InvalidStepChoiceException;

import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookExceptionController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookExceptionControllerTest {

    private GenericBookExceptionController underTest;
    private IMocksControl mockControl;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);

        underTest = new GenericBookExceptionController();
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    public void testErrorHandlerWhenExceptionReceivedShouldReturnProperlyConfiguredMav() {
        // GIVEN
        final Exception exception = new Exception();
        logger.warn("Book error handler kicked in!", exception);
        mockControl.replay();
        // WHEN
        final ModelAndView returned = underTest.errorHandler(exception);
        // THEN
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.get("problemCode"), "page.book.error.unexpected");
        Assert.assertEquals(model.get("isErrorPage"), true);
    }

    public void testErrorHandlerWhenInvalidStepChoiceExceptionReceivedShouldReturnProperlyConfiguredMav() {
        // GIVEN
        final InvalidStepChoiceException exception = new InvalidStepChoiceException("111", 0);
        logger.warn("Book error handler kicked in!", exception);
        mockControl.replay();
        // WHEN
        final ModelAndView returned = underTest.errorHandler(exception);
        // THEN
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.get("problemCode"), "page.book.error.invalidstep");
        Assert.assertEquals(model.get("isErrorPage"), true);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
