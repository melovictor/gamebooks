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
import hu.zagor.gamebooks.mvc.book.inventory.domain.ReplaceItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemData;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private GenericBookTakeItemController underTest;
    @MockControl private IMocksControl mockControl;
    private BookInformations info;
    @Mock private HttpServletRequest request;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper sessionWrapper;
    @Mock private GatheredLostItem glItem;
    @Mock private PlayerUser playerUser;
    @Mock private Paragraph paragraph;
    @Inject private Logger logger;
    @Inject private BookContentInitializer contentInitializer;
    @Instance private Character character;
    @Instance private CharacterHandler characterHandler;
    @Mock private CharacterItemHandler itemHandler;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Instance private TakeItemData takeData;
    @Instance private ReplaceItemData replaceData;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);

        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
        Whitebox.setInternalState(underTest, "info", info);

        takeData.setAmount(AMOUNT);
        takeData.setItemId(ITEM_ID);

        replaceData.setLoseId("1001");
        replaceData.setGatherId("1002");
        replaceData.setAmount(1);
    }

    @BeforeMethod
    public void setUpMethod() {
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
        final int returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned, AMOUNT);
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
        final int returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned, 0);
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
        final int returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned, TAKEN_AMOUNT);
    }

    public void testHandleItemReplaceWhenCharacterDoesNotHaveReplacementItemShouldReturnZero() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        itemInteractionRecorder.recordItemReplacing(sessionWrapper, "1002", "1001");
        expect(beanFactory.getBean("gatheredLostItem", "1002", 1, 0, false)).andReturn(glItem);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "1001")).andReturn(false);
        logger.debug("User doesn't have item {}.", "1001");
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleItemReplace(request, replaceData);
        // THEN
        Assert.assertEquals(returned, "0");
    }

    public void testHandleItemReplaceWhenCharacterHasReplacementItemShouldReturnOne() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        itemInteractionRecorder.recordItemReplacing(sessionWrapper, "1002", "1001");
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
        final String returned = underTest.handleItemReplace(request, replaceData);
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
