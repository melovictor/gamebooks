package hu.zagor.gamebooks.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.DropItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.ReplaceItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemData;
import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemResponse;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class for handling the retrieval of an item for the character.
 * @author Tamas_Szekeres
 */
public class GenericBookTakeItemController extends AbstractRequestWrappingController {

    @LogInject private Logger logger;
    @Autowired private BookContentInitializer contentInitializer;

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param data the {@link TakeItemData} containing the incoming parameters
     * @return the bean containing results of the taking
     */
    @RequestMapping(value = PageAddresses.BOOK_TAKE_ITEM, method = RequestMethod.POST)
    @ResponseBody
    public final TakeItemResponse handleItemTake(final HttpServletRequest request, final TakeItemData data) {
        Assert.notNull(data.getItemId(), "The parameter 'itemId' cannot be null!");
        Assert.isTrue(data.getItemId().length() > 0, "The parameter 'itemId' cannot be empty!");
        Assert.isTrue(data.getAmount() > 0, "The parameter 'amount' must be positive!");

        return doHandleItemTake(request, data.getItemId(), data.getAmount());
    }

    /**
     * Method for handling the replacement of items through the displayed text.
     * @param request the http request
     * @param data the {@link ReplaceItemData} object containing the incoming parameters
     * @return the amount of items successfully taken
     */
    @RequestMapping(value = PageAddresses.BOOK_REPLACE_ITEM, method = RequestMethod.POST)
    @ResponseBody
    public final String handleItemReplace(final HttpServletRequest request, final ReplaceItemData data) {
        Assert.notNull(data.getLoseId(), "The parameter 'oldItemId' cannot be null!");
        Assert.isTrue(!data.getLoseId().isEmpty(), "The parameter 'newItemId' cannot be empty!");
        Assert.notNull(data.getGatherId(), "The parameter 'itemId' cannot be null!");
        Assert.isTrue(!data.getGatherId().isEmpty(), "The parameter 'newItemId' cannot be empty!");
        Assert.isTrue(data.getAmount() > 0, "The parameter 'amount' must be positive!");

        return doHandleItemReplace(request, data.getLoseId(), data.getGatherId(), data.getAmount());
    }

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param itemId the id of the item to take
     * @param amount the amount of the item to take
     * @return the bean storing information about the taking
     */
    protected TakeItemResponse doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final GatheredLostItem glItem = getGatheredLostItem(itemId, amount);
        validateItem(glItem, request);
        final Character character = wrapper.getCharacter();

        final int totalTook = getInfo().getCharacterHandler().getItemHandler().addItem(character, itemId, amount);
        if (totalTook > 0) {
            final Paragraph paragraph = wrapper.getParagraph();
            paragraph.removeValidItem(itemId, totalTook);
        }
        logger.debug("Took {} piece(s) of item {}.", totalTook, itemId);

        return new TakeItemResponse(totalTook);
    }

    private GatheredLostItem getGatheredLostItem(final String itemId, final int amount) {
        return (GatheredLostItem) getBeanFactory().getBean("gatheredLostItem", itemId, amount, 0, false);
    }

    /**
     * Validates that the given item can actually be gathered at this point in the story.
     * @param glItem the id and amount of the item to load, cannot be null
     * @param request the http request, cannot be null
     */
    protected void validateItem(final GatheredLostItem glItem, final HttpServletRequest request) {
        Assert.notNull(glItem, "Parameter 'glItem' can not be null!");
        Assert.notNull(request, "The parameter 'request' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        final Paragraph paragraph = wrapper.getParagraph();
        contentInitializer.validateItem(glItem, player, paragraph, getInfo());
    }

    /**
     * Method for handling the replacement of items through the displayed text.
     * @param request the http request
     * @param oldItemId the id of the item to lose
     * @param newItemId the id of the item to take
     * @param amount the amount of the item to take
     * @return the amount of items successfully taken
     */
    protected String doHandleItemReplace(final HttpServletRequest request, final String oldItemId, final String newItemId, final int amount) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final GatheredLostItem glNewItem = getGatheredLostItem(newItemId, amount);

        final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final Character character = wrapper.getCharacter();
        final int totalTook;
        if (!itemHandler.hasItem(character, oldItemId)) {
            logger.debug("User doesn't have item {}.", oldItemId);
            totalTook = 0;
        } else {
            validateItem(glNewItem, request);
            itemHandler.removeItem(character, oldItemId, 1);
            totalTook = itemHandler.addItem(character, newItemId, amount);
            final Paragraph paragraph = wrapper.getParagraph();
            paragraph.removeValidItem(newItemId, totalTook);
            logger.debug("Took {} piece(s) of item {} in place of item {}.", totalTook, newItemId, oldItemId);
        }

        return String.valueOf(totalTook);
    }

    /**
     * Method for handling the user dropping a random, discardable item.
     * @param dropItem the {@link DropItemData} bean containing information about the dropped item
     * @param request the {@link HttpServletRequest} bean
     */
    @RequestMapping(value = "drop", method = RequestMethod.POST)
    @ResponseBody
    public final void dropItem(final HttpServletRequest request, @ModelAttribute final DropItemData dropItem) {
        final CharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final Character character = getWrapper(request).getCharacter();
        final Item item = itemHandler.getItem(character, dropItem.getItemId());
        if (item != null && item.getEquipInfo().isRemovable()) {
            itemHandler.removeItem(character, dropItem.getItemId(), 1);
        }
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
            final CharacterHandler characterHandler = getInfo().getCharacterHandler();
            characterHandler.getItemHandler().setItemEquipState(character, itemId, isEquipped);
        }
    }
}
