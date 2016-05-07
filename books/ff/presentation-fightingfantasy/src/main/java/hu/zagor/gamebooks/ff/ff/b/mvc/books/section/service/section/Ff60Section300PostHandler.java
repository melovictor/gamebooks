package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.section;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.ui.Model;

/**
 * Post section handler for FF60, section 300.
 * @author Tamas_Szekeres
 */
public class Ff60Section300PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final int stamina = info.getCharacterHandler().getAttributeHandler().resolveValue(character, "stamina");
        character.changeStamina(-stamina / 2);
    }

}
