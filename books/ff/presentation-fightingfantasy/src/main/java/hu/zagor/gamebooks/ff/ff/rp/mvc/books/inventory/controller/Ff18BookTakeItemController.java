package hu.zagor.gamebooks.ff.ff.rp.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.REBEL_PLANET)
public class Ff18BookTakeItemController extends FfBookTakeItemController {

    private static final int FRUIT_PRICE = 20;
    private static final int FRUIT_HEALING = 4;
    private static final int POTION_PRICE = 50;
    private static final int POTION_HEALING = 4;

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        int takenItems;
        if ("2001".equals(itemId)) {
            final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
            character.changeStamina(FRUIT_HEALING);
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            characterHandler.getAttributeHandler().handleModification(character, "gold", -FRUIT_PRICE);
            characterHandler.getItemHandler().addItem(character, "4001", 1);
            takenItems = 1;
        } else if ("2002".equals(itemId)) {
            final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
            character.changeStamina(POTION_HEALING);
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            characterHandler.getAttributeHandler().handleModification(character, "gold", -POTION_PRICE);
            takenItems = 1;
        } else {
            takenItems = super.doHandleItemTake(request, itemId, amount);
        }
        return takenItems;
    }
}
