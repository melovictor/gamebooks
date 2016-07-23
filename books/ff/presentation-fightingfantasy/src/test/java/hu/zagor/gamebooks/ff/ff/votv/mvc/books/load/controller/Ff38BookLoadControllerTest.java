package hu.zagor.gamebooks.ff.ff.votv.mvc.books.load.controller;

import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38BookLoadController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38BookLoadControllerTest {

    private Ff38BookLoadController underTest;
    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;
    private BeanFactory beanFactory;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest = new Ff38BookLoadController(sectionHandler);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff38BookLoadController(null).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenSectionHandlerProvidedShouldCreateObject() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff38BookLoadController(sectionHandler).getClass();
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
