package hu.zagor.gamebooks.ff.ff.b.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.BLOODBONES)
public class Ff60BookTakeItemController extends FfBookTakeItemController {

    @Override
    protected String doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        if ("3013".equals(itemId)) {
            getInfo().getCharacterHandler().getItemHandler().addItem(wrapper.getCharacter(), "4000", 2);
        } else if ("2001".equals(itemId)) {
            getInfo().getCharacterHandler().getItemHandler().addItem(wrapper.getCharacter(), "4000", 1);
        } else if ("2002".equals(itemId)) {
            handleAntivenom(wrapper);
        }

        return super.doHandleConsumeItem(wrapper, itemId);
    }

    private void handleAntivenom(final HttpSessionWrapper wrapper) {
        final Ff60Character character = (Ff60Character) wrapper.getCharacter();
        final String paragraphId = wrapper.getParagraph().getId();
        if ("235".equals(paragraphId)) {
            final int half = character.getNineTailDamage() / 2;
            character.changeStamina(half);
            character.setNineTailDamage(0);
        } else if ("293".equals(paragraphId)) {
            character.changeStamina(character.getScarachnaPoison());
            character.setScarachnaPoison(0);
        }
    }

}
