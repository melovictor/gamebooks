package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.tcoc.character.Ff2Character;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.newgame.service.Ff2CharacterGenerator;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.FfBookNewGameController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_CITADEL_OF_CHAOS)
public class Ff2BookNewGameController extends FfBookNewGameController {

    /**
     * Finalizes the character generation process.
     * @param request the {@link HttpServletRequest} object
     * @param spells the spells selected by the user
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "/" + PageAddresses.BOOK_GENERATE_CHARACTER + "2")
    @ResponseBody
    public void handleGenerate2(final HttpServletRequest request, @RequestParam("spells") final String spells) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Ff2Character character = (Ff2Character) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final Ff2CharacterGenerator characterGenerator = (Ff2CharacterGenerator) characterHandler.getCharacterGenerator();
        characterGenerator.finalizeCharacter(character, spells, characterHandler);
    }

}
