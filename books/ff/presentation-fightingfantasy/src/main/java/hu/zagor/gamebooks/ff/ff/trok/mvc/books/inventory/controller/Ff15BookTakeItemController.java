package hu.zagor.gamebooks.ff.ff.trok.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_RINGS_OF_KETHER)
public class Ff15BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final int itemsTaken = super.doHandleItemTake(request, itemId, amount);

        if (itemsTaken > 0 && "1002".equals(itemId)) {
            final Character character = getWrapper(request).getCharacter();
            final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "1001", 1);
        }

        return itemsTaken;
    }
}
