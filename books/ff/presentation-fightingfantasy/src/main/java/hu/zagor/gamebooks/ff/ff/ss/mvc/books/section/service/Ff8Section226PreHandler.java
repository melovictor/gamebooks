package hu.zagor.gamebooks.ff.ff.ss.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Pre handler for section 226, FF8.
 * @author Tamas_Szekeres
 */
@Component
public class Ff8Section226PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        checkItem(character, itemHandler, "3004");
        checkItem(character, itemHandler, "3008");
        checkItem(character, itemHandler, "3010");
        checkItem(character, itemHandler, "3011");
        checkItem(character, itemHandler, "3012");
    }

    private void checkItem(final Character character, final FfCharacterItemHandler itemHandler, final String amuletId) {
        if (itemHandler.hasItem(character, amuletId)) {
            itemHandler.addItem(character, "4030", 1);
        }
    }

}
