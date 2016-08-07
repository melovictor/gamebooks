package hu.zagor.gamebooks.ff.ff.tod.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.FfBookNewGameController;
import hu.zagor.gamebooks.ff.mvc.book.newgame.domain.FfPotionSelection;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.TALISMAN_OF_DEATH)
public class Ff11BookNewGameController extends FfBookNewGameController {

    @Override
    protected void initializeItems(final FfPotionSelection potionSelection, final FfCharacter character) {
        super.initializeItems(potionSelection, character);

        final String potionMarkerId = "41" + potionSelection.getPotion().substring(2, 4);
        getInfo().getCharacterHandler().getItemHandler().addItem(character, potionMarkerId, 1);
    }
}
