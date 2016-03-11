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
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SEVEN_SERPENTS)
public class Sor3BookTakeItemController extends FfBookTakeItemController {

    private static final int BASE_ITEM_SELLING_PRICE = 3;

    @Autowired private LocaleProvider provider;
    @Autowired private MessageSource messageSource;
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @RequestMapping(value = PageAddresses.BOOK_MARKET_SELL + "/{id}")
    @ResponseBody
    @Override
    public Map<String, Object> handleMarketSell(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        Map<String, Object> handleMarketSell;
        if ("79".equals(paragraph.getId())) {
            handleMarketSell = handleMarket79(itemId, wrapper);
        } else if ("315".equals(paragraph.getId())) {
            handleMarketSell = handleMarket315(itemId, wrapper);
        } else {
            handleMarketSell = super.handleMarketSell(request, itemId);
        }
        return handleMarketSell;
    }

    private Map<String, Object> handleMarket315(final String itemId, final HttpSessionWrapper wrapper) {
        Map<String, Object> handleMarketSell;
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        handleMarketSell = new HashMap<>();

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
        }
        handleMarketSell.put("giveUpMode", false);
        handleMarketSell.put("giveUpFinished", true);
        handleMarketSell.put("successfulTransaction", true);
        handleMarketSell.put("gold", character.getGold());
        return handleMarketSell;
    }

    private Map<String, Object> handleMarket79(final String itemId, final HttpSessionWrapper wrapper) {
        final Paragraph paragraph = wrapper.getParagraph();
        Map<String, Object> handleMarketSell;
        final MarketCommand marketCommand = (MarketCommand) paragraph.getItemsToProcess().get(0).getCommand();
        final AttributeTestCommand skillCheckCommand = (AttributeTestCommand) marketCommand.getAfter().getCommands().get(0);
        final FfParagraphData data = skillCheckCommand.getSuccess().get(0).getData();
        final List<GatheredLostItem> lostItems = data.getLostItems();
        lostItems.clear();
        lostItems.add(new GatheredLostItem(itemId, 1, 0, false));
        blacklistItem(wrapper.getCharacter(), itemId);

        handleMarketSell = new HashMap<>();
        handleMarketSell.put("giveUpMode", true);
        handleMarketSell.put("giveUpFinished", true);
        return handleMarketSell;
    }

    private int calculateSkillTestDifference(final Map<String, Object> handleMarketSell, final SorCharacter character, final String itemName) {
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

        handleMarketSell.put("text", newText);
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
