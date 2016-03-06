package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterItemHandlerAddItemTest {

    private static final String ITEM_ID = "3001";
    private static final String ITEM_ID_C = "3003";
    private static final String ITEM_ID_D = "3004";
    @UnderTest private DefaultCharacterItemHandler underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private ItemFactory itemFactory;
    private Character character;
    private Item nonEquippableItem;
    private Item normalBackpackItem;
    private Item oversizedBackpackItem;
    private Item equippableEquippedItem;
    private Item equippableNonEquippedItem;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
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
        character = new Character();
        character.setBackpackSize(99);
        normalBackpackItem.setAmount(1);
    }

    public void testAddItemWhenAnItemIsAddedShouldStoreIt() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_C);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_C);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_D);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_C);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_C);
        expect(itemFactory.resolveItem(ITEM_ID_C)).andReturn(normalBackpackItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID_C, 1);
        // THEN
        Assert.assertFalse(underTest.hasItem(character, ITEM_ID_C, 2));
    }

}
