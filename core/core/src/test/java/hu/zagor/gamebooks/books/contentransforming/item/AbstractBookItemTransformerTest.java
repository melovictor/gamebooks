package hu.zagor.gamebooks.books.contentransforming.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link AbstractBookItemTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractBookItemTransformerTest extends AbstractTransformerTest {

    private static final String ITEM_ID = "3001";
    private static final String NAME = "provision";
    private static final String VALID_ITEM_TYPE = "common";
    private static final String INVALID_ITEM_TYPE = "time machine";
    private TestTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private Item item;
    @Inject private BeanFactory beanFactory;
    @Mock private Document document;
    @Inject private Logger logger;
    @Mock private EquipInfo equipInfo;

    @UnderTest
    public TestTransformer underTest() {
        return new TestTransformer();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformItemsWhenDocumentIsNullShouldThrowException() throws XmlTransformationException {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transformItems(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = XmlTransformationException.class)
    public void testTransformItemsWhenChildNodesAreEmptyShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        logger.error("Couldn't find 'items' element in xml file!");
        mockControl.replay();
        // WHEN
        underTest.transformItems(document);
        // THEN throws exception
    }

    public void testTransformItemsWhenChildNodesContainNoItemShouldReturnEmptyContentMap() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(nodeValue);

        expect(nodeValue.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testTransformItemsWhenChildNodesContainItemWithoutSubTypeShouldReturnContentMapWithItem() throws XmlTransformationException {
        expectBasicXmlStructure(1);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        item.setBackpackSize(1);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey(ITEM_ID));
        Assert.assertSame(returned.get(ITEM_ID), item);
    }

    public void testTransformItemsWhenChildNodesContainDefaultWeaponItemWithoutSubTypeShouldReturnContentMapWithUnremovableItem() throws XmlTransformationException {
        expectBasicXmlStructure(1);

        expectAttribute("id", "defWpn");
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        expect(item.getEquipInfo()).andReturn(equipInfo);
        equipInfo.setRemovable(false);
        item.setBackpackSize(1);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn("defWpn");

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey("defWpn"));
        Assert.assertSame(returned.get("defWpn"), item);
    }

    public void testTransformItemsWhenChildNodesContainItemWithSubTypeShouldReturnContentMapWithItem() throws XmlTransformationException {
        expectBasicXmlStructure(1);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType", "blunt");
        expectAttribute("backpackSize");
        item.setSubType(WeaponSubType.blunt);
        item.setBackpackSize(1);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey(ITEM_ID));
        Assert.assertSame(returned.get(ITEM_ID), item);
    }

    public void testTransformItemsWhenChildNodesContainShadowItemShouldSetBackpackSizeToZeroAndReturnContentMapWithItem() throws XmlTransformationException {
        // GIVEN
        expectBasicXmlStructure(1);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", "shadow");
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        item.setBackpackSize(0);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey(ITEM_ID));
        Assert.assertSame(returned.get(ITEM_ID), item);
    }

    public void testTransformItemsWhenChildNodesContainItemWithBackpackSizeShouldSetBackpackSizeToGivenValueAndReturnContentMapWithItem()
        throws XmlTransformationException {
        expectBasicXmlStructure(1);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize", "2");
        item.setBackpackSize(2);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertTrue(returned.containsKey(ITEM_ID));
        Assert.assertSame(returned.get(ITEM_ID), item);
    }

    public void testTransformItemsWhenChildNodesContainItemWithInvalidTypeShouldReturnEmptyContentMap() throws XmlTransformationException {
        expectBasicXmlStructure(1);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", INVALID_ITEM_TYPE);
        expectAttribute("weaponSubType", "blunt");
        expectAttribute("backpackSize");

        logger.error("Found item '{}/{}' with unparseable type '{}'!", NAME, ITEM_ID, INVALID_ITEM_TYPE);

        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testTransformItemsWhenChildNodesContainItemAndDuplicateIdIsFoundShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expectBasicXmlStructure(2);

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        item.setBackpackSize(1);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("item");

        expectAttribute("id", ITEM_ID);
        expectAttribute("name", NAME);
        expectAttribute("type", VALID_ITEM_TYPE);
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        item.setBackpackSize(1);
        expectAttribute("description");
        item.setDescription(null);

        expect(item.getId()).andReturn(ITEM_ID);

        mockControl.replay();
        // WHEN
        underTest.transformItems(document);
        // THEN throws exception
    }

    private void expectBasicXmlStructure(final int nodeLength) {
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(nodeValue);

        expect(nodeValue.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(nodeLength);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("item");
    }

    private class TestTransformer extends AbstractBookItemTransformer<Item> {

        @Override
        protected Item getItem(final String id, final String name, final ItemType itemType) {
            return item;
        }

        @Override
        protected void finishItemCreation(final Item item, final Node node) {
        }

    }
}
