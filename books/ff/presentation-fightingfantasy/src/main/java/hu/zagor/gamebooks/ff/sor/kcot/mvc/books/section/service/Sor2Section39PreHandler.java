package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 39 pre handler for Sorcery 2. When we leave the gambling halls with more money than we had originally, should ad winning marker.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2Section39PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();

        final int currentGold = character.getGold();
        final int previousGold = Integer.valueOf(interactionHandler.getInteractionState(character, "goldBeforeGambling"));
        if (previousGold < currentGold) {
            itemHandler.addItem(character, "4031", 1);
        }
    }

}
