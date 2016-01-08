package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookTakeItemController extends FfBookTakeItemController {
    private static final int MAX_AMOUNT_OF_ATTRIBUTE_RESET = 24;
    private static final int REMOVED_HUNGER_MARKER_COUNT = 10;

    @Override
    protected void consumeSelectedItem(final FfCharacter character, final FfItem item) {
        if (item.getItemType() == ItemType.provision) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, "4101", REMOVED_HUNGER_MARKER_COUNT);
        }
        super.consumeSelectedItem(character, item);
    }

    /**
     * Handles Libra-supported attribute reset.
     * @param request the {@link HttpServletRequest} object
     */
    @RequestMapping("libraReset")
    @ResponseBody
    public void handleLibraReset(final HttpServletRequest request) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        if (itemHandler.hasItem(character, "4103")) {
            characterHandler.getAttributeHandler().handleModification(character, "stamina", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            characterHandler.getAttributeHandler().handleModification(character, "skill", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            characterHandler.getAttributeHandler().handleModification(character, "luck", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            itemHandler.removeItem(character, "4103", 1);
        }
    }

    /**
     * Handles Libra removing curses and sicknesses.
     * @param request the {@link HttpServletRequest} object
     * @param curseId the id of the curse to remove
     */
    @RequestMapping("libraRemoveCurse/{curseId}")
    @ResponseBody
    public void handleLibraRemoveCurse(final HttpServletRequest request, @PathVariable("curseId") final String curseId) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        if (itemHandler.hasItem(character, "4103")) {
            itemHandler.removeItem(character, curseId, 1);
            itemHandler.removeItem(character, "4103", 1);
        }
    }
}
