package hu.zagor.gamebooks.ff.ff.toc.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
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
 * Unit test for class {@link Ff21BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff21BookSectionControllerTest {

    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;
    private Model model;
    private HttpSessionWrapper wrapper;
    private FfCharacter character;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private CharacterParagraphHandler paragraphHandler;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
        model = mockControl.createMock(Model.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        character = mockControl.createMock(FfCharacter.class);
        paragraphHandler = mockControl.createMock(CharacterParagraphHandler.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        info = new FfBookInformations(3L);
        characterHandler = new FfCharacterHandler();
        characterHandler.setParagraphHandler(paragraphHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
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
        new Ff21BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenSectionHandlerProvidedShouldCreateObject() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff21BookSectionController(sectionHandler).getClass();
        // THEN
    }

    public void testHandleCustomSectionsWhenRevisitingOldSectionShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff21BookSectionController(sectionHandler).handleCustomSectionsPre(model, wrapper, null, null);
        // THEN
    }

    public void testHandleCustomSectionsWhenSection201HasNotBeenVisitedShouldDoNothing() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraphHandler.visitedParagraph(character, "201")).andReturn(false);
        mockControl.replay();
        final Ff21BookSectionController underTest = new Ff21BookSectionController(sectionHandler);
        Whitebox.setInternalState(underTest, "info", info);
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, "333", null);
        // THEN
    }

    public void testHandleCustomSectionsWhenSection201HasBeenVisitedShouldDecreaseCharacterStaminaByOne() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(paragraphHandler.visitedParagraph(character, "201")).andReturn(true);
        attributeHandler.handleModification(character, "stamina", -1);
        mockControl.replay();
        final Ff21BookSectionController underTest = new Ff21BookSectionController(sectionHandler);
        Whitebox.setInternalState(underTest, "info", info);
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, "333", null);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
