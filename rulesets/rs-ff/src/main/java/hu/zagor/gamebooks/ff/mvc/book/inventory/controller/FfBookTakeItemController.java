package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.complex.mvc.book.inventory.controller.ComplexBookTakeItemController;
import hu.zagor.gamebooks.complex.mvc.book.inventory.domain.ConsumeItemResponse;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.domain.TakePurchaseItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import hu.zagor.gamebooks.support.messages.MessageSource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookTakeItemController extends ComplexBookTakeItemController<FfCharacter> {

    @Autowired @Qualifier("ffMarketHandler") private MarketHandler<FfCharacter> marketHandler;
    @Autowired private MessageSource messageSource;

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param data the {@link TakePurchaseItemData} containing the incoming parameters
     * @return the amount of items successfully taken
     */
    @RequestMapping(value = PageAddresses.BOOK_PURCHASE_ITEM, method = RequestMethod.POST)
    @ResponseBody
    public final TakeItemResponse handleItemTake(final HttpServletRequest request, @ModelAttribute final TakePurchaseItemData data) {
        final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
        TakeItemResponse takeItemResult;
        if (data.getPrice() > 0 && character.getGold() < data.getPrice()) {
            takeItemResult = new TakeItemResponse();
        } else {
            takeItemResult = super.handleItemTake(request, data);
            character.setGold(character.getGold() - Math.min(takeItemResult.getTotalTaken(), 1) * data.getPrice());
        }
        return takeItemResult;
    }

    @Override
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        TakeItemResponse takenItemAmount = new TakeItemResponse();

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final int totalActions = paragraph.getActions();
        final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final int itemActions = getItemActions(itemId, itemHandler);

        if (totalActions >= itemActions) {
            takenItemAmount = super.doHandleItemTake(request, itemId, amount);
            paragraph.setActions(totalActions - itemActions);
        }

        return takenItemAmount;
    }

    private int getItemActions(final String itemId, final CharacterItemHandler itemHandler) {
        int itemActions = 1;
        if (!"gold".equals(itemId)) {
            final FfItem item = (FfItem) itemHandler.resolveItem(itemId);
            itemActions = item.getActions();
        }
        return itemActions;
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
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, itemId);
        if (notFighting(commandView)) {
            if (itemConsumptionAllowed(paragraph, item)) {
                final int totalActions = paragraph.getActions();
                final int consumeTime = item.getActions();
                if (totalActions >= consumeTime) {
                    paragraph.setActions(totalActions - consumeTime);
                    consumeSelectedItem(character, item);
                } else {
                    message = messageSource.getMessage("page.ff.equipment.eat.notEnoughActionPoints");
                }
            } else {
                message = messageSource.getMessage("page.ff.equipment.eat.notAllowedEatingHere");
            }
        } else {
            message = messageSource.getMessage("page.ff.equipment.eat.notWhileFighting");
        }
        final ConsumeItemResponse response = new ConsumeItemResponse();
        response.setMessage(message);
        return response;
    }

    /**
     * Checks whether the specific item is permitted for consumption at this location.
     * @param paragraph the {@link Paragraph} where we are
     * @param item the {@link FfItem} to consume
     * @return true if consumption is allowed, false otherwise
     */
    protected boolean itemConsumptionAllowed(final Paragraph paragraph, final FfItem item) {
        return !isFood(item) || canEatHere(paragraph);
    }

    /**
     * Handles the actual consumption, assuming that it has already been established that the item in question can indeed be consumed.
     * @param character the {@link FfCharacter} object
     * @param item the {@link FfItem} to consume
     */
    protected void consumeSelectedItem(final FfCharacter character, final FfItem item) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        characterHandler.getItemHandler().consumeItem(character, item.getId(), characterHandler.getAttributeHandler());
    }

    private boolean isFood(final FfItem item) {
        return item.getItemType() == ItemType.provision;
    }

    private boolean canEatHere(final Paragraph paragraph) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfParagraphData data = (FfParagraphData) paragraph.getData();
        return characterHandler.isCanEatEverywhere() || data.isCanEat();
    }

    private boolean notFighting(final CommandView commandView) {
        return commandView == null || commandView.getViewName() == null || !commandView.getViewName().startsWith("ffFight");
    }

    @Override
    public FfBookInformations getInfo() {
        return (FfBookInformations) super.getInfo();
    }

    protected MessageSource getMessageSource() {
        return messageSource;
    }

    @Override
    protected MarketHandler<FfCharacter> getMarketHandler() {
        return marketHandler;
    }

    @Override
    protected FfCharacter getCharacter(final HttpSessionWrapper wrapper) {
        return (FfCharacter) wrapper.getCharacter();
    }
}
