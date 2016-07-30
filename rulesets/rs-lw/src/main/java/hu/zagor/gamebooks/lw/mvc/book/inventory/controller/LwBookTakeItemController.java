package hu.zagor.gamebooks.lw.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.complex.mvc.book.inventory.controller.ComplexBookTakeItemController;
import hu.zagor.gamebooks.complex.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.lw.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.lw.character.item.LwItem;
import hu.zagor.gamebooks.lw.content.LwParagraphData;
import hu.zagor.gamebooks.lw.domain.LwBookInformations;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import hu.zagor.gamebooks.support.messages.MessageSource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookTakeItemController extends ComplexBookTakeItemController<LwCharacter> {
    @Autowired @Qualifier("lwMarketHandler") private MarketHandler<LwCharacter> marketHandler;
    @Autowired private MessageSource messageSource;

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final TakeItemResponse response = super.doHandleItemTake(request, itemId, amount);
        String msgKeyPostfix = null;
        if (response.getTotalTaken() == 0) {
            if (amount == 1) {
                msgKeyPostfix = "singleItemNotTaken";
            } else {
                msgKeyPostfix = "multiItemsNotTaken";
            }
        } else if (response.getTotalTaken() < amount) {
            msgKeyPostfix = "notAllItemsTaken";
        }
        if (msgKeyPostfix != null) {
            response.setWarningMessage(messageSource.getMessage("page.book.inventory.takeItem." + msgKeyPostfix));
        }
        return response;
    }

    /**
     * Method for consuming an item (provision or potion).
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @return an object containing information about the result of the item consumption, can be null
     */
    @RequestMapping(value = PageAddresses.BOOK_CONSUME_ITEM + "/{id}")
    @ResponseBody
    public final ConsumeItemResponse handleConsumeItem(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        return doHandleConsumeItem(getWrapper(request), itemId);
    }

    /**
     * Method for actually consuming an item (provision or potion).
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the id of the item to change the state
     * @return an object containing information about the result of the item consumption, can be null
     */
    protected ConsumeItemResponse doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        String message = null;
        final Paragraph paragraph = wrapper.getParagraph();
        final LwCharacter character = (LwCharacter) wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        final LwCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final LwItem item = (LwItem) itemHandler.getItem(character, itemId);

        if (notFighting(commandView)) {

            if (isMeal(item)) {
                if (mustEat(paragraph)) {
                    eatMeal(paragraph, character, item);
                } else {
                    message = messageSource.getMessage("page.lw.equipment.eat.notRequiredEatingHere");
                }
            } else {
                consumeItem(character, item);
            }
        } else {
            message = messageSource.getMessage("page.lw.equipment.eat.notAllowedWhileFighting");
        }

        final ConsumeItemResponse response = new ConsumeItemResponse();
        response.setMessage(message);
        return response;
    }

    private void consumeItem(final LwCharacter character, final LwItem item) {
        final LwCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final LwCharacterItemHandler itemHandler = characterHandler.getItemHandler();
        itemHandler.removeItem(character, item.getId(), 1);
        characterHandler.getAttributeHandler().handleModification(character, "endurance", item.getEndurance());
        itemHandler.addItem(character, "50000", item.getCombatSkill());
    }

    private boolean isMeal(final LwItem item) {
        return item.getItemType() == ItemType.provision;
    }

    private boolean notFighting(final CommandView commandView) {
        return commandView == null || commandView.getViewName() == null || !commandView.getViewName().startsWith("lwFight");
    }

    private void eatMeal(final Paragraph paragraph, final LwCharacter character, final LwItem item) {
        final LwParagraphData data = (LwParagraphData) paragraph.getData();
        data.setMustEat(false);
        consumeItem(character, item);
    }

    private boolean mustEat(final Paragraph paragraph) {
        final LwParagraphData data = (LwParagraphData) paragraph.getData();
        return data.isMustEat();
    }

    @Override
    public LwBookInformations getInfo() {
        return (LwBookInformations) super.getInfo();
    }

    @Override
    protected MarketHandler<LwCharacter> getMarketHandler() {
        return marketHandler;
    }

    @Override
    protected LwCharacter getCharacter(final HttpSessionWrapper wrapper) {
        return (LwCharacter) wrapper.getCharacter();
    }
}
