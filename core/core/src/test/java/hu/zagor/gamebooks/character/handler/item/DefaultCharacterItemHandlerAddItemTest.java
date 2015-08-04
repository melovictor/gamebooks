package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
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
public class DefaultCharacterItemHandlerAddItemTest {

    private static final String ITEM_ID = "3001";
    private static final String ITEM_ID_C = "3003";
    private static final String ITEM_ID_D = "3004";
    private CharacterItemHandler underTest;
    private IMocksControl mockControl;
    private ItemFactory itemFactory;
    private Character character;
    private Item nonEquippableItem;
    private Item normalBackpackItem;
    private Item oversizedBackpackItem;
    private Item equippableEquippedItem;
    private Item equippableNonEquippedItem;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        mockControl.createMock(ItemFactory.class);
        itemFactory = mockControl.createMock(ItemFactory.class);
        nonEquippableItem = new Item(ITEM_ID, "item", ItemType.common);

        normalBackpackItem = new Item(ITEM_ID_C, "item", ItemType.common);
        oversizedBackpackItem = new Item(ITEM_ID_D, "item", ItemType.common);
        oversizedBackpackItem.setBackpackSize(2);

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
        normalBackpackItem.setAmount(1);
        mockControl.reset();
    }

    public void testAddItemWhenAnItemIsAddedShouldStoreIt() {
        // GIVEN
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 1);
        // THEN
        Assert.assertTrue(underTest.hasItem(character, ITEM_ID));
    }

    public void testAddItemWhenBackpackHasTwoNonEquippableItemsShouldNotAddItem() {
        // GIVEN
        character.setBackpackSize(2);
        character.getEquipment().add(normalBackpackItem);
        character.getEquipment().add(normalBackpackItem);
        expect(itemFactory.resolveItem(ITEM_ID_C)).andReturn(normalBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_C, 1);
        // THEN
        Assert.assertFalse(underTest.hasItem(character, ITEM_ID_C, 3));
    }

    public void testAddItemWhenBackpackHasStackedTwoNonEquippableItemsShouldNotAddItem() {
        // GIVEN
        character.setBackpackSize(2);
        normalBackpackItem.setAmount(2);
        character.getEquipment().add(normalBackpackItem);
        expect(itemFactory.resolveItem(ITEM_ID_C)).andReturn(normalBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_C, 1);
        // THEN
        Assert.assertFalse(underTest.hasItem(character, ITEM_ID_C, 3));
    }

    public void testAddItemWhenBackpackHasOneNonEquippableItemAndNewIsTooBigShouldNotAddItem() {
        // GIVEN
        character.setBackpackSize(2);
        character.getEquipment().add(normalBackpackItem);
        expect(itemFactory.resolveItem(ITEM_ID_D)).andReturn(oversizedBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_D, 1);
        // THEN
        Assert.assertFalse(underTest.hasItem(character, ITEM_ID_D));
    }

    public void testAddItemWhenBackpackHasOneNonEquippableAndOneEquippedItemShouldAddItem() {
        // GIVEN
        character.setBackpackSize(2);
        character.getEquipment().add(normalBackpackItem);
        character.getEquipment().add(equippableEquippedItem);
        expect(itemFactory.resolveItem(ITEM_ID_C)).andReturn(normalBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_C, 1);
        // THEN
        Assert.assertTrue(underTest.hasItem(character, ITEM_ID_C, 2));
    }

    public void testAddItemWhenBackpackHasOneNonEquippableAndOneEquippedAndOneNonEquippedItemShouldNotAddItem() {
        // GIVEN
        character.setBackpackSize(2);
        character.getEquipment().add(normalBackpackItem);
        character.getEquipment().add(equippableEquippedItem);
        character.getEquipment().add(equippableNonEquippedItem);
        expect(itemFactory.resolveItem(ITEM_ID_C)).andReturn(normalBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_C, 1);
        // THEN
        Assert.assertFalse(underTest.hasItem(character, ITEM_ID_C, 2));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
