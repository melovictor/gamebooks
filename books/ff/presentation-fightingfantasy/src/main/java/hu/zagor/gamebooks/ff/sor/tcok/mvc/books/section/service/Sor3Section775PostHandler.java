package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.service;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for SOR3.
 * @author Tamas_Szekeres
 */
@Component
public class Sor3Section775PostHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        final Character character = wrapper.getCharacter();
        final List<Item> rocks = itemHandler.getItems(character, "3019");
        if (!rocks.isEmpty()) {
            itemHandler.addItem(character, "3019a", rocks.size());
        }
    }

}
