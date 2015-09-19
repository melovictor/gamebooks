package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_WARLOCK_OF_FIRETOP_MOUNTAIN)
public class Ff1BookTakeItemController extends FfBookTakeItemController {

    private static final int CANDLE_PRICE = 20;

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        int takenAmount;

        if ("3016".equals(itemId)) {
            final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
            if (character.getGold() >= CANDLE_PRICE) {
                takenAmount = super.doHandleItemTake(request, itemId, amount);
                getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "gold", -CANDLE_PRICE);
            } else {
                takenAmount = 0;
            }
        } else {
            takenAmount = super.doHandleItemTake(request, itemId, amount);
        }

        return takenAmount;
    }
}
