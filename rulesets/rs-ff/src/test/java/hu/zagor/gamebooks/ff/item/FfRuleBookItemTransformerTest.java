package hu.zagor.gamebooks.ff.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;

import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Unit test for class {@link FfRuleBookItemTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FfRuleBookItemTransformerTest extends AbstractTransformerTest {

    private IMocksControl mockControl;
    private FfRuleBookItemTransformer underTest;
    private Logger logger;
    private Document document;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FfRuleBookItemTransformer();
        init(mockControl);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);
        document = mockControl.createMock(Document.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = XmlTransformationException.class)
    public void testTransformItemsWhenNodeListIsZeroShouldThrowException() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        logger.error("Couldn't find 'items' element in xml file!");
        mockControl.replay();
        // WHEN
        underTest.transformItems(document);
        // THEN throws exception
    }

    public void testTransformItemsWhenNodeListContainsItemNonForceEquippedItemShouldParseIt() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("item");
        expectAttributeReads("false");
        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        final FfItem item = (FfItem) returned.get("26");
        Assert.assertEquals(item.getName(), "Shield");
        Assert.assertFalse(item.getEquipInfo().isEquipped());
    }

    public void testTransformItemsWhenNodeListContainsItemForceEquippedItemShouldParseIt() throws XmlTransformationException {
        // GIVEN
        expect(document.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(node);
        expect(node.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(node);
        expect(node.getNodeName()).andReturn("item");
        expectAttributeReads("true");
        mockControl.replay();
        // WHEN
        final Map<String, Item> returned = underTest.transformItems(document);
        // THEN
        final FfItem item = (FfItem) returned.get("26");
        Assert.assertEquals(item.getName(), "Shield");
        Assert.assertTrue(item.getEquipInfo().isEquipped());
    }

    private void expectAttributeReads(final String forceEquip) {
        expectAttribute("id", "26");
        expectAttribute("name", "Shield");
        expectAttribute("type", "shield");
        expectAttribute("weaponSubType");
        expectAttribute("backpackSize");
        expectAttribute("staminaDamage");
        expectAttribute("skillDamage");

        expectAttribute("dose");
        expectAttribute("price");

        expectAttribute("addToSkill");
        expectAttribute("addToLuck");
        expectAttribute("addToStamina");
        expectAttribute("addToInitialSkill");
        expectAttribute("addToInitialLuck");
        expectAttribute("addToInitialStamina");

        expectAttribute("addToAttackStrength");

        expectAttribute("blessed");
        expectAttribute("magical");

        expectAttribute("preFight");

        expectAttribute("actions");
        expectAttribute("forceEquip", forceEquip);

        expectAttribute("removable");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
