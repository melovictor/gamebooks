package hu.zagor.gamebooks.tm.mvc.book.newgame.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.raw.mvc.book.newgame.controller.RawBookNewGameController;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

/**
 * Generic new game handling controller for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmBookNewGameController extends RawBookNewGameController {

    @Override
    public String handleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        super.handleNew(request, model, locale);
        addCssResource(model, "tm");
        return "tmSection";
    }

    @Override
    public Character getCharacter(final Locale locale) {
        final Character character = super.getCharacter(locale);
        character.setBackpackSize(Integer.MAX_VALUE);

        final String databank = getApplicationContext().getMessage(getInfo().getResourceDir() + ".databank", null, locale);
        character.setNotes(databank);

        return character;
    }

}
