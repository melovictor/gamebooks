package hu.zagor.gamebooks.ff.ff.votv.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.VAULT_OF_THE_VAMPIRE)
public class Ff38BookTakeItemController extends FfBookTakeItemController {

    private static final String GHOST_IN_ROOM = "98";

    private static final int LUCK_BONUS = 3;
    private static final String LUCK_SPELL = "4216";
    private static final String TRUE_HEALING = "4218";

    private static final String MEAL = "2000";

    private static final String STRONG_HIT = "4214";
    private static final String STRONG_HIT_ACTIVE = "4013";
    private static final String BASH = "4217";
    private static final String BASH_ACTIVE = "4015";
    private static final String JANDOR_ARROW = "4215";
    private static final String JANDOR_ARROW_ACTIVE = "4014";

    private static final String EAT_WHILE_HEYDRICH_REGENERATES = "4021";
    private static final String HEYDRICH_REGENERATES = "63";

    @Override
    public String doHandleConsumeItem(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final String id = paragraph.getId();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) getInfo().getCharacterHandler();
        final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
        String response = null;

        if (isSpell(itemId)) {
            if (haveTimeForSpell(paragraph) && !hasActiveSpell(character, itemHandler)) {
                response = handleSpell(request, itemId, character, characterHandler, itemHandler);
            }
        } else if (!GHOST_IN_ROOM.equals(id) && canEatWhileHeydrichRegenerates(id, itemId, character, itemHandler)) {
            super.doHandleConsumeItem(request, itemId);
        }

        return response;
    }

    private boolean haveTimeForSpell(final Paragraph paragraph) {
        final int actions = paragraph.getActions();
        if (actions > 0) {
            paragraph.setActions(actions - 1);
        }
        return actions > 0;
    }

    private boolean canEatWhileHeydrichRegenerates(final String id, final String itemId, final FfCharacter character, final CharacterItemHandler itemHandler) {
        boolean canEatNow = true;
        if (MEAL.equals(itemId) && HEYDRICH_REGENERATES.equals(id)) {
            if (itemHandler.hasItem(character, EAT_WHILE_HEYDRICH_REGENERATES)) {
                canEatNow = false;
            } else {
                itemHandler.addItem(character, EAT_WHILE_HEYDRICH_REGENERATES, 1);
            }
        }
        return canEatNow;
    }

    private String handleSpell(final HttpServletRequest request, final String itemId, final FfCharacter character, final FfCharacterHandler characterHandler,
        final CharacterItemHandler itemHandler) {
        String response = null;
        if (isFighting(request)) {
            response = handleFightingSpell(itemId, character, itemHandler);
        } else {
            handleNonFightingSpell(itemId, character, characterHandler, itemHandler);
        }
        return response;
    }

    private void handleNonFightingSpell(final String itemId, final FfCharacter character, final FfCharacterHandler characterHandler,
        final CharacterItemHandler itemHandler) {
        if (LUCK_SPELL.equals(itemId)) {
            character.changeLuck(LUCK_BONUS);
            itemHandler.removeItem(character, itemId, 1);
        } else if (TRUE_HEALING.equals(itemId)) {
            final int additionalStamina = characterHandler.getAttributeHandler().resolveValue(character, "initialStamina") / 2;
            character.changeStamina(additionalStamina);
            itemHandler.removeItem(character, itemId, 1);
        }
    }

    private String handleFightingSpell(final String itemId, final FfCharacter character, final CharacterItemHandler itemHandler) {
        String response = null;
        if (STRONG_HIT.equals(itemId)) {
            itemHandler.addItem(character, STRONG_HIT_ACTIVE, 1);
            itemHandler.removeItem(character, itemId, 1);
            response = "#ffAttackButton button";
        } else if (JANDOR_ARROW.equals(itemId)) {
            itemHandler.addItem(character, JANDOR_ARROW_ACTIVE, 1);
            itemHandler.removeItem(character, itemId, 1);
            response = "#ffAttackButton button";
        } else if (BASH.equals(itemId)) {
            itemHandler.addItem(character, BASH_ACTIVE, 1);
            itemHandler.removeItem(character, itemId, 1);
            response = "#ffAttackButton button";
        }
        return response;
    }

    private boolean hasActiveSpell(final FfCharacter character, final CharacterItemHandler itemHandler) {
        return itemHandler.hasItem(character, JANDOR_ARROW_ACTIVE) || itemHandler.hasItem(character, STRONG_HIT_ACTIVE) || itemHandler.hasItem(character, BASH_ACTIVE);
    }

    private boolean isFighting(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        return commandView != null && commandView.getViewName().startsWith("ffFight");
    }

    private boolean isSpell(final String itemId) {
        return itemId.startsWith("42");
    }

}
