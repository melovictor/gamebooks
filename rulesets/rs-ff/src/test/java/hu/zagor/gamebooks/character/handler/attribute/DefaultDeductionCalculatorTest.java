package hu.zagor.gamebooks.character.handler.attribute;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Arrays;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultDeductionCalculator}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultDeductionCalculatorTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DefaultDeductionCalculator underTest;
    @Mock private FfCharacter character;
    private FfItem item10;
    private FfItem item6;
    private FfItem item5;
    private FfItem item4;
    private FfItem itemnv;

    @BeforeClass
    public void setUpClass() {
        item10 = getItem(10);
        item6 = getItem(6);
        item5 = getItem(5);
        item4 = getItem(4);
        itemnv = getItem(11, ItemType.common);
    }

    private FfItem getItem(final int price) {
        return getItem(price, ItemType.valuable);
    }

    private FfItem getItem(final int price, final ItemType type) {
        final FfItem item = new FfItem("gold" + price, "gold " + price + " - " + type, type);
        item.setGold(price);
        return item;
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testCalculateDeductibleElementsWhenSingleItemMatchesPriceShouldReturnSingleItemWithoutExtraGold() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(item4, itemnv, item5, item10, item6)));
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction returned = underTest.calculateDeductibleElements(character, 10);
        // THEN
        Assert.assertSame(returned.getItems().size(), 1);
        Assert.assertSame(returned.getItems().get(0), item10);
        Assert.assertEquals(returned.getGold(), 0);
    }

    public void testCalculateDeductibleElementsWhenSingleItemWorthMoreThanPriceAndNothingElseShouldReturnSingleItemWithoutExtraGold() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(itemnv, item10)));
        expect(character.getGold()).andReturn(0);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction returned = underTest.calculateDeductibleElements(character, 6);
        // THEN
        Assert.assertSame(returned.getItems().size(), 1);
        Assert.assertSame(returned.getItems().get(0), item10);
        Assert.assertEquals(returned.getGold(), 0);
    }

    public void testCalculateDeductibleElementsWhenSingleItemPlusGoldMatchesPriceShouldReturnSingleItemWithExtraGold() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(item4, itemnv, item5, item10, item6)));
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction returned = underTest.calculateDeductibleElements(character, 11);
        // THEN
        Assert.assertSame(returned.getItems().size(), 1);
        Assert.assertSame(returned.getItems().get(0), item10);
        Assert.assertEquals(returned.getGold(), 1);
    }

    public void testCalculateDeductibleElementsWhenTwoItemsMatchesPriceShouldReturnTwoSingleItemsWithoutExtraGold() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(item4, itemnv, item5, item10, item6)));
        expect(character.getGold()).andReturn(0);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction returned = underTest.calculateDeductibleElements(character, 11);
        // THEN
        Assert.assertSame(returned.getItems().size(), 2);
        Assert.assertSame(returned.getItems().get(0), item6);
        Assert.assertSame(returned.getItems().get(1), item5);
        Assert.assertEquals(returned.getGold(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalculateDeductibleElementsWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.calculateDeductibleElements(character, -1);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalculateDeductibleElementsWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.calculateDeductibleElements(character, 0);
        // THEN throws exception
    }

    public void testCalculateDeductibleElementsWhenGoldIsMoreThanTotalWealthShouldReturnEverything() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(item4, itemnv, item5, item10, item6)));
        expect(character.getGold()).andReturn(15);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction calculateDeductibleElements = underTest.calculateDeductibleElements(character, 9999999);
        // THEN
        Assert.assertEquals(calculateDeductibleElements.getGold(), 15);
        Assert.assertEquals(calculateDeductibleElements.getItems().size(), 4);
        Assert.assertFalse(calculateDeductibleElements.getItems().contains(itemnv));
        Assert.assertTrue(calculateDeductibleElements.getItems().contains(item10));
        Assert.assertTrue(calculateDeductibleElements.getItems().contains(item4));
        Assert.assertTrue(calculateDeductibleElements.getItems().contains(item5));
        Assert.assertTrue(calculateDeductibleElements.getItems().contains(item6));
    }

    public void testCalculateDeductibleElementsWhenGoldHasNoValuablesShouldReturnAmountToBeDeductedAsGold() {
        // GIVEN
        expect(character.getFfEquipment()).andReturn(new ArrayList<>(Arrays.asList(itemnv)));
        expect(character.getGold()).andReturn(15);
        mockControl.replay();
        // WHEN
        final GoldItemDeduction calculateDeductibleElements = underTest.calculateDeductibleElements(character, 3);
        // THEN
        Assert.assertEquals(calculateDeductibleElements.getGold(), 3);
        Assert.assertTrue(calculateDeductibleElements.getItems().isEmpty());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
