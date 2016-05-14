package hu.zagor.gamebooks.ff.mvc.book.inventory.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.command.market.GiveUpMode;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    private MarketCommand command;
    private MarketElement itemA;
    private MarketElement itemB;
    private MarketElement itemC;
    @Mock private List<Item> itemList;
    @Mock private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new MarketCommand();
        command.setMoneyAttribute("gold");

        itemA = getMarketElement("3001", 3, 2);
        itemB = getMarketElement("3002", 10, 1);
        itemC = getMarketElement("3003", 5, 0);
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
        final BuySellResponse returned = underTest.handleMarketPurchase("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 10);
    }

    public void testHandleMarketPurchaseWhenItemIsForSaleButTooExpensiveShouldReturnWithFailedTransactionAndOriginalMoney() {
        // GIVEN
        command.getItemsForSale().add(itemA);
        expect(character.getGold()).andReturn(2).times(2);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketPurchase("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 2);
    }

    public void testHandleMarketPurchaseWhenItemIsForSaleButOutOfStockShouldReturnWithFailedTransactionAndOriginalMoney() {
        // GIVEN
        command.getItemsForSale().add(itemC);
        expect(character.getGold()).andReturn(5).times(2);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketPurchase("3003", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 5);
    }

    public void testHandleMarketPurchaseWhenItemIsAvailableShouldReturnWithSuccessfulTransaction() {
        // GIVEN
        command.getItemsForSale().add(itemA);
        expect(character.getGold()).andReturn(5);
        attributeHandler.handleModification(character, "gold", -3);
        expect(itemHandler.addItem(character, "3001", 1)).andReturn(1);
        expect(character.getGold()).andReturn(2);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketPurchase("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), true);
        Assert.assertEquals(returned.getGold(), 2);
        Assert.assertEquals(itemA.getStock(), 1);
    }

    public void testHandleMarketSellWhenItemIsNotAvailableForSaleAndNotGiveUpModeShouldReturnWithFailedTransaction() {
        // GIVEN
        command.getItemsForPurchase().add(itemB);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 5);
        Assert.assertEquals(returned.isGiveUpMode(), false);
        Assert.assertEquals(returned.isGiveUpFinished(), true);
    }

    public void testHandleMarketSellWhenItemIsNotAvailableForSaleAndGiveUpModeStillActiveShouldReturnWithFailedTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemB);
        command.setGiveUpMode(GiveUpMode.asMuchAsPossible);
        command.setGiveUpAmount(1);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 5);
        Assert.assertEquals(returned.isGiveUpMode(), true);
        Assert.assertEquals(returned.isGiveUpFinished(), false);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }

    public void testHandleMarketSellWhenCharacterDoesNotOwnItemAndGiveUpModeStillActiveShouldReturnWithFailedTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(GiveUpMode.asMuchAsPossible);
        command.setGiveUpAmount(1);
        expect(itemHandler.hasItem(character, "3001")).andReturn(false);
        expect(character.getGold()).andReturn(5);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), false);
        Assert.assertEquals(returned.getGold(), 5);
        Assert.assertEquals(returned.isGiveUpMode(), true);
        Assert.assertEquals(returned.isGiveUpFinished(), false);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpModeNotActiveShouldReturnWithSuccessfulTransaction() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        attributeHandler.handleModification(character, "gold", 3);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), true);
        Assert.assertEquals(returned.getGold(), 8);
        Assert.assertEquals(returned.isGiveUpMode(), false);
        Assert.assertEquals(returned.isGiveUpFinished(), true);
        Assert.assertEquals(itemA.getStock(), 1);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpIsActiveWithOneItemShouldReturnWithSuccessfulTransactionAndGiveUpModeInactive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(GiveUpMode.asMuchAsPossible);
        command.setGiveUpAmount(1);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        attributeHandler.handleModification(character, "gold", 3);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), true);
        Assert.assertEquals(returned.getGold(), 8);
        Assert.assertEquals(returned.isGiveUpMode(), true);
        Assert.assertEquals(returned.isGiveUpFinished(), true);
        Assert.assertEquals(itemA.getStock(), 1);
        Assert.assertEquals(command.getGiveUpAmount(), 0);
    }

    public void testHandleMarketSellWhenCharacterOwnsItemAndGiveUpIsActiveWithTwoItemsShouldReturnWithSuccessfulTransactionAndGiveUpModeActive() {
        // GIVEN
        command.getItemsForPurchase().add(itemA);
        command.setGiveUpMode(GiveUpMode.asMuchAsPossible);
        command.setGiveUpAmount(2);
        expect(itemHandler.hasItem(character, "3001")).andReturn(true);
        attributeHandler.handleModification(character, "gold", 3);
        expect(itemHandler.removeItem(character, "3001", 1)).andReturn(itemList);
        expect(character.getGold()).andReturn(8);
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.handleMarketSell("3001", character, command, characterHandler);
        // THEN
        Assert.assertEquals(returned.isSuccessfulTransaction(), true);
        Assert.assertEquals(returned.getGold(), 8);
        Assert.assertEquals(returned.isGiveUpMode(), true);
        Assert.assertEquals(returned.isGiveUpFinished(), false);
        Assert.assertEquals(itemA.getStock(), 1);
        Assert.assertEquals(command.getGiveUpAmount(), 1);
    }
}
