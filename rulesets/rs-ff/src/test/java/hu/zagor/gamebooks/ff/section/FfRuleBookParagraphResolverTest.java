package hu.zagor.gamebooks.ff.section;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private FfRuleBookParagraphResolver underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private FfParagraphData paragraphData;
    @Mock private FfCharacter character;
    @Mock private FfCharacterItemHandler itemHandler;
    @Instance private List<GatheredLostItem> lostItems;
    private GatheredLostItem eqWpn;
    @Mock private FfItem item;
    @Inject private Logger logger;
    private GatheredLostItem normalItem;
    private GatheredLostItem doseFromPotion;
    @Mock private List<Item> itemList;

    @BeforeClass
    public void setUpClass() {
        eqWpn = new GatheredLostItem("equippedWeapon", 1, 0, false);
        normalItem = new GatheredLostItem("3005", 1, 0, false);
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
        expect(itemHandler.removeItem(character, "1003", 1)).andReturn(itemList);
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
        expect(itemHandler.removeItem(character, normalItem)).andReturn(itemList);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        mockControl.replay();
        // WHEN
        underTest.loseItems(paragraphData, character, itemHandler);
        // THEN
        Assert.assertTrue(lostItems.isEmpty());
    }

    public void testLoseItemsWhenTryingToLoseDosesFromPotionsAndHasLeftoverShouldReduceDoseOnItem() {
        // GIVEN
        lostItems.add(doseFromPotion);
        expect(paragraphData.getLostItems()).andReturn(lostItems);
        logger.debug("Lost item {}", "2003");
        expect(itemHandler.getItem(character, "2003")).andReturn(item);
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
        expect(item.getDose()).andReturn(1);
        expect(itemHandler.removeItem(character, "2003", 1)).andReturn(itemList);
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
