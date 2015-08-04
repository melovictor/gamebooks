package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tcoc.character.Ff2Character;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_CITADEL_OF_CHAOS)
public class Ff2BookTakeItemController extends FfBookTakeItemController {

    private final List<String> spells = Arrays.asList("stamina", "strength", "weakness", "foolsGold", "illusion", "esp", "levitation", "luck", "creatureCopy", "fire",
        "skill", "shielding");
    private final List<String> resettingSpells = Arrays.asList("stamina", "luck", "skill");

    @Override
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        String response;
        if (isSpell(itemId)) {
            if (isResettingSpell(itemId) && !isFighting(request)) {
                handleSpell(itemId, request);
            }
            response = null;
        } else {
            response = super.doHandleConsumeItem(request, itemId);
        }
        return response;
    }

    private void handleSpell(final String spellId, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Ff2Character character = (Ff2Character) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) getInfo().getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final int attributeChange = attributeHandler.resolveValue(character, spellId) / 2;
        attributeHandler.handleModification(character, spellId, attributeChange);
        getInfo().getCharacterHandler().getItemHandler().removeItem(character, spellId, 1);
    }

    private boolean isFighting(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        return commandView != null && commandView.getViewName().startsWith("ffFight");
    }

    private boolean isResettingSpell(final String spellId) {
        return resettingSpells.contains(spellId);
    }

    private boolean isSpell(final String itemId) {
        return spells.contains(itemId);
    }
}
