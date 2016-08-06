package hu.zagor.gamebooks.ff.ff.sob.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SEAS_OF_BLOOD)
public class Ff16BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        TakeItemResponse takenItemAmount;
        if ("slave".equals(itemId)) {
            takenItemAmount = new TakeItemResponse();
            takenItemAmount.setTotalTaken(amount);
            getInfo().getCharacterHandler().getItemHandler().addItem(getWrapper(request).getCharacter(), itemId, amount);
        } else {
            takenItemAmount = super.doHandleItemTake(request, itemId, amount);
        }

        return takenItemAmount;
    }

}
