package hu.zagor.gamebooks.ff.wm.tdcoa.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Warlock;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_DARK_CHRONICLES_OF_ANAKENDIS)
public class Wm6BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        if ("1002".equals(itemId)) {
            final HttpSessionWrapper wrapper = getWrapper(request);
            final Character character = wrapper.getCharacter();
            final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "1001", 1);
        }
        return super.doHandleItemTake(request, itemId, amount);
    }
}
