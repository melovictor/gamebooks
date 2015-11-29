package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookTakeItemController extends FfBookTakeItemController {

    private static final int REMOVED_HUNGER_MARKER_COUNT = 10;

    @Override
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        final Character character = getWrapper(request).getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final Item item = itemHandler.getItem(character, itemId);
        if (item.getItemType() == ItemType.provision) {
            itemHandler.removeItem(character, "4000", REMOVED_HUNGER_MARKER_COUNT);
        }
        return super.doHandleConsumeItem(request, itemId);
    }

}
