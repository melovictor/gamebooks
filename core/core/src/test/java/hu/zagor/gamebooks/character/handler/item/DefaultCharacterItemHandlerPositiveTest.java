package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Iterator;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterItemHandlerPositiveTest {

    private static final String ITEM_ID = "3001";
    private static final String ITEM_ID_B = "3002";
    @UnderTest private DefaultCharacterItemHandler underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private ItemFactory itemFactory;
    private Character character;
    private Item nonEquippableItem;
    private Item nonEquippableItemB;
    private Item equippableEquippedItem;
    private Item equippableNonEquippedItem;
    @Inject private Logger logger;
    @Inject private BookInformationFetcher bookInformationFetcher;
    @Mock private BookInformations info;
    @Inject private BeanFactory beanFactory;

    @BeforeClass
    public void setUpClass() {
        nonEquippableItemB = new Item(ITEM_ID_B, "item", ItemType.common);
        nonEquippableItem = new Item(ITEM_ID, "item", ItemType.common);
        equippableEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableNonEquippedItem = new Item(ITEM_ID, "item", ItemType.weapon1);
        equippableEquippedItem.getEquipInfo().setEquipped(true);
    }

    @BeforeMethod
    public void setUpMethod() {
        character = new Character();
        character.setBackpackSize(99);
    }

    public void testHasItemWhenItemIsNotInEquipmentShouldReturnFalse() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(ITEM_ID)).andReturn(nonEquippableItem);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 3);
        final boolean result = underTest.hasItem(character, "3002");
        // THEN
        Assert.assertFalse(result);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID_B);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(ITEM_ID_B)).andReturn(nonEquippableItemB);
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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
        logger.debug("Resolving item {} for addition.", ITEM_ID);
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
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

    public void testGetItemsWhenCharacterDoesNotHaveItemShouldReturnEmptyList() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final List<Item> returned = underTest.getItems(character, ITEM_ID);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testGetItemsWhenCharacterHasSingleItemShouldReturnListWithOneElement() {
        // GIVEN
        character.getEquipment().add(equippableEquippedItem);
        character.getEquipment().add(nonEquippableItemB);
        mockControl.replay();
        // WHEN
        final List<Item> returned = underTest.getItems(character, ITEM_ID);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertSame(returned.get(0), equippableEquippedItem);
    }

    public void testGetItemsWhenCharacterHasMultipleItemShouldReturnListWithMultipleElement() {
        // GIVEN
        character.getEquipment().add(equippableEquippedItem);
        character.getEquipment().add(nonEquippableItemB);
        character.getEquipment().add(equippableNonEquippedItem);
        mockControl.replay();
        // WHEN
        final List<Item> returned = underTest.getItems(character, ITEM_ID);
        // THEN
        Assert.assertEquals(returned.size(), 2);
        Assert.assertSame(returned.get(0), equippableEquippedItem);
        Assert.assertSame(returned.get(1), equippableNonEquippedItem);
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

}
