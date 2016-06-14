package hu.zagor.gamebooks.controller.content;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.contentstorage.BookContentStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultBookContentInitializer}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultBookContentInitializerPositiveTest {

    private static final String WELCOME_ID = BookParagraphConstants.BACK_COVER.getValue();
    private static final String PARAGRAPH_ID = "13";
    private static final String SERIES = "Series";
    private static final String TITLE = "Title";
    private static final Integer PLAYER_ID = 233;
    private static final Long BOOK_ID = 423452345L;
    private static final Boolean SAVED_GAME_STATE = true;
    private static final Boolean MAP_STATE = false;
    private static final Boolean INVENTORY_STATE = true;

    private BookItemStorage itemStorage;
    private DefaultBookContentInitializer underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private PlayerUser adminPlayer;
    private PlayerUser genericPlayer;
    private SavedGameContainer container;
    private GameStateHandler gameStateHandler;
    private BeanFactory beanFactory;
    private Logger logger;
    private BookContentStorage storage;
    private Model model;
    private Paragraph previousParagraph;
    private Paragraph paragraph;
    private GatheredLostItem glItem;
    private BookContentSpecification contentSpecification;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        adminPlayer = new PlayerUser(PLAYER_ID, "FireFoX", true);
        adminPlayer.getSettings().put("global.imageTypeOrder", "bwFirst");
        genericPlayer = new PlayerUser(11, "gnome", false);
        genericPlayer.getSettings().put("global.imageTypeOrder", "colFirst");
        container = mockControl.createMock(SavedGameContainer.class);
        gameStateHandler = mockControl.createMock(GameStateHandler.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        logger = mockControl.createMock(Logger.class);
        storage = mockControl.createMock(BookContentStorage.class);
        model = mockControl.createMock(Model.class);
        previousParagraph = mockControl.createMock(Paragraph.class);
        paragraph = mockControl.createMock(Paragraph.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        itemStorage = mockControl.createMock(BookItemStorage.class);
        paragraphData = new ParagraphData();

        contentSpecification = new BookContentSpecification();
        contentSpecification.setInventoryAvailable(INVENTORY_STATE);
        contentSpecification.setMapAvailable(MAP_STATE);

        info = new BookInformations(BOOK_ID);
        info.setSeries(SERIES);
        info.setTitle(TITLE);
        info.setContentSpecification(contentSpecification);

        underTest = new DefaultBookContentInitializer(storage, gameStateHandler);
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        paragraphData.setText("sample text with an image: <img src=\"resources/book1/99.jpg\" />");
        mockControl.reset();
    }

    public void testInitModelShouldInitializePassedModel() {
        // GIVEN
        expect(model.addAttribute(ControllerAddresses.USER_STORE_KEY, adminPlayer)).andReturn(model);
        expect(model.addAttribute("pageTitle", SERIES + " &ndash; " + TITLE)).andReturn(model);
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID)).andReturn(container);
        expect(gameStateHandler.checkSavedGame(container)).andReturn(SAVED_GAME_STATE);
        expect(model.addAttribute("haveSavedGame", SAVED_GAME_STATE)).andReturn(model);
        expect(model.addAttribute("hasInventory", INVENTORY_STATE)).andReturn(model);
        expect(model.addAttribute("hasMap", MAP_STATE)).andReturn(model);

        mockControl.replay();
        // WHEN
        underTest.initModel(model, adminPlayer, info);
        // THEN
    }

    public void testLoadSectionWhenPreviousParagraphNullShouldReturnNewParagraph() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.loadSection(PARAGRAPH_ID, genericPlayer, null, info);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testLoadSectionWhenUserNeedsBwImageShouldRewriteImageInTextWithBwQuery() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, adminPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"resources/book1/99.jpg?bwFirst\" />");
    }

    public void testLoadSectionWhenUserNeedsColorImageShouldRewriteImageInTextWithColorQuery() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"resources/book1/99.jpg?colFirst\" />");
    }

    public void testLoadSectionWhenNextParagraphIsWelcomeShouldReturnNewParagraph() {
        // GIVEN
        expect(storage.getBookParagraph(info, WELCOME_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.loadSection(WELCOME_ID, genericPlayer, previousParagraph, info);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testLoadSectionWhenNextParagraphIsValidShouldReturnNewParagraph() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(previousParagraph.isValidMove(PARAGRAPH_ID)).andReturn(true);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.loadSection(PARAGRAPH_ID, genericPlayer, previousParagraph, info);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testLoadSectionWhenNextParagraphIsNotValidButUserIsAdminShouldReturnNewParagraph() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(previousParagraph.isValidMove(PARAGRAPH_ID)).andReturn(false);
        expect(previousParagraph.getId()).andReturn(WELCOME_ID);
        logger.debug("Player tried to navigate to illegal section {}.", PARAGRAPH_ID);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.loadSection(PARAGRAPH_ID, adminPlayer, previousParagraph, info);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testValidateItemWhenItemIsValidShouldDoNothing() {
        // GIVEN
        expect(paragraph.isValidItem(glItem)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.validateItem(glItem, genericPlayer, paragraph, info);
        // THEN
    }

    public void testGetItemStorageWhenInfoNotNullShouldReturnStorage() {
        // GIVEN
        expect(storage.getBookEntry(info)).andReturn(itemStorage);
        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.getItemStorage(info);
        // THEN
        Assert.assertSame(returned, itemStorage);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
