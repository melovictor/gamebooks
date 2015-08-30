package hu.zagor.gamebooks.ff.wm.tfc.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_FLOATING_CITY)
public class Wm8BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        if ("74".equals(wrapper.getParagraph())) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            final Character character = wrapper.getCharacter();
            final FfItem item = (FfItem) itemHandler.getItem(character, itemId);
            if (item.getItemType() == ItemType.provision) {
                itemHandler.addItem(character, "4001", 1);
            }
        }

        return super.doHandleConsumeItem(request, itemId);
    }
}
