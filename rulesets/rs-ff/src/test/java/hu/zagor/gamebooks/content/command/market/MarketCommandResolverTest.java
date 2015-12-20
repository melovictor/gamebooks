package hu.zagor.gamebooks.content.command.market;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link MarketCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class MarketCommandResolverTest {

    private IMocksControl mockControl;
    private MarketCommandResolver underTest;
    private ResolvationData resolvationData;
    private MarketCommand command;
    private BookInformations info;
    private FfCharacter character;
    private ParagraphData rootData;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfCharacterItemHandler itemHandler;

    private MarketElement marketElementA;
    private MarketElement marketElementB;
    private MarketElement marketElementC;
    private MarketElement marketElementD;
    private MarketElement marketElementE;

    private FfItem marketItemA;
    private FfItem marketItemB;
    private FfItem marketItemC;
    private FfItem marketItemD;
    private FfItem marketItemE;

    private FfParagraphData emptyHanded;
    private FfParagraphData after;

    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        info = new FfBookInformations(9L);

        emptyHanded = mockControl.createMock(FfParagraphData.class);
        after = mockControl.createMock(FfParagraphData.class);

        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        character = mockControl.createMock(FfCharacter.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);

        characterHandler = new FfCharacterHandler();
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);

        info.setCharacterHandler(characterHandler);

        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        underTest = new MarketCommandResolver();
        command = new MarketCommand();
        command.setEmptyHanded(emptyHanded);
        command.setAfter(after);

        marketElementA = getElement("3001", 5, 1);
        marketElementB = getElement("3002", 3, 2);
        marketElementC = getElement("3003", 7, 3);
        marketElementD = getElement("3004", 4, 1);
        marketElementE = getElement("3005", 8, 2);

        marketItemA = new FfItem("3001", "Shield", ItemType.shield);
        marketItemB = new FfItem("3002", "Leather Armour", ItemType.shield);
        marketItemC = new FfItem("3003", "Bronze Key", ItemType.shield);
        marketItemD = new FfItem("3004", "Helmet", ItemType.helmet);
        marketItemE = new FfItem("3005", "Candle", ItemType.common);
    }

    private MarketElement getElement(final String id, final int price, final int stock) {
        final MarketElement e = new MarketElement();
        e.setId(id);
        e.setPrice(price);
        e.setStock(stock);
        return e;
    }

    public void testDoResolveWhenSimplePurchaseSaleMarketIsInitializedShouldInitCommandAndReturnNullList() {
        // GIVEN
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(false);
        final List<Item> equipmentList = new ArrayList<>();
        equipmentList.add(marketItemA);
        equipmentList.add(marketItemD);
        equipmentList.add(marketItemE);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(itemHandler.resolveItem("3001")).andReturn(marketItemA);
        expect(itemHandler.resolveItem("3002")).andReturn(marketItemB);
        expect(itemHandler.resolveItem("3003")).andReturn(marketItemC);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(command.getItemsForPurchase().get(0).getName(), marketItemD.getName());
        Assert.assertSame(command.getItemsForPurchase().get(0).getStock(), 1);
        Assert.assertSame(command.getItemsForPurchase().get(1).getName(), marketItemE.getName());
        Assert.assertSame(command.getItemsForPurchase().get(1).getStock(), 1);
        Assert.assertSame(command.getItemsForSale().get(0).getName(), marketItemA.getName());
        Assert.assertSame(command.getItemsForSale().get(1).getName(), marketItemB.getName());
        Assert.assertSame(command.getItemsForSale().get(2).getName(), marketItemC.getName());
        Assert.assertNull(returned);
    }

    public void testDoResolveWhenPurchasableItemNotAvailableShouldInitCommandAndReturnNullList() {
        // GIVEN
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(false);
        final List<Item> equipmentList = new ArrayList<>();
        equipmentList.add(marketItemA);
        equipmentList.add(marketItemD);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(itemHandler.resolveItem("3001")).andReturn(marketItemA);
        expect(itemHandler.resolveItem("3002")).andReturn(marketItemB);
        expect(itemHandler.resolveItem("3003")).andReturn(marketItemC);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(command.getItemsForPurchase().get(0).getName(), marketItemD.getName());
        Assert.assertSame(command.getItemsForPurchase().get(0).getStock(), 1);
        Assert.assertSame(command.getItemsForPurchase().get(1).getName(), null);
        Assert.assertSame(command.getItemsForPurchase().get(1).getStock(), 0);
        Assert.assertSame(command.getItemsForSale().get(0).getName(), marketItemA.getName());
        Assert.assertSame(command.getItemsForSale().get(1).getName(), marketItemB.getName());
        Assert.assertSame(command.getItemsForSale().get(2).getName(), marketItemC.getName());
        Assert.assertNull(returned);
    }

    public void testDoResolveWhenInGiveUpModeAndHasNoPurchasableItemsShouldReturnEmptyHandedResponse() {
        // GIVEN
        command.setGiveUpMode(true);
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(false);
        final List<Item> equipmentList = new ArrayList<>();
        equipmentList.add(marketItemA);
        equipmentList.add(marketItemB);
        equipmentList.add(marketItemC);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(itemHandler.resolveItem("3001")).andReturn(marketItemA);
        expect(itemHandler.resolveItem("3002")).andReturn(marketItemB);
        expect(itemHandler.resolveItem("3003")).andReturn(marketItemC);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(command.getItemsForPurchase().get(0).getName());
        Assert.assertSame(command.getItemsForPurchase().get(0).getStock(), 0);
        Assert.assertNull(command.getItemsForPurchase().get(1).getName());
        Assert.assertSame(command.getItemsForPurchase().get(1).getStock(), 0);
        Assert.assertSame(command.getItemsForSale().get(0).getName(), marketItemA.getName());
        Assert.assertSame(command.getItemsForSale().get(1).getName(), marketItemB.getName());
        Assert.assertSame(command.getItemsForSale().get(2).getName(), marketItemC.getName());
        Assert.assertSame(returned.get(0), emptyHanded);
        Assert.assertTrue(command.isGiveUpUnsuccessful());
    }

    public void testDoResolveWhenInGiveUpModeAndHasPurchasableItemsShouldReturnNull() {
        // GIVEN
        command.setGiveUpMode(true);
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(false);
        final List<Item> equipmentList = new ArrayList<>();
        equipmentList.add(marketItemA);
        equipmentList.add(marketItemB);
        equipmentList.add(marketItemE);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(itemHandler.resolveItem("3001")).andReturn(marketItemA);
        expect(itemHandler.resolveItem("3002")).andReturn(marketItemB);
        expect(itemHandler.resolveItem("3003")).andReturn(marketItemC);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(command.getItemsForPurchase().get(0).getName());
        Assert.assertSame(command.getItemsForPurchase().get(0).getStock(), 0);
        Assert.assertSame(command.getItemsForPurchase().get(1).getName(), marketItemE.getName());
        Assert.assertSame(command.getItemsForPurchase().get(1).getStock(), 1);
        Assert.assertSame(command.getItemsForSale().get(0).getName(), marketItemA.getName());
        Assert.assertSame(command.getItemsForSale().get(1).getName(), marketItemB.getName());
        Assert.assertSame(command.getItemsForSale().get(2).getName(), marketItemC.getName());
        Assert.assertNull(returned);
        Assert.assertFalse(command.isGiveUpUnsuccessful());
    }

    public void testDoResolveWhenSimplePurchaseSaleMarketIsClosedAndCharacterDoesNotHaveEnoughGoldShouldReinitializeMarket() {
        // GIVEN
        command.setMustHaveGold(10);
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(true);
        expect(attributeHandler.resolveValue(character, "gold")).andReturn(5);
        final List<Item> equipmentList = new ArrayList<>();
        equipmentList.add(marketItemA);
        equipmentList.add(marketItemD);
        equipmentList.add(marketItemE);
        expect(character.getEquipment()).andReturn(equipmentList);
        expect(itemHandler.resolveItem("3001")).andReturn(marketItemA);
        expect(itemHandler.resolveItem("3002")).andReturn(marketItemB);
        expect(itemHandler.resolveItem("3003")).andReturn(marketItemC);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(command.getItemsForPurchase().get(0).getName(), marketItemD.getName());
        Assert.assertSame(command.getItemsForPurchase().get(0).getStock(), 1);
        Assert.assertSame(command.getItemsForPurchase().get(1).getName(), marketItemE.getName());
        Assert.assertSame(command.getItemsForPurchase().get(1).getStock(), 1);
        Assert.assertSame(command.getItemsForSale().get(0).getName(), marketItemA.getName());
        Assert.assertSame(command.getItemsForSale().get(1).getName(), marketItemB.getName());
        Assert.assertSame(command.getItemsForSale().get(2).getName(), marketItemC.getName());
        Assert.assertNull(returned);
    }

    public void testDoResolveWhenSimplePurchaseSaleMarketIsClosedAndCharacterHasEnoughGoldShouldReturnAfter() {
        // GIVEN
        command.setMustHaveGold(3);
        command.getItemsForSale().add(marketElementA);
        command.getItemsForSale().add(marketElementB);
        command.getItemsForSale().add(marketElementC);
        command.getItemsForPurchase().add(marketElementD);
        command.getItemsForPurchase().add(marketElementE);
        expect(interactionHandler.hasMarketCommand(character)).andReturn(true);
        expect(attributeHandler.resolveValue(character, "gold")).andReturn(5);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned.get(0), after);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
