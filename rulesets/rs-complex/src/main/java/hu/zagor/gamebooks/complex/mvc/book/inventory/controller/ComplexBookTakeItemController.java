package hu.zagor.gamebooks.complex.mvc.book.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.inventory.controller.GenericBookTakeItemController;
import hu.zagor.gamebooks.mvc.book.inventory.domain.BuySellResponse;
import hu.zagor.gamebooks.mvc.book.inventory.service.MarketHandler;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Take item controller for complex rule systems.
 * @author Tamas_Szekeres
 * @param <C> the actually used {@link Character} type
 */
public abstract class ComplexBookTakeItemController<C extends Character> extends GenericBookTakeItemController {

    /**
     * Method for buying an item in the market.
     * @param request the {@link HttpServletRequest}
     * @param itemId the id of the item to buy
     * @return the response for taking the item
     */
    @RequestMapping(value = PageAddresses.BOOK_MARKET_BUY + "/{id}")
    @ResponseBody
    public final BuySellResponse handleMarketBuy(final HttpServletRequest request, @PathVariable("id") final String itemId) {
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
    protected BuySellResponse doHandleMarketBuy(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final C character = getCharacter(wrapper);

        final BuySellResponse result = getMarketHandler().handleMarketPurchase(itemId, character,
            (MarketCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand(), getInfo().getCharacterHandler());

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
    public final BuySellResponse handleMarketSell(final HttpServletRequest request, @PathVariable("id") final String itemId) {
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
    protected BuySellResponse doHandleMarketSell(final HttpServletRequest request, final String itemId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final C character = getCharacter(wrapper);

        final BuySellResponse result = getMarketHandler().handleMarketSell(itemId, character,
            (MarketCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand(), getInfo().getCharacterHandler());
        return result;
    }

    /**
     * Returns the market handler.
     * @return the market handler
     */
    protected abstract MarketHandler<C> getMarketHandler();

    /**
     * Returns the character.
     * @param wrapper the {@link HttpSessionWrapper} from which the character must be returned
     * @return the proper {@link Character}
     */
    protected abstract C getCharacter(final HttpSessionWrapper wrapper);

}
