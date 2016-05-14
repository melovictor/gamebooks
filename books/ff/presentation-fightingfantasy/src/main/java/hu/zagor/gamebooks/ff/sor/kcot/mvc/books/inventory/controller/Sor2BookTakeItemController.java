package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookTakeItemController extends SorBookTakeItemController {

    @Override
    protected BuySellResponse doHandleMarketSell(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final String sectionId = wrapper.getParagraph().getId();
        BuySellResponse handleMarketSell;
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
            interactionHandler.setInteractionState(character, "gnomeHagglingOriginalItems", itemList);
            handleMarketSell = new BuySellResponse();
            handleMarketSell.setGiveUpMode(false);
            handleMarketSell.setGiveUpFinished(true);
            handleMarketSell.setSuccessfulTransaction(true);
            handleMarketSell.setGold(character.getGold());
        } else {
            handleMarketSell = super.doHandleMarketSell(request, itemId);
        }
        return handleMarketSell;

    }

}
