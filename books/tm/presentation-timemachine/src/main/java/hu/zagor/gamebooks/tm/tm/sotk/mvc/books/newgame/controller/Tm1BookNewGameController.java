package hu.zagor.gamebooks.tm.tm.sotk.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.bookids.english.TimeMachine;
import hu.zagor.gamebooks.tm.mvc.book.newgame.controller.TmBookNewGameController;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + TimeMachine.SECRET_OF_THE_KNIGHTS)
public class Tm1BookNewGameController extends TmBookNewGameController {

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler) {
        super.setUpCharacterHandler(wrapper, characterHandler);
        final Character character = wrapper.getCharacter();
        getInfo().getCharacterHandler().getItemHandler().addItem(character, "1001", 1);
    }
}
