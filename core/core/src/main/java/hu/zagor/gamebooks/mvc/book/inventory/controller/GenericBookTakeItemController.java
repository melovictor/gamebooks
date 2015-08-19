package hu.zagor.gamebooks.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.recording.ItemInteractionRecorder;
import hu.zagor.gamebooks.support.logging.LogInject;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class for handling the retrieval of an item for the character.
 * @author Tamas_Szekeres
 */
public class GenericBookTakeItemController extends AbstractRequestWrappingController {

    @LogInject
    private Logger logger;
    @Autowired
    private ItemInteractionRecorder itemInteractionRecorder;
    @Autowired
    private BookContentInitializer contentInitializer;

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param itemId the id of the item to take
     * @param amount the amount of the item to take
     * @return the amount of items successfully taken
     */
    @RequestMapping(value = PageAddresses.BOOK_TAKE_ITEM + "/{id}/{amount}")
    @ResponseBody
    public String handleItemTake(final HttpServletRequest request, @PathVariable("id") final String itemId, @PathVariable("amount") final int amount) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");
        Assert.isTrue(amount > 0, "The parameter 'amount' must be positive!");

        return String.valueOf(doHandleItemTake(request, itemId, amount));
    }

    /**
     * Method for handling the acquiring of items through the displayed text.
     * @param request the http request
     * @param itemId the id of the item to take
     * @param amount the amount of the item to take
     * @return the amount of items successfully taken
     */
    protected int doHandleItemTake(final HttpServletRequest request, final String itemId, final int amount) {
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
        itemInteractionRecorder.recordItemTaking(wrapper, itemId);

        return totalTook;
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
    @RequestMapping(value = PageAddresses.BOOK_REPLACE_ITEM + "/{oldId}/{newId}/{amount}")
    @ResponseBody
    public final String handleItemReplace(final HttpServletRequest request, @PathVariable("oldId") final String oldItemId, @PathVariable("newId") final String newItemId,
        @PathVariable("amount") final int amount) {
        Assert.notNull(oldItemId, "The parameter 'oldItemId' cannot be null!");
        Assert.isTrue(!oldItemId.isEmpty(), "The parameter 'newItemId' cannot be empty!");
        Assert.notNull(newItemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(!newItemId.isEmpty(), "The parameter 'newItemId' cannot be empty!");
        Assert.isTrue(amount > 0, "The parameter 'amount' must be positive!");

        return doHandleItemReplace(request, oldItemId, newItemId, amount);
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

    public ItemInteractionRecorder getItemInteractionRecorder() {
        return itemInteractionRecorder;
    }

}
