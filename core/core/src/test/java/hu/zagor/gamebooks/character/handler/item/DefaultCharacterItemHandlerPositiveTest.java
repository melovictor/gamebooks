package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;

import java.util.Iterator;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterItemHandlerPositiveTest {

    private static final String ITEM_ID = "3001";
    private static final String ITEM_ID_B = "3002";
    private CharacterItemHandler underTest;
    private IMocksControl mockControl;
    private ItemFactory itemFactory;
    private Character character;
    private Item nonEquippableItem;
    private Item nonEquippableItemB;
    private Item equippableEquippedItem;
    private Item equippableNonEquippedItem;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        mockControl.createMock(ItemFactory.class);
        itemFactory = mockControl.createMock(ItemFactory.class);
        nonEquippableItemB = new Item(ITEM_ID_B, "item", ItemType.common);
        nonEquippableItem = new Item(ITEM_ID, "item", ItemType.common);
        equippableEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableNonEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableEquippedItem.getEquipInfo().setEquipped(true);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultCharacterItemHandler();
        underTest.setItemFactory(itemFactory);
        character = new Character();
        character.setBackpackSize(99);
        mockControl.reset();
    }

    public void testHasItemWhenItemIsNotInEquipmentShouldReturnFalse() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 3);
        final boolean result = underTest.hasItem(character, "3002");
        // THEN
        Assert.assertFalse(result);
    }

    public void testSetItemFactoryWhenItemFactoryIsAlreadySetShouldReplaceIt() {
        // GIVEN
        final ItemFactory factory = mockControl.createMock(ItemFactory.class);
        underTest.setItemFactory(factory);
        mockControl.replay();
        // WHEN
        final ItemFactory returned = Whitebox.getInternalState(underTest, "itemFactory");
        // THEN
        Assert.assertSame(returned, factory);
    }

    public void testHasEquippedItemWhenIdIsNotAvailableShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.hasEquippedItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasEquippedItemWhenItemIsEquippableAndNotEquippedShouldReturnFalse() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID_B)).andReturn(nonEquippableItemB);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(equippableNonEquippedItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_B, 1);
        underTest.addItem(character, ITEM_ID, 1);
        final boolean returned = underTest.hasEquippedItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasEquippedItemWhenItemIsEquippableAndIsEquippedShouldReturnTrue() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(equippableEquippedItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        final boolean returned = underTest.hasEquippedItem(character, ITEM_ID);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testHasEquippedItemWhenItemIsNotEquippableShouldReturnFalse() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        final boolean returned = underTest.hasEquippedItem(character, ITEM_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasItemWhenThreeItemsNeededButOnlyTwoAreAvailableShouldReturnFalse() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 2);
        final boolean hasItem = underTest.hasItem(character, ITEM_ID, 3);
        // THEN
        Assert.assertFalse(hasItem);
    }

    public void testHasItemWhenThreeItemsNeededAndFourAreAvailableShouldReturnTrue() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 4);
        final boolean hasItem = underTest.hasItem(character, ITEM_ID, 3);
        // THEN
        Assert.assertTrue(hasItem);
    }

    public void testResolveItemShouldResolveItemIdFromFactory() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(equippableEquippedItem);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.resolveItem(ITEM_ID);
        // THEN
        Assert.assertSame(returned, equippableEquippedItem);
    }

    public void testGetItemWhenCharacterDoesNotHaveItemShouldReturnNull() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getItem(character, ITEM_ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetItemWhenCharacterDoesHaveItemShouldReturnItem() {
        // GIVEN
        character.getEquipment().add(equippableEquippedItem);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getItem(character, equippableEquippedItem.getId());
        // THEN
        Assert.assertSame(returned, equippableEquippedItem);
    }

    public void testGetItemIteratorShouldReturnIteratorForEquipment() {
        // GIVEN
        character.getEquipment().add(equippableEquippedItem);
        mockControl.replay();
        // WHEN
        final Iterator<Item> returned = underTest.getItemIterator(character);
        // THEN
        Assert.assertTrue(returned.hasNext());
        Assert.assertSame(returned.next(), equippableEquippedItem);
        Assert.assertFalse(returned.hasNext());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
