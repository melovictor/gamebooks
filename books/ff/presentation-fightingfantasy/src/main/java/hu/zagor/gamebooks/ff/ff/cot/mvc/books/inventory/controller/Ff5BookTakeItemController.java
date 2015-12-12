package hu.zagor.gamebooks.ff.ff.cot.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.CITY_OF_THIEVES)
public class Ff5BookTakeItemController extends FfBookTakeItemController {

    private static final String COLORED_CANDLE = "3032";

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
        int result = 0;
        if (!COLORED_CANDLE.equals(itemId) || character.getGold() > 0) {
            result = super.doHandleItemTake(request, itemId, amount);

            if (COLORED_CANDLE.equals(itemId)) {
                character.setGold(character.getGold() - 1);
                result = 0;
            }
        }

        return result;
    }
}
