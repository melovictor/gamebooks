package hu.zagor.gamebooks.ff.ff.twofm.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.complex.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.content.Paragraph;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_WARLOCK_OF_FIRETOP_MOUNTAIN)
public class Ff1BookTakeItemController extends FfBookTakeItemController {

    private static final int FOOD_EFFECT_REDUCTION = -2;
    private static final int CANDLE_PRICE = 20;

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        TakeItemResponse takenAmount;

        if ("3016".equals(itemId)) {
            final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
            if (character.getGold() >= CANDLE_PRICE) {
                takenAmount = super.doHandleItemTake(request, itemId, amount);
                getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "gold", -CANDLE_PRICE);
            } else {
                takenAmount = new TakeItemResponse(0);
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
    protected ConsumeItemResponse doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        final Paragraph paragraph = wrapper.getParagraph();
        final ConsumeItemResponse result = super.doHandleConsumeItem(wrapper, itemId);

        if ("131".equals(paragraph.getId()) && "2000".equals(itemId)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            character.changeStamina(FOOD_EFFECT_REDUCTION);
        }

        return result;
    }
}
