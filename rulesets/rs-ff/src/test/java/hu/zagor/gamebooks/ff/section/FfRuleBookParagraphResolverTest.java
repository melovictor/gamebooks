package hu.zagor.gamebooks.ff.section;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfRuleBookParagraphResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FfRuleBookParagraphResolverTest {

    private FfRuleBookParagraphResolver underTest;
    private IMocksControl mockControl;
    private FfParagraphData paragraphData;
    private FfCharacter character;
    private FfCharacterItemHandler itemHandler;
    private List<GatheredLostItem> lostItems;
    private GatheredLostItem eqWpn;
    private FfItem item;
    private Logger logger;
    private GatheredLostItem normalItem;
    private GatheredLostItem doseFromNormalItem;
    private GatheredLostItem doseFromPotion;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FfRuleBookParagraphResolver();
        character = mockControl.createMock(FfCharacter.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        paragraphData = mockControl.createMock(FfParagraphData.class);
        lostItems = new ArrayList<>();
        item = mockControl.createMock(FfItem.class);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);

        eqWpn = new GatheredLostItem("equippedWeapon", 1, 0, false);
        normalItem = new GatheredLostItem("3005", 1, 0, false);
        doseFromNormalItem = new GatheredLostItem("3005", 0, 1, false);
        doseFromPotion = new GatheredLostItem("2003", 0, 1, false);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        lostItems.clear();
    }

    public void testLoseItemsWhenTryingToLoseEquippedWeaponShouldRemoveEquippedWeapon() {
        // GIVEN
        lostItems.add(eqWpn);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "equippedWeapon");
        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1003");
        itemHandler.removeItem(character, "1003", 1);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN
        Assert.assertTrue(lostItems.isEmpty());
    }

    public void testLoseItemsWhenTryingToLoseNormalItemShouldRemoveNormalItem() {
        // GIVEN
        lostItems.add(normalItem);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "3005");
        itemHandler.removeItem(character, normalItem);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN
        Assert.assertTrue(lostItems.isEmpty());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoseItemsWhenTryingToLoseDosesFromNormalItemShouldThrowException() {
        // GIVEN
        lostItems.add(doseFromNormalItem);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "3005");
        expect(itemHandler.getItem(character, "3005")).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.common);
        expect(item.getName()).andReturn("Ruby");
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN throws exception
    }

    public void testLoseItemsWhenTryingToLoseDosesFromPotionsAndHasLeftoverShouldReduceDoseOnItem() {
        // GIVEN
        lostItems.add(doseFromPotion);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "2003");
        expect(itemHandler.getItem(character, "2003")).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.potion);
        expect(item.getDose()).andReturn(3).times(2);
        item.setDose(2);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN
        Assert.assertTrue(lostItems.isEmpty());
    }

    public void testLoseItemsWhenTryingToLoseDosesFromPotionsAndHasNoLeftoverShouldRemoveItem() {
        // GIVEN
        lostItems.add(doseFromPotion);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "2003");
        expect(itemHandler.getItem(character, "2003")).andReturn(item);
        expect(item.getItemType()).andReturn(ItemType.potion);
        expect(item.getDose()).andReturn(1);
        itemHandler.removeItem(character, "2003", 1);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN
        Assert.assertTrue(lostItems.isEmpty());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
