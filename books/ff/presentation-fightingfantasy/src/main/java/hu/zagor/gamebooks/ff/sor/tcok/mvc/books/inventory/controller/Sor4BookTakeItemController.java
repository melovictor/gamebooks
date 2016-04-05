package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.inventory.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.inventory.controller.SorBookTakeItemController;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the item taking request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_CROWN_OF_KINGS)
public class Sor4BookTakeItemController extends SorBookTakeItemController {
    @Resource(name = "sor4MagicItemsPriceChange") private Map<Integer, Integer> priceChange;

    @Override
    protected Map<String, Object> doHandleMarketBuy(final HttpServletRequest request, final String itemId) {
        final Map<String, Object> doHandleMarketBuy = super.doHandleMarketBuy(request, itemId);

        final HttpSessionWrapper wrapper = getWrapper(request);
        final MarketCommand market = (MarketCommand) wrapper.getParagraph().getItemsToProcess().get(0).getCommand();
        for (final MarketElement element : market.getItemsForSale()) {
            element.setPrice(priceChange.get(element.getPrice()));
        }

        return doHandleMarketBuy;
    }
}
