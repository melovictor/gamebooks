package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.market.GiveUpMode;
import hu.zagor.gamebooks.content.command.market.MarketCommand;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming the "market" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class MarketTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<MarketCommand>> marketTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        verifyData(data);
        final MarketCommand command = new MarketCommand();
        data.addCommand(command);
        command.setMoneyAttribute(extractAttribute(node, "moneyAttribute", "gold"));
        command.setSingleCcyKey(extractAttribute(node, "currencySimple", "page.ff.label.market.goldPiece"));
        command.setMultipleCcyKey(extractAttribute(node, "currencyMultiple", "page.ff.label.market.goldPieces"));
        command.setMustHaveGold(extractIntegerAttribute(node, "mustHaveGold", 0));
        command.setMustSellExactly(extractIntegerAttribute(node, "mustSellExactly", 0));
        command.setMustBuy(extractIntegerAttribute(node, "mustBuy", 0));
        final Integer giveUpAmount = extractIntegerAttribute(node, "mustGiveUp", 0);
        if (giveUpAmount > 0) {
            final String giveUpMode = extractAttribute(node, "mustGiveUpMode", "asMuchAsPossible");
            command.setGiveUpMode(GiveUpMode.valueOf(giveUpMode));
            command.setGiveUpAmount(giveUpAmount);
        }

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        final ChoicePositionCounter positionCounter = data.getPositionCounter();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<MarketCommand> responseTransformer = marketTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, command, positionCounter);
            }
        }
    }

    private void verifyData(final ParagraphData data) {
        if (!hasMarketMarker(data)) {
            throw new IllegalStateException("We're trying to read a market block without the proper html placeholder ([div data-market=\"\"][/div])! Please fix it!");
        }
    }

    private boolean hasMarketMarker(final ParagraphData data) {
        return data.getText().contains("data-market=\"\"");
    }

    public void setMarketTransformers(final Map<String, CommandSubTransformer<MarketCommand>> marketTransformers) {
        this.marketTransformers = marketTransformers;
    }

}
