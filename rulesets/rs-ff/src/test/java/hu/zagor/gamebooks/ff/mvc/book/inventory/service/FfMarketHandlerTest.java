package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfMarketHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfMarketHandlerTest {
    @MockControl private IMocksControl mockControl;
    @UnderTest private FfMarketHandler underTest;
    @Mock private FfCharacter character;
    @Mock private FfCharacterItemHandler itemHandler;
    private MarketCommand command;
    private MarketElement itemA;
    private MarketElement itemB;
    private MarketElement itemC;
    @Mock private List<Item> itemList;

    @BeforeMethod
    public void setUpMethod() {
        command = new MarketCommand();

        itemA = getMarketElement("3001", 3, 2);
        itemB = getMarketElement("3002", 10, 1);
        itemC = getMarketElement("3003", 5, 0);

        mockControl.reset();
    }

    private MarketElement getMarketElement(final String id, final int price, final int stock) {
        final MarketElement item = new MarketElement();
        item.setId(id);
        item.setPrice(price);
        item.setStock(stock);
        return item;
    }

    public void testHandleMarketPurchaseWhenItemIsNotForSaleShouldReturnWithFailedTransactionAndOriginalMoney() {
        // GIVEN
        command.getItemsForSale().add(itemB);
        expect(character.getGold()).andReturn(10);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketPurchase("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 10);
    }

    public void testHandleMarketPurchaseWhenItemIsForSaleButTooExpensiveShouldReturnWithFailedTransactionAndOriginalMoney() {
        // GIVEN
        command.getItemsForSale().add(itemA);
        expect(character.getGold()).andReturn(2).times(2);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketPurchase("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 2);
    }

    public void testHandleMarketPurchaseWhenItemIsForSaleButOutOfStockShouldReturnWithFailedTransactionAndOriginalMoney() {
        // GIVEN
        command.getItemsForSale().add(itemC);
        expect(character.getGold()).andReturn(5).times(2);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketPurchase("3003", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 5);
    }

    public void testHandleMarketPurchaseWhenItemIsAvailableShouldReturnWithSuccessfulTransaction() {
        // GIVEN
        command.getItemsForSale().add(itemA);
        expect(character.getGold()).andReturn(5);
        character.setGold(2);
        expect(itemHandler.addItem(character, "3001", 1)).andReturn(1);
        expect(character.getGold()).andReturn(2);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketPurchase("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), true);
        Assert.assertEquals(returned.get("gold"), 2);
        Assert.assertEquals(itemA.getStock(), 1);
    }

    public void testHandleMarketSellWhenItemIsNotAvailableForSaleAndNotGiveUpModeShouldReturnWithFailedTransaction() {
        // GIVEN
        command.getItemsForPurchase().add(itemB);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 5);
        Assert.assertEquals(returned.get("giveUpMode"), false);
        Assert.assertEquals(returned.get("giveUpFinished"), true);
    }

    public void testHandleMarketSellWhenItemIsNotAvailableForSaleAndGiveUpModeStillActiveShouldReturnWithFailedTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemB);
        command.setGiveUpMode(true);
        command.setGiveUpAmount(1);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 5);
        Assert.assertEquals(returned.get("giveUpMode"), true);
        Assert.assertEquals(returned.get("giveUpFinished"), false);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }

    public void testHandleMarketSellWhenCharacterDoesNotOwnItemAndGiveUpModeStillActiveShouldReturnWithFailedTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(true);
        command.setGiveUpAmount(1);
        expect(itemHandler.hasItem(character, "3001")).andReturn(false);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), false);
        Assert.assertEquals(returned.get("gold"), 5);
        Assert.assertEquals(returned.get("giveUpMode"), true);
        Assert.assertEquals(returned.get("giveUpFinished"), false);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpModeNotActiveShouldReturnWithSuccessfulTransaction() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(false);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        expect(character.getGold()).andReturn(5);
        character.setGold(8);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), true);
        Assert.assertEquals(returned.get("gold"), 8);
        Assert.assertEquals(returned.get("giveUpMode"), false);
        Assert.assertEquals(returned.get("giveUpFinished"), true);
        Assert.assertEquals(itemA.getStock(), 1);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpIsActiveWithOneItemShouldReturnWithSuccessfulTransactionAndGiveUpModeInactive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(true);
        command.setGiveUpAmount(1);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        expect(character.getGold()).andReturn(5);
        character.setGold(8);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), true);
        Assert.assertEquals(returned.get("gold"), 8);
        Assert.assertEquals(returned.get("giveUpMode"), true);
        Assert.assertEquals(returned.get("giveUpFinished"), true);
        Assert.assertEquals(itemA.getStock(), 1);
        Assert.assertEquals(command.getGiveUpAmount(), 0);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpIsActiveWithTwoItemsShouldReturnWithSuccessfulTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(true);
        command.setGiveUpAmount(2);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        expect(character.getGold()).andReturn(5);
        character.setGold(8);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final Map<String, Object> returned = underTest.handleMarketSell("3001", character, command, itemHandler);
        // THEN
        Assert.assertEquals(returned.get("successfulTransaction"), true);
        Assert.assertEquals(returned.get("gold"), 8);
        Assert.assertEquals(returned.get("giveUpMode"), true);
        Assert.assertEquals(returned.get("giveUpFinished"), false);
        Assert.assertEquals(itemA.getStock(), 1);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
