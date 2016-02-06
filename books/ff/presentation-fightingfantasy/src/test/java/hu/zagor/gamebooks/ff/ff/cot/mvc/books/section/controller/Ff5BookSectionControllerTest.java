package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.BallThrowChallenge;
import hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.WhoThrowsHigherService;
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
 * Unit test for class {@link Ff5BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff5BookSectionControllerTest {

    private IMocksControl mockControl;
    private SectionHandlingService sectionHandler;
    private Ff5BookSectionController underTest;
    private Model model;
    private HttpSessionWrapper wrapper;
    private Paragraph paragraph;

    private WhoThrowsHigherService whoThrowsHigher;
    private BallThrowChallenge ballThrowChallenge;
    private FfCharacter character;
    private ParagraphData data;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandler = mockControl.createMock(SectionHandlingService.class);
        underTest = new Ff5BookSectionController(sectionHandler);
        paragraph = mockControl.createMock(Paragraph.class);
        whoThrowsHigher = mockControl.createMock(WhoThrowsHigherService.class);
        ballThrowChallenge = mockControl.createMock(BallThrowChallenge.class);
        Whitebox.setInternalState(underTest, "whoThrowsHigher", whoThrowsHigher);
        Whitebox.setInternalState(underTest, "ballThrowChallenge", ballThrowChallenge);
        character = mockControl.createMock(FfCharacter.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        data = mockControl.createMock(FfParagraphData.class);
        info = new FfBookInformations(1L);
        Whitebox.setInternalState(underTest, "info", info);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
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
        new Ff5BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testHandleCustomSectionsWhenSectionIsNotSpecialShouldDoNothing() {
        // GIVEN
        final String sectionId = "66";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId).times(2);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenSectionIsWhoThrowsHigherTheFirstTimeShouldRegisterFirstAttemptAndPlayWhoThrowsHigher() {
        // GIVEN
        final String sectionId = "206a";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "42063")).andReturn(false);
        expect(itemHandler.hasItem(character, "42062")).andReturn(false);
        expect(itemHandler.hasItem(character, "42061")).andReturn(false);
        expect(itemHandler.addItem(character, "42061", 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        whoThrowsHigher.playGame(character, data);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenSectionIsWhoThrowsHigherTheSecondTimeShouldRegisterSecondAttemptAndPlayWhoThrowsHigher() {
        // GIVEN
        final String sectionId = "206a";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "42063")).andReturn(false);
        expect(itemHandler.hasItem(character, "42062")).andReturn(false);
        expect(itemHandler.hasItem(character, "42061")).andReturn(true);
        expect(itemHandler.addItem(character, "42062", 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        whoThrowsHigher.playGame(character, data);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenSectionIsWhoThrowsHigherTheThirdTimeShouldRegisterThirdAttemptAndPlayWhoThrowsHigher() {
        // GIVEN
        final String sectionId = "206a";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "42063")).andReturn(false);
        expect(itemHandler.hasItem(character, "42062")).andReturn(true);
        expect(itemHandler.addItem(character, "42063", 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        whoThrowsHigher.playGame(character, data);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenSectionIsWhoThrowsHigherTheFourthTimeShouldRegisterFourthAttemptAndPlayWhoThrowsHigher() {
        // GIVEN
        final String sectionId = "206a";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "42063")).andReturn(true);
        expect(itemHandler.addItem(character, "42064", 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        whoThrowsHigher.playGame(character, data);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenSectionIsThrowTheBallShouldPlayBallThrowChallangeGame() {
        // GIVEN
        final String sectionId = "378";
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn(sectionId).times(2);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        ballThrowChallenge.playGame(character, data);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
