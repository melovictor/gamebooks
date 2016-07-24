package hu.zagor.gamebooks.ff.ff.sos.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.STEALER_OF_SOULS)
public class Ff34BookTakeItemController extends FfBookTakeItemController {

    private static final String CHAINMAIL_ID = "3025";
    private static final int MAX_IMPROVED_SKILL = 11;

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final TakeItemResponse doHandleItemTake = super.doHandleItemTake(request, itemId, amount);

        if (CHAINMAIL_ID.equals(itemId)) {
            enhanceChainMail(wrapper);
        }

        return doHandleItemTake;
    }

    private void enhanceChainMail(final HttpSessionWrapper wrapper) {
        final Character character = wrapper.getCharacter();
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final int initSkill = characterHandler.getAttributeHandler().resolveValue(character, "initialSkill");
        if (initSkill < MAX_IMPROVED_SKILL) {
            final FfItem item = (FfItem) characterHandler.getItemHandler().getItem(character, CHAINMAIL_ID);
            item.setInitialSkill(1);
        }
    }

}
