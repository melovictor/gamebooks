package hu.zagor.gamebooks.raw.cyoar.sw.mvc.books.section.controller;

import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Cyoar23BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Cyoar23BookSectionControllerTest {

    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
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
        new Cyoar23BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenSectionHandlerProvidedShouldCreateObject() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Cyoar23BookSectionController(sectionHandler).getClass();
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
