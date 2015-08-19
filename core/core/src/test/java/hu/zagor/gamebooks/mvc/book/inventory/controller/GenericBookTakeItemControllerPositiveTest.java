package hu.zagor.gamebooks.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookTakeItemControllerPositiveTest {

    private static final String ITEM_ID = "3001";
    private static final int AMOUNT = 5;
    private static final int TAKEN_AMOUNT = 3;
    private GenericBookTakeItemController underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private HttpServletRequest request;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper sessionWrapper;
    private GatheredLostItem glItem;
    private PlayerUser playerUser;
    private Paragraph paragraph;
    private Logger logger;
    private BookContentInitializer contentInitializer;
    private Character character;
    private CharacterHandler characterHandler;
    private CharacterItemHandler itemHandler;
    private ItemInteractionRecorder itemInteractionRecorder;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        sessionWrapper = mockControl.createMock(HttpSessionWrapper.class);
        glItem = mockControl.createMock(GatheredLostItem.class);
        playerUser = mockControl.createMock(PlayerUser.class);
        paragraph = mockControl.createMock(Paragraph.class);
        logger = mockControl.createMock(Logger.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        character = new Character();
        characterHandler = new CharacterHandler();
        itemHandler = mockControl.createMock(CharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        itemInteractionRecorder = mockControl.createMock(ItemInteractionRecorder.class);

        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new GenericBookTakeItemController();
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "itemInteractionRecorder", itemInteractionRecorder);
        Whitebox.setInternalState(underTest, "info", info);
        mockControl.reset();
    }

    public void testHandleItemTakeWhenAllItemsTakenShouldRemoveAllItems() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(AMOUNT);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(ITEM_ID, AMOUNT);
        logger.debug("Took {} piece(s) of item {}.", AMOUNT, ITEM_ID);
        itemInteractionRecorder.recordItemTaking(sessionWrapper, ITEM_ID);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemTake(request, ITEM_ID, AMOUNT);
        // THEN
        Assert.assertEquals(returned, String.valueOf(AMOUNT));
    }

    public void testHandleItemTakeWhenNoItemsAreTakenShouldNotRemoveAnyItems() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(0);
        logger.debug("Took {} piece(s) of item {}.", 0, ITEM_ID);
        itemInteractionRecorder.recordItemTaking(sessionWrapper, ITEM_ID);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemTake(request, ITEM_ID, AMOUNT);
        // THEN
        Assert.assertEquals(returned, String.valueOf(0));
    }

    public void testHandleItemTakeWhenSomeItemsTakenShouldRemoveOnlyTakenItems() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(TAKEN_AMOUNT);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(ITEM_ID, TAKEN_AMOUNT);
        logger.debug("Took {} piece(s) of item {}.", TAKEN_AMOUNT, ITEM_ID);
        itemInteractionRecorder.recordItemTaking(sessionWrapper, ITEM_ID);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemTake(request, ITEM_ID, AMOUNT);
        // THEN
        Assert.assertEquals(returned, String.valueOf(TAKEN_AMOUNT));
    }

    public void testHandleItemReplaceWhenCharacterDoesNotHaveReplacementItemShouldReturnZero() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", "1002", 1, 0, false)).andReturn(glItem);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "1001")).andReturn(false);
        logger.debug("User doesn't have item {}.", "1001");
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemReplace(request, "1001", "1002", 1);
        // THEN
        Assert.assertEquals(returned, "0");
    }

    public void testHandleItemReplaceWhenCharacterHasReplacementItemShouldReturnOne() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(beanFactory.getBean("gatheredLostItem", "1002", 1, 0, false)).andReturn(glItem);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "1001")).andReturn(true);

        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);

        itemHandler.removeItem(character, "1001", 1);
        expect(itemHandler.addItem(character, "1002", 1)).andReturn(1);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem("1002", 1);
        logger.debug("Took {} piece(s) of item {} in place of item {}.", 1, "1002", "1001");

        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemReplace(request, "1001", "1002", 1);
        // THEN
        Assert.assertEquals(returned, "1");
    }

    public void testGetItemInteractionRecorderShouldReturnRecorder() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final ItemInteractionRecorder returned = underTest.getItemInteractionRecorder();
        // THEN
        Assert.assertSame(returned, itemInteractionRecorder);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
