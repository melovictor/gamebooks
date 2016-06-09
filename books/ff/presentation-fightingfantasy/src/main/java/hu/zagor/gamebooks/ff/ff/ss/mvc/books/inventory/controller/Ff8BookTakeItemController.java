package hu.zagor.gamebooks.ff.ff.ss.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.ss.character.Ff8Character;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
import hu.zagor.gamebooks.ff.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.SCORPION_SWAMP)
public class Ff8BookTakeItemController extends FfBookTakeItemController {
    private static final int MAX_SELLABLE_ITEMS = 3;

    @Override
    protected ConsumeItemResponse doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {

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

    @Override
    protected BuySellResponse doHandleMarketSell(final HttpServletRequest request, final String itemId) {
        BuySellResponse result;
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        if ("150".equals(paragraph.getId())) {
            final Ff8Character character = (Ff8Character) wrapper.getCharacter();
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            final List<Item> items = itemHandler.getItems(character, "4029");
            if (items.size() >= MAX_SELLABLE_ITEMS) {
                result = new BuySellResponse();
                result.setSuccessfulTransaction(false);
                result.setGold(character.getSpellItem());
            } else {
                itemHandler.addItem(character, "4029", 1);
                result = super.doHandleMarketSell(request, itemId);
            }

        } else {
            result = super.doHandleMarketSell(request, itemId);
        }
        return result;
    }
}
