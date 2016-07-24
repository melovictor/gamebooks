package hu.zagor.gamebooks.ff.ff.hoh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.HOUSE_OF_HELL)
public class Ff10BookTakeItemController extends FfBookTakeItemController {

    private static final int WINE_FEAR_MODIFICATION = -2;

    @Override
    public TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final TakeItemResponse handleItemTake = super.doHandleItemTake(request, itemId, amount);

        if ("5007".equals(itemId) || "5008".equals(itemId)) {
            final HttpSessionWrapper wrapper = getWrapper(request);
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            attributeHandler.handleModification(character, "fear", WINE_FEAR_MODIFICATION);
        }

        return handleItemTake;
    }

}
