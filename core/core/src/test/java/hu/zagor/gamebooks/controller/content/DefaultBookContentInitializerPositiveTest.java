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
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Locale;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
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

    private DefaultBookContentInitializer underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private BookItemStorage itemStorage;
    private BookInformations info;
    private PlayerUser adminPlayer;
    private PlayerUser genericPlayer;
    @Mock private SavedGameContainer container;
    @Mock private GameStateHandler gameStateHandler;
    @Inject private BeanFactory beanFactory;
    @Inject private Logger logger;
    @Mock private BookContentStorage storage;
    @Mock private Model model;
    @Mock private Paragraph previousParagraph;
    @Mock private Paragraph paragraph;
    @Mock private GatheredLostItem glItem;
    @Instance private BookContentSpecification contentSpecification;
    @Instance private ParagraphData paragraphData;
    @Inject private LocaleProvider localeProvider;

    @BeforeClass
    public void setUpClass() {
        adminPlayer = new PlayerUser(PLAYER_ID, "FireFoX", true);
        adminPlayer.getSettings().put("global.imageTypeOrder", "bwFirst");
        genericPlayer = new PlayerUser(11, "gnome", false);
        genericPlayer.getSettings().put("global.imageTypeOrder", "colFirst");

        contentSpecification.setInventoryAvailable(INVENTORY_STATE);
        contentSpecification.setMapAvailable(MAP_STATE);

        info = new BookInformations(BOOK_ID);
        info.setSeries(SERIES);
        info.setTitle(TITLE);
        info.setContentSpecification(contentSpecification);
    }

    @UnderTest
    public DefaultBookContentInitializer underTest() {
        return new DefaultBookContentInitializer(storage, gameStateHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        paragraphData.setText("sample text with an image: <img src=\"resources/book1/99.jpg\" />");
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
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
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
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, adminPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"http://zagor.hu/gamebooks/img.php?book=book1&type=b&img=99&loc=en\" />");
    }

    public void testLoadSectionWhenUserNeedsColorImageShouldRewriteImageInTextWithColorQuery() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"http://zagor.hu/gamebooks/img.php?book=book1&type=c&img=99&loc=en\" />");
    }

    public void testLoadSectionWhenRulesetImageShouldLeaveItAlone() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
        paragraphData.setText("sample text with an image: <img src=\"resources/ff/dice.jpg\" />");
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"resources/ff/dice.jpg?colFirst\" />");
    }

    public void testLoadSectionWhenCoreImageShouldLeaveItAlone() {
        // GIVEN
        expect(storage.getBookParagraph(info, PARAGRAPH_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
        paragraphData.setText("sample text with an image: <img src=\"../resources/dice.jpg\" />");
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, genericPlayer, null, info);
        // THEN
        Assert.assertEquals(paragraphData.getText(), "sample text with an image: <img src=\"../resources/dice.jpg?colFirst\" />");
    }

    public void testLoadSectionWhenNextParagraphIsWelcomeShouldReturnNewParagraph() {
        // GIVEN
        expect(storage.getBookParagraph(info, WELCOME_ID)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(paragraphData);
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
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
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
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
        expect(localeProvider.getLocale()).andReturn(new Locale("en"));
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

}
