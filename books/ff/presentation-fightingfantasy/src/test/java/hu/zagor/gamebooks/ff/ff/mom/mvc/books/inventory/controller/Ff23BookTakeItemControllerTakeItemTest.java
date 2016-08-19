package hu.zagor.gamebooks.ff.ff.mom.mvc.books.inventory.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
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
 * Unit test for class {@link Ff23BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BookTakeItemControllerTakeItemTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff23BookTakeItemController underTest;
    @Mock private HttpServletRequest request;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    @Mock private FfCharacter character;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfParagraphData data;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem item;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private GatheredLostItem glItem;
    @Mock private PlayerUser player;
    @Inject private BookContentInitializer contentInitializer;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setCanEatEverywhere(true);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    public void testDoHandleItemTakeWhenNormalItemShouldTakeItemNormally() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expectTakeItemNormally("3001");
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "3001", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenWaterFlaskShouldResetProvisionBonusAndTakeItemNormally() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItems(character, "2000")).andReturn(Arrays.asList((Item) item));
        item.setStamina(4);
        expect(wrapper.getCharacter()).andReturn(character);
        expectTakeItemNormally("3004");
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "3004", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenBuyingFoodAndHaveMoneyShouldDeductGoldAndIncreaseStamina() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(3).times(2);
        character.setGold(2);
        attributeHandler.handleModification(character, "stamina", 2);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "4003", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenBuyingFoodAndNotHaveMoneyShouldDoNothingAndReturnWithZero() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getGold()).andReturn(0);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "4003", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 0);
    }

    public void testDoHandleItemTakeWhenResettingSkillShouldResetSkill() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        character.changeSkill(12);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "4006", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenResettingStaminaShouldResetStamina() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        character.changeStamina(24);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "4007", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenResettingLuckShouldResetLuck() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        character.changeLuck(12);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "4008", 1);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 1);
    }

    public void testDoHandleItemTakeWhenGetchingGoldPiecesAtSection304ShouldTakeGoldDeductStaminaAndReturnWithTwo() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("304");
        expect(character.getGold()).andReturn(3);
        character.setGold(5);
        character.changeStamina(-1);
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "gold", 10);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 2);
    }

    public void testDoHandleItemTakeWhenGetchingGoldPiecesAtSectionsOtherThan304ShouldTakeItemsNormally() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("253");
        expectTakeGoldNormally();
        mockControl.replay();
        // WHEN
        final TakeItemResponse returned = underTest.doHandleItemTake(request, "gold", 3);
        // THEN
        Assert.assertEquals(returned.getTotalTaken(), 3);
    }

    private void expectTakeItemNormally(final String itemId) {
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expect(itemHandler.resolveItem(itemId)).andReturn(item);
        expect(item.getActions()).andReturn(1);
        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, 1, 0, false)).andReturn(glItem);
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, 1)).andReturn(1);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, 1);
        logger.debug("Took {} piece(s) of item {}.", 1, itemId);
        paragraph.setActions(9);
    }

    private void expectTakeGoldNormally() {
        final String itemId = "gold";
        final int amount = 3;
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getActions()).andReturn(10);
        expectWrapper();
        expect(beanFactory.getBean("gatheredLostItem", itemId, amount, 0, false)).andReturn(glItem);
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        contentInitializer.validateItem(glItem, player, paragraph, info);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, itemId, amount)).andReturn(amount);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        paragraph.removeValidItem(itemId, amount);
        logger.debug("Took {} piece(s) of item {}.", amount, itemId);
        paragraph.setActions(9);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
