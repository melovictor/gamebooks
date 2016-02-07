package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.HashMap;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookTakeItemController extends SorBookTakeItemController {

    @RequestMapping(value = PageAddresses.BOOK_MARKET_SELL + "/{id}")
    @ResponseBody
    @Override
    public Map<String, Object> handleMarketSell(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final String sectionId = wrapper.getParagraph().getId();
        Map<String, Object> handleMarketSell;
        if ("264".equals(sectionId)) {
            final SorCharacter character = (SorCharacter) wrapper.getCharacter();
            final FfUserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();
            String itemList = interactionHandler.getInteractionState(character, "gnomeHagglingItems");
            if (itemList == null) {
                itemList = itemId;
            } else {
                itemList += "," + itemId;
            }
            interactionHandler.setInteractionState(character, "gnomeHagglingItems", itemList);
            handleMarketSell = new HashMap<>();
            handleMarketSell.put("giveUpMode", false);
            handleMarketSell.put("giveUpFinished", true);
            handleMarketSell.put("successfulTransaction", true);
            handleMarketSell.put("gold", character.getGold());
        } else {
            handleMarketSell = super.handleMarketSell(request, itemId);
        }
        return handleMarketSell;

    }

}
