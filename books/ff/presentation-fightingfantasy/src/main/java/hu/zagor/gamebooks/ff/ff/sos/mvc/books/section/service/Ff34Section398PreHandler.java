package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Pre section handler for section 398, FF34.
 * @author Tamas_Szekeres
 */
@Component
public class Ff34Section398PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfAttributeHandler attributeHandler = info.getCharacterHandler().getAttributeHandler();
        final int luck = attributeHandler.resolveValue(character, "luck");
        final int initLuck = attributeHandler.resolveValue(character, "initialLuck");
        if (initLuck == luck) {
            attributeHandler.handleModification(character, "initialLuck", 1);
        }
    }

}
