package hu.zagor.gamebooks.ff.wm.tds.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.paragraph.CharacterParagraphHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.ff.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.support.bookids.english.Warlock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_DERVISH_STONE)
public class Wm4BookTakeItemController extends FfBookTakeItemController {

    private static final String NO_EATING_AFTER_CITY_MARKER = "4011";
    private static final String CITY_LEAVING_SECION = "5";
    private static final String PROVISION = "2000";

    @Override
    protected ConsumeItemResponse doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {

        if (PROVISION.equals(itemId)) {
            final Character character = wrapper.getCharacter();
            final CharacterHandler characterHandler = getInfo().getCharacterHandler();
            final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
            final CharacterParagraphHandler paragraphHandler = characterHandler.getParagraphHandler();
            if (paragraphHandler.visitedParagraph(character, CITY_LEAVING_SECION)) {
                itemHandler.removeItem(character, NO_EATING_AFTER_CITY_MARKER, 1);
            }
        }

        return super.doHandleConsumeItem(wrapper, itemId);
    }
}
