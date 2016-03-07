package hu.zagor.gamebooks.ff.sor.tss.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = PageAddresses.BOOK_MARKET_SELL + "/{id}")
    @ResponseBody
    @Override
    public Map<String, Object> handleMarketSell(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        Map<String, Object> handleMarketSell;
        if ("79".equals(paragraph.getId())) {
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
        } else {
            handleMarketSell = super.handleMarketSell(request, itemId);
        }
        return handleMarketSell;
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
