package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 56 pre handler for Sorcery 2. Record gold pieces we have before entering the gambling halls.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2Section56PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        interactionHandler.setInteractionState(character, "goldBeforeGambling", String.valueOf(character.getGold()));
    }

}
