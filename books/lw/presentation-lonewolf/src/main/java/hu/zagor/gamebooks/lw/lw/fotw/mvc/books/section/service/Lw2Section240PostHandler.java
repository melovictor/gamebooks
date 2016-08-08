package hu.zagor.gamebooks.lw.lw.fotw.mvc.books.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section post handler for LW2, section 240.
 * @author Tamas_Szekeres
 */
@Component
public class Lw2Section240PostHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();

        int toRestore = character.getInitialEndurance() - character.getEndurance();
        if (!character.getKaiDisciplines().isHealing()) {
            toRestore /= 2;
        }

        final LwAttributeHandler attributeHandler = (LwAttributeHandler) info.getCharacterHandler().getAttributeHandler();
        attributeHandler.handleModification(character, "endurance", toRestore);
    }

}
