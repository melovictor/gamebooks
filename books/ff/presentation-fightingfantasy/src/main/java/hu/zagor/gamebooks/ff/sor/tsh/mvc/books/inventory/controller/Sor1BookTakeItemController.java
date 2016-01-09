package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookTakeItemController extends SorBookTakeItemController {

    private static final String MANANKA_CURSE = "5001";

    @Override
    protected void removeCurse(final String curseId, final FfCharacterItemHandler itemHandler, final SorCharacter character) {
        if (MANANKA_CURSE.equals(curseId)) {
            character.setDamageProtection(0);
        }
        super.removeCurse(curseId, itemHandler, character);
    }

}
