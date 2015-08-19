package hu.zagor.gamebooks.ff.ff.iotlk.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.ISLAND_OF_THE_LIZARD_KING)
public class Ff7BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        if ("1003".equals(itemId)) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            final Character character = getWrapper(request).getCharacter();
            itemHandler.removeItem(character, "1001", 1);
            itemHandler.removeItem(character, "1002", 1);
        }
        return super.doHandleItemTake(request, itemId, amount);
    }
}
