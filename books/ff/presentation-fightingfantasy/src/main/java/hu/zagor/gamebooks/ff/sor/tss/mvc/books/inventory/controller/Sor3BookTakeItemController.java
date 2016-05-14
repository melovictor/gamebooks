package hu.zagor.gamebooks.ff.sor.tss.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SEVEN_SERPENTS)
public class Sor3BookTakeItemController extends SorBookTakeItemController {

    private static final int BASE_ITEM_SELLING_PRICE = 3;

    @Autowired private LocaleProvider provider;
    @Autowired private MessageSource messageSource;
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    protected BuySellResponse doHandleMarketBuy(final HttpServletRequest request, final String itemId) {
        BuySellResponse result;
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();

        if ("315a".equals(paragraph.getId())) {
            final Character character = wrapper.getCharacter();
            if (itemHandler.hasItem(character, "4069")) {
                final MarketCommand marketCommand = fetchMarketCommand(paragraph);
                final MarketElement item = getMarketItem(marketCommand.getItemsForSale(), itemId);
                itemHandler.addItem(character, "gold", item.getPrice());
                itemHandler.removeItem(character, "4069", 1);
            }
        }
        result = super.doHandleMarketBuy(request, itemId);

        return result;
    }

    private MarketElement getMarketItem(final List<MarketElement> itemList, final String itemId) {
        MarketElement found = null;
        for (final MarketElement element : itemList) {
            if (itemId.equals(element.getId())) {
                found = element;
            }
        }
        return found;
    }

    @Override
    public BuySellResponse doHandleMarketSell(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        BuySellResponse handleMarketSell;
        if ("79".equals(paragraph.getId())) {
            handleMarketSell = handleMarket79(itemId, wrapper);
        } else if ("315".equals(paragraph.getDisplayId())) {
            handleMarketSell = handleMarket315(itemId, wrapper);
        } else {
            handleMarketSell = super.doHandleMarketSell(request, itemId);
        }
        return handleMarketSell;
    }

    private BuySellResponse handleMarket315(final String itemId, final HttpSessionWrapper wrapper) {
        final BuySellResponse handleMarketSell = new BuySellResponse();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();

        final Item item = itemHandler.getItem(character, itemId);
        final int diff = calculateSkillTestDifference(handleMarketSell, character, item.getName());
        if (diff >= 0) {
            if (roundOne(itemHandler, character)) {
                itemHandler.addItem(character, "4076", 1);
                itemHandler.addItem(character, "gold", BASE_ITEM_SELLING_PRICE);
            } else {
                itemHandler.removeItem(character, "4076", 1);
                itemHandler.addItem(character, "gold", BASE_ITEM_SELLING_PRICE + diff);
            }
            itemHandler.removeItem(character, itemId, 1);
            final MarketCommand marketCommand = fetchMarketCommand(wrapper.getParagraph());
            final MarketElement marketItem = getMarketItem(marketCommand.getItemsForPurchase(), itemId);
            marketItem.setStock(marketItem.getStock() - 1);
        }
        handleMarketSell.setGiveUpMode(false);
        handleMarketSell.setGiveUpFinished(true);
        handleMarketSell.setSuccessfulTransaction(true);
        handleMarketSell.setGold(character.getGold());
        return handleMarketSell;
    }

    private BuySellResponse handleMarket79(final String itemId, final HttpSessionWrapper wrapper) {
        final Paragraph paragraph = wrapper.getParagraph();
        final MarketCommand marketCommand = fetchMarketCommand(paragraph);
        final AttributeTestCommand skillCheckCommand = (AttributeTestCommand) marketCommand.getAfter().getCommands().get(0);
        final FfParagraphData data = skillCheckCommand.getSuccess().get(0).getData();
        final List<GatheredLostItem> lostItems = data.getLostItems();
        lostItems.clear();
        lostItems.add(new GatheredLostItem(itemId, 1, 0, false));
        blacklistItem(wrapper.getCharacter(), itemId);

        final BuySellResponse handleMarketSell = new BuySellResponse();
        handleMarketSell.setGiveUpMode(true);
        handleMarketSell.setGiveUpFinished(true);
        return handleMarketSell;
    }

    private MarketCommand fetchMarketCommand(final Paragraph paragraph) {
        return (MarketCommand) paragraph.getItemsToProcess().get(0).getCommand();
    }

    private int calculateSkillTestDifference(final BuySellResponse handleMarketSell, final SorCharacter character, final String itemName) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final int skill = characterHandler.getAttributeHandler().resolveValue(character, "skill");
        final int[] randomNumber = generator.getRandomNumber(2);
        final String diceRender = renderer.render(generator.getDefaultDiceSide(), randomNumber);

        String itemExchangeResult;
        if (skill >= randomNumber[0]) {
            // success
            int price = BASE_ITEM_SELLING_PRICE;
            if (!roundOne(characterHandler.getItemHandler(), character)) {
                price += skill - randomNumber[0];
            }
            itemExchangeResult = messageSource.getMessage("page.sor3.market.itemBought", new Object[]{price, itemName}, provider.getLocale());
        } else {
            // failure
            itemExchangeResult = messageSource.getMessage("page.sor3.market.itemDismissed", new Object[]{itemName}, provider.getLocale());
        }
        final String newText = messageSource.getMessage("page.ff.label.test.skill.compact", new Object[]{diceRender, randomNumber[0], itemExchangeResult},
            provider.getLocale());

        handleMarketSell.setText(newText);
        return skill - randomNumber[0];
    }

    private boolean roundOne(final FfCharacterItemHandler itemHandler, final SorCharacter character) {
        return !itemHandler.hasItem(character, "4076");
    }

    private void blacklistItem(final Character character, final String itemId) {
        final Map<String, String> userInteraction = character.getUserInteraction();
        final String blacklist = userInteraction.get("foodExchangeBlacklistedItems");
        if (blacklist == null) {
            userInteraction.put("foodExchangeBlacklistedItems", itemId);
        } else {
            userInteraction.put("foodExchangeBlacklistedItems", blacklist + "," + itemId);
        }
    }

}
