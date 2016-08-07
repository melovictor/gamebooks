package hu.zagor.gamebooks.ff.ff.sots.mvc.books.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.sots.character.Ff20Character;
import hu.zagor.gamebooks.ff.ff.sots.character.SpecialSkill;
import hu.zagor.gamebooks.ff.mvc.book.newgame.controller.FfBookNewGameController;
import hu.zagor.gamebooks.ff.mvc.book.newgame.domain.FfPotionSelection;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SWORD_OF_THE_SAMURAI)
public class Ff20BookNewGameController extends FfBookNewGameController {

    @Override
    protected Map<String, Object> doGenerateCharacter(final HttpServletRequest request, final FfPotionSelection potionSelection) {
        final Map<String, Object> doGenerateCharacter = super.doGenerateCharacter(request, potionSelection);

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Ff20Character character = (Ff20Character) wrapper.getCharacter();
        character.setSpecialSkill(SpecialSkill.valueOf(request.getParameterMap().get("specialSkill")[0]));

        return doGenerateCharacter;
    }

}
