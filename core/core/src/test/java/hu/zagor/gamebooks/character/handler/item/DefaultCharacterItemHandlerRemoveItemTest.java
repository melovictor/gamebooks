package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import org.easymock.IMocksControl;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterItemHandlerRemoveItemTest {

    private static final String ITEM_ID = "3001";
    @UnderTest private DefaultCharacterItemHandler underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private ItemFactory itemFactory;
    private Character character;
    private Item nonEquippableItem;
    private Item equippableEquippedItem;
    private Item equippableNonEquippedItem;
    private Item nonEquippableItemStack;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
        nonEquippableItem = new Item(ITEM_ID, "item", ItemType.common);
        nonEquippableItemStack = new Item(ITEM_ID, "item", ItemType.common);
        equippableEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableNonEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableEquippedItem.getEquipInfo().setEquipped(true);
    }

    @BeforeMethod
    public void setUpMethod() {
        character = new Character();
        character.setBackpackSize(99);
        nonEquippableItemStack.setAmount(4);
        mockControl.reset();
    }

    public void testRemoveItemWhenAnItemIsAddedThenRemovedShouldNotContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        underTest.removeItem(character, new GatheredLostItem(ITEM_ID, 1, 0, false));
        final boolean hasItem = underTest.hasItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(hasItem);
    }

    public void testRemoveItemWhenAnEquippedItemIsBeingRemovedInNonEquippedOnlyModeShouldContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(this.equippableEquippedItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        underTest.removeItem(character, new GatheredLostItem(ITEM_ID, 1, 0, true));
        final boolean hasItem = underTest.hasItem(character, ITEM_ID);
        // THEN
        Assert.assertTrue(hasItem);
    }

    public void testRemoveItemWhenANonEquippedItemIsBeingRemovedInNonEquippedOnlyModeShouldNotContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(this.equippableNonEquippedItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        underTest.removeItem(character, new GatheredLostItem(ITEM_ID, 1, 0, true));
        final boolean hasItem = underTest.hasItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(hasItem);
    }

    public void testRemoveItemWhenANonEquippableItemIsBeingRemovedInNonEquippedOnlyModeShouldNotContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(this.nonEquippableItemStack);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        underTest.removeItem(character, new GatheredLostItem(ITEM_ID, 1, 0, true));
        final boolean hasItem = underTest.hasItem(character, ITEM_ID);
        // THEN
        Assert.assertTrue(hasItem);
        Assert.assertEquals(nonEquippableItemStack.getAmount(), 3);
    }

    public void testRemoveItemWhenAnItemIsAddedThenMoreIsRemovedShouldNotContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        underTest.removeItem(character, ITEM_ID, 3);
        final boolean hasItem = underTest.hasItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(hasItem);
    }

    public void testRemoveItemWhenItemsAreAddedThenOneIsRemovedShouldContainIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 3);
        underTest.removeItem(character, ITEM_ID, 1);
        // THEN
        Assert.assertTrue(underTest.hasItem(character, ITEM_ID));
    }

    public void testRemoveItemWhenAnItemListIsRemovedWhereBothElementsAreAvailableShouldRemoveRandomElement() {
        // GIVEN
        final List<Item> equipment = character.getEquipment();
        final Item sword = new Item("1001", "Sword", ItemType.weapon1);
        final Item dagger = new Item("1002", "Dagger", ItemType.weapon1);
        equipment.add(sword);
        equipment.add(dagger);
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, "1001,1002", 1);
        // THEN
        Assert.assertTrue(equipment.contains(sword) || equipment.contains(dagger));
        Assert.assertFalse(equipment.contains(sword) && equipment.contains(dagger));
    }

    public void testRemoveItemWhenAnItemListIsRemovedWhenOnlyOneElementIsAvailableShouldRemoveAvailableElement() {
        // GIVEN
        final List<Item> equipment = character.getEquipment();
        final Item sword = new Item("1001", "Sword", ItemType.weapon1);
        final Item provision = new Item("2000", "Provision", ItemType.provision);
        equipment.add(sword);
        equipment.add(provision);
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, "1001,1002", 2);
        // THEN
        Assert.assertFalse(equipment.contains(sword));
    }

    public void testRemoveItemWhenAnItemListIsRemovedWhereBothElementsAreAvailableButOneIsEquippedAndOnlyRemoveUnequippedShouldRemoveUnequippedElement() {
        // GIVEN
        final List<Item> equipment = character.getEquipment();
        final Item sword = new Item("1001", "Sword", ItemType.weapon1);
        sword.getEquipInfo().setEquipped(true);
        final Item dagger = new Item("1002", "Dagger", ItemType.weapon1);
        equipment.add(sword);
        equipment.add(dagger);
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, new GatheredLostItem("1001,1002", 1, 0, true));
        // THEN
        Assert.assertTrue(equipment.contains(sword));
        Assert.assertFalse(equipment.contains(dagger));
    }

    public void testRemoveItemWhenAnItemListIsRemovedWhereBothElementsAreAvailableButOneIsNonEquippedAndOnlyRemoveUnequippedShouldRemoveUnequippedElement() {
        // GIVEN
        final List<Item> equipment = character.getEquipment();
        final Item sword = new Item("1001", "Sword", ItemType.weapon1);
        sword.getEquipInfo().setEquipped(true);
        final Item rope = new Item("1002", "Rope", ItemType.common);
        equipment.add(sword);
        equipment.add(rope);
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, new GatheredLostItem("1001,1002", 1, 0, true));
        // THEN
        Assert.assertTrue(equipment.contains(sword));
        Assert.assertFalse(equipment.contains(rope));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
