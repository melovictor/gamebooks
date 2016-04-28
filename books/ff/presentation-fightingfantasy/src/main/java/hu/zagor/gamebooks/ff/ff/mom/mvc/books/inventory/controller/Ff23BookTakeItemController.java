package hu.zagor.gamebooks.ff.ff.mom.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.FfBookTakeItemController;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.MASKS_OF_MAYHEM)
public class Ff23BookTakeItemController extends FfBookTakeItemController {

    private static final int MAX_LUCK = 12;
    private static final int MAX_STAMINA = 24;
    private static final int MAX_SKILL = 12;
    private static final String FLASK = "3004";
    private static final int PROVISION_STAMINA_BONUS = 4;
    private static final int AMOUNT = 10;
    private static final String NOT_EATEN_FLAG = "4002";
    private static final String PROVISION = "2000";

    @Override
    protected String doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        if (PROVISION.equals(itemId)) {
            final Character character = wrapper.getCharacter();
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, NOT_EATEN_FLAG, AMOUNT);
        }
        return super.doHandleConsumeItem(wrapper, itemId);
    }

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        if (FLASK.equals(itemId)) {
            resetProvisions(wrapper);
        }

        int takeItemResult = 0;
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        if ("4003".equals(itemId)) {
            if (character.getGold() > 0) {
                character.setGold(character.getGold() - 1);
                getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "stamina", 2);
                takeItemResult = 1;
            }
        } else if ("4006".equals(itemId)) {
            character.changeSkill(MAX_SKILL);
            takeItemResult = 1;
        } else if ("4007".equals(itemId)) {
            character.changeStamina(MAX_STAMINA);
            takeItemResult = 1;
        } else if ("4008".equals(itemId)) {
            character.changeLuck(MAX_LUCK);
            takeItemResult = 1;
        } else if ("gold".equals(itemId) && "304".equals(wrapper.getParagraph().getId())) {
            takeItemResult = 2;
            character.setGold(character.getGold() + 2);
            character.changeStamina(-1);
        } else {
            takeItemResult = super.doHandleItemTake(request, itemId, amount);
        }

        return takeItemResult;
    }

    private void resetProvisions(final HttpSessionWrapper wrapper) {
        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final List<Item> items = itemHandler.getItems(character, PROVISION);
        for (final Item item : items) {
            ((FfItem) item).setStamina(PROVISION_STAMINA_BONUS);
        }
    }
}
