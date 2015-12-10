package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
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
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        final Character character = getWrapper(request).getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final Item item = itemHandler.getItem(character, itemId);
        if (item.getItemType() == ItemType.provision) {
            itemHandler.removeItem(character, "4101", REMOVED_HUNGER_MARKER_COUNT);
        }
        return super.doHandleConsumeItem(request, itemId);
    }

    /**
     * Handles Libra-supported attribute reset.
     * @param request the {@link HttpServletRequest} object
     * @param attrib the attribute to reset
     */
    @RequestMapping("libraReset/{attrib}")
    @ResponseBody
    public void handleLibraReset(final HttpServletRequest request, @PathVariable("attrib") final String attrib) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        if (itemHandler.hasItem(character, "4103")) {
            characterHandler.getAttributeHandler().handleModification(character, attrib, MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            itemHandler.removeItem(character, "4103", 1);
        }
    }
}
