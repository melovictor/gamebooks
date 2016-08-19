package hu.zagor.gamebooks.mvc.book.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.inventory.domain.ReplaceItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    @Instance private TakeItemData takeData;
    @Instance private ReplaceItemData replaceData;
    @Mock private List<Item> itemList;
    @Mock private Item item;
    @Mock private EquipInfo equipInfo;

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

    public void testHandleItemTakeWhenAllItemsTakenShouldRemoveAllItems() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(AMOUNT);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(ITEM_ID, AMOUNT);
        logger.debug("Took {} piece(s) of item {}.", AMOUNT, ITEM_ID);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), AMOUNT);
    }

    public void testHandleItemTakeWhenNoItemsAreTakenShouldNotRemoveAnyItems() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(0);
        logger.debug("Took {} piece(s) of item {}.", 0, ITEM_ID);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 0);
    }

    public void testHandleItemTakeWhenSomeItemsTakenShouldRemoveOnlyTakenItems() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(beanFactory.getBean("gatheredLostItem", ITEM_ID, AMOUNT, 0, false)).andReturn(glItem);
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, ITEM_ID, AMOUNT)).andReturn(TAKEN_AMOUNT);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(ITEM_ID, TAKEN_AMOUNT);
        logger.debug("Took {} piece(s) of item {}.", TAKEN_AMOUNT, ITEM_ID);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.handleItemTake(request, takeData);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), TAKEN_AMOUNT);
    }

    public void testHandleItemReplaceWhenCharacterDoesNotHaveReplacementItemShouldReturnZero() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(beanFactory.getBean("gatheredLostItem", "1002", 1, 0, false)).andReturn(glItem);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "1001")).andReturn(true);

        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(sessionWrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, playerUser, paragraph, info);

        expect(itemHandler.removeItem(character, "1001", 1)).andReturn(itemList);
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

    public void testDropItemWhenItemDoesNotExistsShouldDoNothing() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, "3001")).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.dropItem(request, "3001");
        // THEN
    }

    public void testDropItemWhenItemIsNotRemovableShouldDoNothing() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, "3001")).andReturn(item);
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isRemovable()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.dropItem(request, "3001");
        // THEN
    }

    public void testDropItemWhenItemIsRemovableShouldRemoveItem() {
        // GIVEN
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(sessionWrapper);
        expect(sessionWrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, "3001")).andReturn(item);
        expect(item.getEquipInfo()).andReturn(equipInfo);
        expect(equipInfo.isRemovable()).andReturn(true);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        mockControl.replay();
        // WHEN
        underTest.dropItem(request, "3001");
        // THEN
    }

}
