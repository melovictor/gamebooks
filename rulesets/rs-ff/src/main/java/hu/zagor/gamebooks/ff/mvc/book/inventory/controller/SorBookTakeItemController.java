package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookTakeItemController extends FfBookTakeItemController {
    private static final String HUNGER_MARKER_ID = "4101";
    private static final String LIBRA_HELP_AVAILABLE_MARKER_ID = "4103";
    private static final String BOMBA_ID = "2002";
    private static final String LUCK_COOKIE_ID = "2003";
    private static final int MAX_AMOUNT_OF_ATTRIBUTE_RESET = 24;
    private static final int REMOVED_HUNGER_MARKER_COUNT = 10;

    @Autowired private HttpServletRequest request;

    @Override
    protected void consumeSelectedItem(final FfCharacter characterObject, final FfItem item) {
        final SorCharacter character = (SorCharacter) characterObject;
        if (LUCK_COOKIE_ID.equals(item.getId())) {
            character.setLuckCookieActive(true);
        } else if (item.getItemType() == ItemType.provision) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, HUNGER_MARKER_ID, REMOVED_HUNGER_MARKER_COUNT);
        }
        final SorParagraphData data = (SorParagraphData) getWrapper(request).getParagraph().getData();
        data.setCanEat(false);
        character.setLastEatenBonus(item.getStamina());
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
        if (itemHandler.hasItem(character, LIBRA_HELP_AVAILABLE_MARKER_ID)) {
            characterHandler.getAttributeHandler().handleModification(character, "stamina", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            characterHandler.getAttributeHandler().handleModification(character, "skill", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            characterHandler.getAttributeHandler().handleModification(character, "luck", MAX_AMOUNT_OF_ATTRIBUTE_RESET);
            itemHandler.removeItem(character, LIBRA_HELP_AVAILABLE_MARKER_ID, 1);
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
        if (itemHandler.hasItem(character, LIBRA_HELP_AVAILABLE_MARKER_ID)) {
            removeCurse(curseId, itemHandler, character);
        }
    }

    /**
     * Does the actual curse-removal.
     * @param curseId the ID of the curse to remove
     * @param itemHandler the {@link FfCharacterItemHandler} object
     * @param character the {@link SorCharacter} object
     */
    protected void removeCurse(final String curseId, final FfCharacterItemHandler itemHandler, final SorCharacter character) {
        itemHandler.removeItem(character, curseId, 1);
        itemHandler.removeItem(character, LIBRA_HELP_AVAILABLE_MARKER_ID, 1);
    }

    @Override
    protected boolean itemConsumptionAllowed(final Paragraph paragraph, final FfItem item) {
        final SorCharacter character = (SorCharacter) getWrapper(request).getCharacter();
        final boolean isBomba = BOMBA_ID.equals(item.getId());
        final boolean isBombaAfterEating = isBomba && character.getLastEatenBonus() > 0;
        if (isBombaAfterEating) {
            item.setStamina(character.getLastEatenBonus());
        }
        final boolean isLuckCookie = LUCK_COOKIE_ID.equals(item.getId());
        return (super.itemConsumptionAllowed(paragraph, item) && !isBomba) || isBombaAfterEating || isLuckCookie;
    }
}
