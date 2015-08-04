package hu.zagor.gamebooks.controller.content;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.BookContentStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidGatheredItemException;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.player.PlayerUser;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.ui.Model;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultBookContentInitializer}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultBookContentInitializerNegativeTest {

    private static final String WELCOME_ID = BookParagraphConstants.BACK_COVER.getValue();
    private static final String PARAGRAPH_ID = "13";
    private static final Integer PLAYER_ID = 233;
    private static final String ITEM_ID = "3001";
    private DefaultBookContentInitializer underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private PlayerUser genericPlayer;
    private PlayerUser adminPlayer;
    private GameStateHandler gameStateHandler;
    private Logger logger;
    private BookContentStorage storage;
    private Model model;
    private Paragraph previousParagraph;
    private Paragraph paragraph;
    private GatheredLostItem glItem;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        genericPlayer = new PlayerUser(11, "gnome", false);
        gameStateHandler = mockControl.createMock(GameStateHandler.class);
        logger = mockControl.createMock(Logger.class);
        storage = mockControl.createMock(BookContentStorage.class);
        model = mockControl.createMock(Model.class);
        previousParagraph = mockControl.createMock(Paragraph.class);
        paragraph = mockControl.createMock(Paragraph.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        paragraphData = new ParagraphData();
        adminPlayer = new PlayerUser(PLAYER_ID, "FireFoX", true);
        adminPlayer.getSettings().put("global.imageTypeOrder", "bwFirst");

        info = new BookInformations(1L);

        underTest = new DefaultBookContentInitializer(storage, gameStateHandler);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @BeforeMethod
    public void setUpMethod() {
        paragraphData.setText("sample text with an image: <img src=\"resources/book1/99.jpg\" />");
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenStorageIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultBookContentInitializer(null, gameStateHandler).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenGameStateHandlerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultBookContentInitializer(storage, null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenModelIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(null, genericPlayer, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenPlayerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(model, null, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(model, genericPlayer, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSectionWhenParagraphIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSection(null, genericPlayer, previousParagraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSectionWhenPlayerIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, null, previousParagraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSectionWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, previousParagraph, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateItemWhenGlItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.validateItem(null, genericPlayer, paragraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateItemWhenPlayerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, null, paragraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateItemWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, genericPlayer, null, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateItemWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, genericPlayer, paragraph, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = InvalidStepChoiceException.class)
    public void testLoadSectionWhenNextParagraphIsNotValidAndUserIsNotAdminShouldThrowException() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(previousParagraph.isValidMove(PARAGRAPH_ID)).andReturn(false);
        expect(previousParagraph.getId()).andReturn(WELCOME_ID);
        logger.debug("Player tried to navigate to illegal section {}.", PARAGRAPH_ID);
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, previousParagraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = InvalidGatheredItemException.class)
    public void testValidateItemWhenItemIsValidAndPlayerIsNotAdminShouldThrowException() {
        // GIVEN
        expect(paragraph.isValidItem(glItem)).andReturn(false);
        expect(glItem.getId()).andReturn(ITEM_ID);
        expect(paragraph.getId()).andReturn(PARAGRAPH_ID);
        expect(glItem.getId()).andReturn(ITEM_ID);
        logger.debug("Player tried to collect item {}", ITEM_ID);
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, genericPlayer, paragraph, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetItemStorageWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getItemStorage(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = InvalidGatheredItemException.class)
    public void testValidateItemWhenItemIsValidAndPlayerIsAdminShouldThrowException() {
        // GIVEN
        expect(paragraph.isValidItem(glItem)).andReturn(false);
        expect(glItem.getId()).andReturn(ITEM_ID);
        expect(paragraph.getId()).andReturn(PARAGRAPH_ID);
        expect(glItem.getId()).andReturn(ITEM_ID);
        logger.debug("Player tried to collect item {}", ITEM_ID);
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, adminPlayer, paragraph, info);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
