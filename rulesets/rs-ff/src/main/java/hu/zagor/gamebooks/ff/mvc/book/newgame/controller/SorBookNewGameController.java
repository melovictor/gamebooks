package hu.zagor.gamebooks.ff.mvc.book.newgame.controller;

import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
public class SorBookNewGameController extends FfBookNewGameController {
    @Override
    public Map<String, Object> generateCharacter(final HttpServletRequest request) {
        final Map<String, Object> results = super.generateCharacter(request);
        final SorCharacter character = (SorCharacter) getWrapper(request).getCharacter();
        if (character.isWizard()) {
            getInfo().getCharacterHandler().getItemHandler().addItem(character, "4102", 1);
        }
        return results;
    }
}
