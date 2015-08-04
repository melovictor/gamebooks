package hu.zagor.gamebooks.books.contentransforming.section.stub.market;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.market.MarketCommand;

import org.w3c.dom.Node;

/**
 * Sub transformer for transforming "sale" elements in "market".
 * @author Tamas_Szekeres
 */
public class MarketSaleLabelTransformer extends AbstractCommandSubTransformer<MarketCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final MarketCommand command, final ChoicePositionCounter positionCounter) {
        command.setSaleLabel(node.getTextContent());
    }

}
