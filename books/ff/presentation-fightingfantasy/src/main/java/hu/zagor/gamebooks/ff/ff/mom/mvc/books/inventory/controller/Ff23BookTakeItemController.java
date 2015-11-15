package hu.zagor.gamebooks.ff.ff.mom.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.MASKS_OF_MAYHEM)
public class Ff23BookTakeItemController extends FfBookTakeItemController {

    private static final String FLASK = "3014";
    private static final int PROVISION_STAMINA_BONUS = 4;
    private static final int AMOUNT = 10;
    private static final String NOT_EATEN_FLAG = "4002";
    private static final String PROVISION = "2000";

    @Override
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        if (PROVISION.equals(itemId)) {
            final Character character = getWrapper(request).getCharacter();
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, NOT_EATEN_FLAG, AMOUNT);
        }
        return super.doHandleConsumeItem(request, itemId);
    }

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        if (FLASK.equals(itemId)) {
            resetProvisions(request);
        }
        return super.doHandleItemTake(request, itemId, amount);
    }

    private void resetProvisions(final HttpServletRequest request) {
        final Character character = getWrapper(request).getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final List<Item> items = itemHandler.getItems(character, PROVISION);
        for (final Item item : items) {
            ((FfItem) item).setStamina(PROVISION_STAMINA_BONUS);
        }
    }
}
