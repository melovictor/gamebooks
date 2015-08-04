package hu.zagor.gamebooks.ff.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import hu.zagor.gamebooks.support.writer.Converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Generic take item controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookTakeItemController extends GenericBookTakeItemController {

    @Autowired
    private MarketHandler marketHandler;
    @Autowired
    private Converter converter;

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
     * @return null
     */
    @RequestMapping(value = PageAddresses.BOOK_CHANGE_ITEM_EQUIP_STATE + "/{id}/{isEquipped}")
    @ResponseBody
    public String handleItemStateChange(final HttpServletRequest request, @PathVariable("id") final String itemId, @PathVariable("isEquipped") final boolean isEquipped) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();

        final int totalActions = paragraph.getActions();
        if (totalActions > 0) {
            paragraph.setActions(totalActions - 1);
            final Character character = wrapper.getCharacter();
            final FfCharacterHandler characterHandler = (FfCharacterHandler) getInfo().getCharacterHandler();
            characterHandler.getItemHandler().setItemEquipState(character, itemId, isEquipped);
            getItemInteractionRecorder().changeItemEquipState(wrapper, itemId);
        }
        return null;
    }

    /**
     * Method for consuming an item (provision or potion).
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @return null
     */
    @RequestMapping(value = PageAddresses.BOOK_CONSUME_ITEM + "/{id}")
    @ResponseBody
    public String handleConsumeItem(final HttpServletRequest request, @PathVariable("id") final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        return doHandleConsumeItem(request, itemId);
    }

    /**
     * Method for actually consuming an item (provision or potion).
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to change the state
     * @return usually nothing
     */
    protected String doHandleConsumeItem(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        getItemInteractionRecorder().recordItemConsumption(wrapper, itemId);
        final Paragraph paragraph = wrapper.getParagraph();
        final Character character = wrapper.getCharacter();
        final CommandView commandView = character.getCommandView();
        if (notFighting(commandView) && canEatHere(paragraph)) {
            final FfCharacterHandler characterHandler = (FfCharacterHandler) getInfo().getCharacterHandler();
            final FfCharacterItemHandler itemHandler = characterHandler.getItemHandler();
            final FfItem item = (FfItem) itemHandler.getItem(character, itemId);
            final int totalActions = paragraph.getActions();
            final int consumeTime = item.getActions();
            if (totalActions >= consumeTime) {
                paragraph.setActions(totalActions - consumeTime);
                itemHandler.consumeItem((FfCharacter) character, itemId, characterHandler.getAttributeHandler());
            }
        }
        return null;
    }

    private boolean canEatHere(final Paragraph paragraph) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) getInfo().getCharacterHandler();
        final FfParagraphData data = (FfParagraphData) paragraph.getData();
        return characterHandler.isCanEatEverywhere() || data.isCanEat();
    }

    private boolean notFighting(final CommandView commandView) {
        return commandView == null || commandView.getViewName() == null || !commandView.getViewName().startsWith("ffFight");
    }

    /**
     * Method for buying an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @param itemId the id of the item to buy
     * @throws IOException when a problem occurs while writing the output
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_BUY + "/{id}")
    @ResponseBody
    public void handleMarketBuy(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("id") final String itemId) throws IOException {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        final Paragraph paragraph = wrapper.getParagraph();
        final FfParagraphData data = (FfParagraphData) paragraph.getData();

        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) getInfo().getCharacterHandler().getItemHandler();

        final Map<String, Object> result = marketHandler.handleMarketPurchase(itemId, character, data, itemHandler);
        getItemInteractionRecorder().recordItemMarketMovement(wrapper, "Sale", itemId);

        response.setContentType("application/json; charset=utf-8");
        final PrintWriter writer = response.getWriter();
        final String jsonString = converter.toJsonString(result);
        writer.write(jsonString);
        writer.close();
    }

    /**
     * Method for selling an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @param itemId the id of the item to buy
     * @throws IOException when a problem occurs while writing the output
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_SELL + "/{id}")
    @ResponseBody
    public void handleMarketSell(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("id") final String itemId) throws IOException {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();

        final Paragraph paragraph = wrapper.getParagraph();
        final FfParagraphData data = (FfParagraphData) paragraph.getData();

        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) getInfo().getCharacterHandler().getItemHandler();

        final Map<String, Object> result = marketHandler.handleMarketSell(itemId, character, data, itemHandler);
        getItemInteractionRecorder().recordItemMarketMovement(wrapper, "Purchase", itemId);

        response.setContentType("application/json; charset=utf-8");
        final PrintWriter writer = response.getWriter();
        final String jsonString = converter.toJsonString(result);
        writer.write(jsonString);
        writer.close();
    }

}
