package hu.zagor.gamebooks.ff.ff.cotsw.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.CAVERNS_OF_THE_SNOW_WITCH)
public class Ff9BookTakeItemController extends FfBookTakeItemController {

    private static final int GOLD_AMOUNT_IN_SINGLE_TAKE = 50;
    private static final int MAX_TAKEABLE_GOLD_AMOUNT = 600;

    @Override
    public String doHandleItemReplace(final HttpServletRequest request, final String oldItemId, final String newItemId, final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final UserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();

        int actualAmount = amount;

        if ("171".equals(paragraph.getId()) && "gold".equals(newItemId) && amount > 0) {
            actualAmount = GOLD_AMOUNT_IN_SINGLE_TAKE;
        }

        String takenItems = super.doHandleItemReplace(request, oldItemId, newItemId, actualAmount);

        if ("171".equals(paragraph.getId()) && "gold".equals(newItemId) && amount > 0) {
            takenItems = handleSection171(takenItems, character, interactionHandler);
        }
        return takenItems;
    }

    private String handleSection171(final String takenItemsOrig, final FfCharacter character, final UserInteractionHandler interactionHandler) {
        String takenItems;
        final String goldTakenString = interactionHandler.getInteractionState(character, "171gold");
        int goldTaken;
        if (goldTakenString == null) {
            goldTaken = 0;
        } else {
            goldTaken = Integer.parseInt(goldTakenString);
        }

        if (goldTaken + GOLD_AMOUNT_IN_SINGLE_TAKE < MAX_TAKEABLE_GOLD_AMOUNT) {
            takenItems = String.valueOf(GOLD_AMOUNT_IN_SINGLE_TAKE);
            interactionHandler.setInteractionState(character, "171gold", String.valueOf(goldTaken + GOLD_AMOUNT_IN_SINGLE_TAKE));
        } else {
            takenItems = takenItemsOrig;
        }
        return takenItems;
    }

}
