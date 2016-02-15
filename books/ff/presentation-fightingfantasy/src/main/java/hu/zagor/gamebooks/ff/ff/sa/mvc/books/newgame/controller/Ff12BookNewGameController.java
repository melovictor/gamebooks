package hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.service.Ff12CharacterGenerator;
import hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.service.Ff12WeaponChoice;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.FfBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SPACE_ASSASSIN)
public class Ff12BookNewGameController extends FfBookNewGameController {

    /**
     * Finalizes the character generation process.
     * @param request the {@link HttpServletRequest} object
     * @param userSelection the weapons and armour selected by the user
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER + "2")
    @ResponseBody
    public void handleGenerate2(final HttpServletRequest request, final Ff12WeaponChoice userSelection) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Ff12Character character = (Ff12Character) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final Ff12CharacterGenerator characterGenerator = (Ff12CharacterGenerator) characterHandler.getCharacterGenerator();
        characterGenerator.finalizeCharacter(character, userSelection, characterHandler);
    }

}
