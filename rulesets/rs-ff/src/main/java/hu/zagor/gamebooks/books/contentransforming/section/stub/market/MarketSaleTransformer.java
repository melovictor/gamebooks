package hu.zagor.gamebooks.books.contentransforming.section.stub.market;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import hu.zagor.gamebooks.content.command.market.domain.MarketElement;

import org.w3c.dom.Node;

/**
 * Sub transformer for transforming "sale" elements in "market".
 * @author Tamas_Szekeres
 */
public class MarketSaleTransformer extends AbstractCommandSubTransformer<MarketCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final MarketCommand command, final ChoicePositionCounter positionCounter) {
        final MarketElement element = new MarketElement();

        element.setId(extractAttribute(node, "id"));
        element.setPrice(extractIntegerAttribute(node, "price"));
        element.setStock(extractIntegerAttribute(node, "stock"));

        command.getItemsForSale().add(element);
    }

}
