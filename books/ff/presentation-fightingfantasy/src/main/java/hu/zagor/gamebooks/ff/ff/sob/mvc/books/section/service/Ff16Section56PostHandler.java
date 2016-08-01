package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for FF16, section 56.
 * @author Tamas_Szekeres
 */
@Component
public class Ff16Section56PostHandler extends FfCustomPrePostSectionHandler {

    private static final int GIANT_ADDITION_STRENGTH = 3;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Ff16Character character = (Ff16Character) wrapper.getCharacter();
        final int maxCrewStrength = character.getInitialCrewStrength();
        final int newCrewStrength = character.getCrewStrength() + GIANT_ADDITION_STRENGTH;
        if (newCrewStrength > maxCrewStrength) {
            character.setInitialCrewStrength(newCrewStrength);
            character.setCrewStrength(newCrewStrength);
        }
    }

}
