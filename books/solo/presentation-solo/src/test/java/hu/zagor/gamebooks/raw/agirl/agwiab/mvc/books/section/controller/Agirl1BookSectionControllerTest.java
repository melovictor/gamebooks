package hu.zagor.gamebooks.raw.agirl.agwiab.mvc.books.section.controller;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Agirl1BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Agirl1BookSectionControllerTest {

    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;
    private Agirl1BookSectionController underTest;
    private Paragraph paragraph;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
        underTest = new Agirl1BookSectionController(sectionHandler);
        paragraph = mockControl.createMock(Paragraph.class);
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
        new Agirl1BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testResolveChoiceDisplayNamesShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveChoiceDisplayNames(paragraph);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
