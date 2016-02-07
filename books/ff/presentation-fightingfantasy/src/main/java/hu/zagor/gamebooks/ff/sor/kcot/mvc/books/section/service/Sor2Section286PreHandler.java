package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.service;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 286 pre handler for Sorcery 2. Add markers for how many gold pieces we have at the moment.
 * @author Tamas_Szekeres
 */
@Component
public class Sor2Section286PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();

        if (!itemHandler.hasItem(character, "4034")) {
            final int gold = character.getGold();
            if (gold > 0) {
                itemHandler.addItem(character, "4034", gold);
            }
        }
    }

}
