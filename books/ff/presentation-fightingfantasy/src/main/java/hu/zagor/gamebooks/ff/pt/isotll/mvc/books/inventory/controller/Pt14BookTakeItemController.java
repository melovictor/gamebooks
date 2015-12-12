package hu.zagor.gamebooks.ff.pt.isotll.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Proteus;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Proteus.IN_SEARCH_OF_THE_LOST_LAND)
public class Pt14BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        if ("3014".equals(itemId)) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(getWrapper(request).getCharacter(), "3013", 1);
        }
        return super.doHandleItemTake(request, itemId, amount);
    }
}
