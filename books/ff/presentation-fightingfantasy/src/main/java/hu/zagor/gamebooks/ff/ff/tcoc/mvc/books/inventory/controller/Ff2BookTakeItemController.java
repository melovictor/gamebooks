package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tcoc.character.Ff2Character;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.ff.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_CITADEL_OF_CHAOS)
public class Ff2BookTakeItemController extends FfBookTakeItemController {

    private static final String NORMAL_SWORD_ID = "1001";
    private static final String MAGIC_SWORD_ID = "1002";
    private static final String SPELL_POTION_ID = "2001";

    @Resource(name = "ff2SpellIds") private List<String> spells;
    @Resource(name = "ff2RestorationSpellIds") private List<String> resettingSpells;

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        if (MAGIC_SWORD_ID.equals(itemId)) {
            dropNormalSword(request);
        }
        return super.doHandleItemTake(request, itemId, amount);
    }

    @Override
    protected ConsumeItemResponse doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        ConsumeItemResponse response;
        if (isSpell(itemId)) {
            if (isResettingSpell(itemId) && !isFighting(wrapper)) {
                handleSpell(itemId, wrapper);
            }
            response = null;
        } else {
            if (SPELL_POTION_ID.equals(itemId) && !isFighting(wrapper)) {
                drinkPotion(wrapper);
            }
            response = super.doHandleConsumeItem(wrapper, itemId);
        }
        return response;
    }

    private void drinkPotion(final HttpSessionWrapper wrapper) {
        final Ff2Character character = (Ff2Character) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        if (character.getLastSpellCast() != null) {
            itemHandler.addItem(character, character.getLastSpellCast(), 1);
        }
        character.setLastSpellCast(null);
    }

    private void dropNormalSword(final HttpServletRequest request) {
        final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
        getInfo().getCharacterHandler().getItemHandler().removeItem(character, NORMAL_SWORD_ID, 1);
    }

    private void handleSpell(final String spellId, final HttpSessionWrapper wrapper) {
        final Ff2Character character = (Ff2Character) wrapper.getCharacter();
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final int attributeChange = attributeHandler.resolveValue(character, spellId) / 2;
        attributeHandler.handleModification(character, spellId, attributeChange);
        getInfo().getCharacterHandler().getItemHandler().removeItem(character, spellId, 1);
        character.setLastSpellCast(spellId);
    }

    private boolean isFighting(final HttpSessionWrapper wrapper) {
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

    /**
     * Handles acquiring a new spell in the middle of the story.
     * @param request the {@link HttpServletRequest} object
     * @param spellId the id of the spell to get
     */
    @RequestMapping("acquireNewSpell/{spellId}")
    @ResponseBody
    public void takeNewSpell(final HttpServletRequest request, @PathVariable("spellId") final String spellId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "4005") && !itemHandler.hasItem(character, "4006", 2)) {
            itemHandler.addItem(character, "4006", 1);
            final FfItem spell = (FfItem) itemHandler.getItem(character, spellId);
            if (spell == null) {
                itemHandler.addItem(character, spellId, 1);
            } else {
                spell.setAmount(spell.getAmount() + 1);
            }
        }

    }
}
