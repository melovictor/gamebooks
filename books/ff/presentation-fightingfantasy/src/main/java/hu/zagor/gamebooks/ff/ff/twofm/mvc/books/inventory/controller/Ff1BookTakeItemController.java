package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_WARLOCK_OF_FIRETOP_MOUNTAIN)
public class Ff1BookTakeItemController extends FfBookTakeItemController {

    private static final int FOOD_EFFECT_REDUCTION = -2;
    private static final int CANDLE_PRICE = 20;

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        int takenAmount;

        if ("3016".equals(itemId)) {
            final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
            if (character.getGold() >= CANDLE_PRICE) {
                takenAmount = super.doHandleItemTake(request, itemId, amount);
                getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "gold", -CANDLE_PRICE);
            } else {
                takenAmount = 0;
            }
        } else {
            if ("gold".equals(itemId) && "313".equals(getWrapper(request).getParagraph().getId())) {
                getInfo().getCharacterHandler().getItemHandler().addItem(getWrapper(request).getCharacter(), "4003", 1);
            }
            takenAmount = super.doHandleItemTake(request, itemId, amount);
        }

        return takenAmount;
    }

    @Override
    protected String doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        final Paragraph paragraph = wrapper.getParagraph();
        final String result = super.doHandleConsumeItem(wrapper, itemId);

        if ("131".equals(paragraph.getId()) && "2000".equals(itemId)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            character.changeStamina(FOOD_EFFECT_REDUCTION);
        }

        return result;
    }
}
