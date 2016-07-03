package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for section 188, FF34.
 * @author Tamas_Szekeres
 */
@Component
public class Ff34Section188PostHandler extends FfCustomPrePostSectionHandler {
    private static final String HEALING_SPELL_ID = "4104";

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Character character = wrapper.getCharacter();
        final FfCharacterHandler characterHandler = info.getCharacterHandler();
        final FfItem item = (FfItem) characterHandler.getItemHandler().getItem(character, HEALING_SPELL_ID);
        if (item != null) {
            final int initStamina = characterHandler.getAttributeHandler().resolveValue(character, "initialStamina");
            item.setStamina(initStamina / 2);
        }
    }

}
