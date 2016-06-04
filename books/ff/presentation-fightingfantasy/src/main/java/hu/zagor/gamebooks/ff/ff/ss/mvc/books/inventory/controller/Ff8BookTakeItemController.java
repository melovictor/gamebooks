package hu.zagor.gamebooks.ff.ff.ss.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SCORPION_SWAMP)
public class Ff8BookTakeItemController extends FfBookTakeItemController {
    @Override
    protected String doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {

        final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        if ("3101".equals(itemId)) {
            final int initSkill = attributeHandler.resolveValue(character, "initialSkill");
            character.changeSkill(initSkill / 2);
        } else if ("3102".equals(itemId)) {
            final int initStamina = attributeHandler.resolveValue(character, "initialStamina");
            character.changeStamina(initStamina / 2);
        } else if ("3103".equals(itemId)) {
            final int initLuck = attributeHandler.resolveValue(character, "initialLuck");
            character.changeLuck(initLuck / 2);
        }

        return super.doHandleConsumeItem(wrapper, itemId);
    }
}
