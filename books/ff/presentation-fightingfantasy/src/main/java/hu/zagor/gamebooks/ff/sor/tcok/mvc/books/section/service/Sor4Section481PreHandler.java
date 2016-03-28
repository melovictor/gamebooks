package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section pre-handler for SOR4, section 481.
 * @author Tamas_Szekeres
 */
@Component
public class Sor4Section481PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Character character = wrapper.getCharacter();
        final FfUserInteractionHandler interactionHandler = info.getCharacterHandler().getInteractionHandler();
        interactionHandler.getInteractionState(character, "tenUpTotal");
        interactionHandler.getInteractionState(character, "tenUpRound");
    }

}
