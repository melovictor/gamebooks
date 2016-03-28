package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
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
    private static final int INITIAL_LUCK = 20;
    private static final String MEDICINAL_POTION_ID = "2005";
    private static final String HUNGER_MARKER_ID = "4101";
    private static final String LIBRA_HELP_AVAILABLE_MARKER_ID = "4103";
    private static final String BOMBA_ID = "2002";
    private static final String LUCK_COOKIE_ID = "2003";
    private static final int MAX_AMOUNT_OF_ATTRIBUTE_RESET = 30;
    private static final int REMOVED_HUNGER_MARKER_COUNT = 10;

    @Autowired private HttpServletRequest request;
    @Resource(name = "sorSicknesses") private Set<String> sicknesses;

    @Override
    protected void consumeSelectedItem(final FfCharacter characterObject, final FfItem item) {
        final SorCharacter character = (SorCharacter) characterObject;
        if (LUCK_COOKIE_ID.equals(item.getId())) {
            character.setLuckCookieActive(true);
        } else if (item.getItemType() == ItemType.provision) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, HUNGER_MARKER_ID, REMOVED_HUNGER_MARKER_COUNT);
        } else if (MEDICINAL_POTION_ID.equals(item.getId())) {
            removeRandomSickness(character);
        }
        final SorParagraphData data = (SorParagraphData) getWrapper(request).getParagraph().getData();
        data.setCanEat(false);
        character.setLastEatenBonus(item.getStamina());
        super.consumeSelectedItem(character, item);
    }

    private void removeRandomSickness(final SorCharacter character) {
        final List<Item> currentSicknesses = new ArrayList<>();
        for (final Item item : character.getEquipment()) {
            if (sicknesses.contains(item.getId())) {
                currentSicknesses.add(item);
            }
        }
        if (!currentSicknesses.isEmpty()) {
            Collections.shuffle(currentSicknesses);
            character.getEquipment().remove(currentSicknesses.get(0));
        }
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

    @Override
    protected void doHandleItemStateChange(final HttpServletRequest request, final String itemId, final boolean isEquipped) {
        if ("3072".equals(itemId)) {
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            final SorCharacter character = (SorCharacter) getWrapper(request).getCharacter();
            attributeHandler.handleModification(character, "luck", INITIAL_LUCK);
            final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
            itemHandler.removeItem(character, "3072", 1);
            itemHandler.addItem(character, "3073", 1);
        } else if ("3086".equals(itemId)) {
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            final SorCharacter character = (SorCharacter) getWrapper(request).getCharacter();
            attributeHandler.handleModification(character, "initialLuck", 1);
            attributeHandler.handleModification(character, "luck", INITIAL_LUCK);
            final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
            itemHandler.removeItem(character, "3086", 1);
        } else {
            super.doHandleItemStateChange(request, itemId, isEquipped);
        }

    }
}
