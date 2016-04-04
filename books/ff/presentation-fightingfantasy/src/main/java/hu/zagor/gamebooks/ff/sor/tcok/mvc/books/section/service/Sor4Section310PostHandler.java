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
 * Post section handler for Sor4, section 310.
 * @author Tamas_Szekeres
 */
@Component
public class Sor4Section310PostHandler extends FfCustomPrePostSectionHandler {

    private static final int SILVER_COIN_REMOVED_ITEM_INDEX = 3;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = info.getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "3094")) {
            itemHandler.removeItem(character, "3094", 1);
            final List<Item> equipment = character.getEquipment();
            if (equipment.size() < SILVER_COIN_REMOVED_ITEM_INDEX) {
                equipment.remove(equipment.size() - 1);
            } else {
                equipment.remove(2);
            }
        }
    }

}
