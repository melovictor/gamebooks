package hu.zagor.gamebooks.ff.sor.tss.mvc.books.section.service;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Section 79 pre handler for Sorcery 3. Remove items from the list that we already tried to sell.
 * @author Tamas_Szekeres
 */
@Component
public class Sor3Section79PreHandler extends FfCustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        final String blacklist = wrapper.getCharacter().getUserInteraction().get("foodExchangeBlacklistedItems");
        if (blacklist != null) {
            final MarketCommand command = (MarketCommand) paragraph.getData().getCommands().get(0);
            final List<MarketElement> itemsForSale = command.getItemsForPurchase();
            for (final String blacklistedEntry : blacklist.split(",")) {
                remove(itemsForSale, blacklistedEntry);
            }
        }

    }

    private void remove(final List<MarketElement> itemsForSale, final String blacklistedEntry) {
        boolean found = false;
        final Iterator<MarketElement> iterator = itemsForSale.iterator();
        while (iterator.hasNext() && !found) {
            final MarketElement next = iterator.next();
            if (next.getId().equals(blacklistedEntry)) {
                iterator.remove();
                found = true;
            }
        }
    }

}
