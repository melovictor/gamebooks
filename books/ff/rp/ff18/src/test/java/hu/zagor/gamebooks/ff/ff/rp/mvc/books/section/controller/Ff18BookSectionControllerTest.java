package hu.zagor.gamebooks.ff.ff.rp.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.ui.Model;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff18BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff18BookSectionControllerTest {

    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;
    private Ff18BookSectionController underTest;
    private Model model;
    private HttpSessionWrapper wrapper;
    private Paragraph paragraph;
    private FfCharacter character;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
        underTest = new Ff18BookSectionController(sectionHandler);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(FfCharacter.class);
        paragraph = mockControl.createMock(Paragraph.class);

        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);
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
        new Ff18BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testHandleCustomSectionsWhenNotSpecialSectionShouldDoNothing() {
        // GIVEN
        expect(paragraph.getId()).andReturn("100");
        mockControl.replay();
        // WHEN
        underTest.handleCustomSections(model, wrapper, "100", paragraph);
        // THEN
    }

    public void testHandleCustomSectionsWhen374ShouldHealHalfStamina() {
        // GIVEN
        expect(paragraph.getId()).andReturn("374");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getStamina()).andReturn(5);
        attributeHandler.handleModification(character, "stamina", 2);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSections(model, wrapper, "374", paragraph);
        // THEN
    }

    public void testHandleCustomSectionsWhen191ShouldHealHalfStamina() {
        // GIVEN
        expect(paragraph.getId()).andReturn("191");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getStamina()).andReturn(5);
        attributeHandler.handleModification(character, "stamina", 2);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSections(model, wrapper, "191", paragraph);
        // THEN
    }

    public void testHandleCustomSectionsWhen78ShouldHealHalfStamina() {
        // GIVEN
        expect(paragraph.getId()).andReturn("78");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getStamina()).andReturn(5);
        attributeHandler.handleModification(character, "stamina", 2);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSections(model, wrapper, "78", paragraph);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
