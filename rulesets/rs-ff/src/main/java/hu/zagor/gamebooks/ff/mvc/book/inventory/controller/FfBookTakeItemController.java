package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.inventory.domain.TakePurchaseItemData;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookTakeItemController extends GenericBookTakeItemController {

    @Autowired private MarketHandler marketHandler;

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param data the {@link TakePurchaseItemData} containing the incoming parameters
     * @return the amount of items successfully taken
     */
    @RequestMapping(value = PageAddresses.BOOK_PURCHASE_ITEM, consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public final int handleItemTake(final HttpServletRequest request, @RequestBody final TakePurchaseItemData data) {
        final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
        int takeItemResult;
        if (data.getPrice() > 0 && character.getGold() < data.getPrice()) {
            takeItemResult = 0;
        } else {
            takeItemResult = super.handleItemTake(request, data);
            character.setGold(character.getGold() - Math.min(takeItemResult, 1) * data.getPrice());
        }
        return takeItemResult;
    }

    @Override
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        int takenItemAmount = 0;

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
     * Method for handling the changing of item state.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @param isEquipped true if the item has to be equipped, false if it has to be removed
     */
    @RequestMapping(value = PageAddresses.BOOK_CHANGE_ITEM_EQUIP_STATE + "/{id}/{isEquipped}")
    @ResponseBody
    public final void handleItemStateChange(final HttpServletRequest request, @PathVariable("id") final String itemId,
        @PathVariable("isEquipped") final boolean isEquipped) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        doHandleItemStateChange(request, itemId, isEquipped);
    }

    /**
     * Method for actually handling the changing of the item state.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @param isEquipped true if the item has to be equipped, false if it has to be removed
     */
    protected void doHandleItemStateChange(final HttpServletRequest request, final String itemId, final boolean isEquipped) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();

        final int totalActions = paragraph.getActions();
        if (totalActions > 0) {
            paragraph.setActions(totalActions - 1);
            final Character character = wrapper.getCharacter();
            final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
            characterHandler.getItemHandler().setItemEquipState(character, itemId, isEquipped);
            getItemInteractionRecorder().changeItemEquipState(wrapper, itemId);
        }
    }

    /**
     * Method for consuming an item (provision or potion).
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @return null
     */
    @RequestMapping(value = PageAddresses.BOOK_CONSUME_ITEM + "/{id}")
    @ResponseBody
    public final String handleConsumeItem(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        return doHandleConsumeItem(getWrapper(request), itemId);
    }

    /**
     * Method for actually consuming an item (provision or potion).
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the id of the item to change the state
     * @return usually nothing
     */
    protected String doHandleConsumeItem(final HttpSessionWrapper wrapper, final String itemId) {
        getItemInteractionRecorder().recordItemConsumption(wrapper, itemId);
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
                }
            }
        }
        return null;
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

    /**
     * Method for buying an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to buy
     * @return the response for taking the item
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_BUY + "/{id}")
    @ResponseBody
    public final Map<String, Object> handleMarketBuy(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        return doHandleMarketBuy(request, itemId);
    }

    /**
     * Method for actually buying an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to buy
     * @return the response for taking the item
     */
    protected Map<String, Object> doHandleMarketBuy(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        final Map<String, Object> result = marketHandler.handleMarketPurchase(itemId, character, wrapper.getParagraph().getItemsToProcess().get(0).getCommand(),
            getInfo().getCharacterHandler());
        getItemInteractionRecorder().recordItemMarketMovement(wrapper, "Sale", itemId);

        return result;
    }

    /**
     * Method for selling an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to buy
     * @return the data about the sale
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_SELL + "/{id}")
    @ResponseBody
    public final Map<String, Object> handleMarketSell(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        return doHandleMarketSell(request, itemId);
    }

    /**
     * Method for actually selling an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to buy
     * @return the data about the sale
     */
    protected Map<String, Object> doHandleMarketSell(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        final Map<String, Object> result = marketHandler.handleMarketSell(itemId, character, wrapper.getParagraph().getItemsToProcess().get(0).getCommand(),
            getInfo().getCharacterHandler());
        getItemInteractionRecorder().recordItemMarketMovement(wrapper, "Purchase", itemId);
        return result;
    }

    @Override
    public FfBookInformations getInfo() {
        return (FfBookInformations) super.getInfo();
    }
}
