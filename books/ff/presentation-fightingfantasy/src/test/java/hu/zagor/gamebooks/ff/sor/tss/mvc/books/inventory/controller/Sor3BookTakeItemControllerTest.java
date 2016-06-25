package hu.zagor.gamebooks.ff.sor.tss.mvc.books.inventory.controller;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ProcessableItemHolder;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.SuccessFailureDataContainer;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Sor3BookTakeItemController}.
 * @author Tamas_Szekeres
 */
@Test
public class Sor3BookTakeItemControllerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Sor3BookTakeItemController underTest;

    @Inject private LocaleProvider provider;
    @Inject private MessageSource messageSource;
    @Inject private DiceResultRenderer renderer;
    @Inject private RandomNumberGenerator generator;
    @Mock private HttpServletRequest request;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private SorCharacter character;
    @Inject private MarketHandler marketHandler;
    @Inject private ItemInteractionRecorder itemInteractionRecorder;
    @Mock private MarketCommand marketCommand;
    private BuySellResponse buySellResponse;
    @Mock private List<MarketElement> itemsForSale;
    @Instance private MarketElement marketA;
    @Instance private MarketElement marketB;
    @Instance private MarketElement marketC;
    @Mock private Item removedItem;
    @Mock private FfParagraphData after;
    @Mock private AttributeTestCommand skillCommand;
    @Instance private CommandList skillCommandList;
    private SuccessFailureDataContainer successContainer;
    @Mock private FfParagraphData successData;
    @Mock private List<GatheredLostItem> lostItems;
    private Capture<GatheredLostItem> newGatherLostItem;
    @Mock private Map<String, String> userInteractions;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(4);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
        itemsForSale = Arrays.asList(marketA, marketB, marketC);
        marketA.setId("3004");
        marketA.setPrice(2);
        marketB.setId("3005");
        marketB.setPrice(4);
        marketC.setId("3006");
        marketC.setPrice(9);
        skillCommandList.add(skillCommand);
        successContainer = new SuccessFailureDataContainer(successData, null);
        newGatherLostItem = newCapture();
    }

    public void testDoHandleMarketBuyWhenOnRandomSectionDoUsual() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("75");
        handleDefaultSale();
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketBuy(request, "3005");
        // THEN
        Assert.assertSame(returned, buySellResponse);
    }

    public void testDoHandleBarketBuyWhenBuyingFromTheElvesAndHasMarkerShouldEnsureItIsTradedAsFreeAndRemoveMarker() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("315a");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "4069")).andReturn(true);
        expectMarketCommand();
        expect(marketCommand.getItemsForSale()).andReturn(itemsForSale);
        expect(itemHandler.addItem(character, "gold", 4)).andReturn(4);
        expect(itemHandler.removeItem(character, "4069", 1)).andReturn(Arrays.asList(removedItem));
        handleDefaultSale();
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketBuy(request, "3005");
        // THEN
        Assert.assertSame(returned, buySellResponse);
    }

    public void testDoHandleBarketBuyWhenBuyingFromTheElvesAndHasNoMarkerShouldTradeAsDefault() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("315a");
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.hasItem(character, "4069")).andReturn(false);
        handleDefaultSale();
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketBuy(request, "3005");
        // THEN
        Assert.assertSame(returned, buySellResponse);
    }

    public void testDoHandleMarketSellWhenGenericSectionShouldTradeAsDefault() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("59");
        expect(paragraph.getDisplayId()).andReturn("59");
        handleDefaultPurchase();
        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketSell(request, "3005");
        // THEN
        Assert.assertSame(returned, buySellResponse);
    }

    public void testDoHandleMarketSellWhenSection79ShouldSetChosenItemAsLostForSuccessInSkillTestAndFinish() {
        // GIVEN
        expectWrapper();
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("79");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expectMarketCommand();
        expect(marketCommand.getAfter()).andReturn(after);
        expect(after.getCommands()).andReturn(skillCommandList);
        expect(skillCommand.getSuccess()).andReturn(Arrays.asList(successContainer));
        expect(successData.getLostItems()).andReturn(lostItems);
        lostItems.clear();
        expect(lostItems.add(capture(newGatherLostItem))).andReturn(true);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getUserInteraction()).andReturn(userInteractions);
        expect(userInteractions.get("foodExchangeBlacklistedItems")).andReturn(null);
        expect(userInteractions.put("foodExchangeBlacklistedItems", "3005")).andReturn(null);

        mockControl.replay();
        // WHEN
        final BuySellResponse returned = underTest.doHandleMarketSell(request, "3005");
        // THEN
        Assert.assertTrue(returned.isGiveUpMode());
        Assert.assertTrue(returned.isGiveUpFinished());
    }

    private void handleDefaultPurchase() {
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expectMarketCommand();
        expect(marketHandler.handleMarketSell("3005", character, marketCommand, characterHandler)).andReturn(buySellResponse);
        itemInteractionRecorder.recordItemMarketMovement(wrapper, "Purchase", "3005");
    }

    private void expectMarketCommand() {
        expect(paragraph.getItemsToProcess()).andReturn(Arrays.asList(new ProcessableItemHolder(marketCommand)));
    }

    private void handleDefaultSale() {
        expectWrapper();
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expectMarketCommand();
        expect(marketHandler.handleMarketPurchase("3005", character, marketCommand, characterHandler)).andReturn(buySellResponse);
        itemInteractionRecorder.recordItemMarketMovement(wrapper, "Sale", "3005");
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
