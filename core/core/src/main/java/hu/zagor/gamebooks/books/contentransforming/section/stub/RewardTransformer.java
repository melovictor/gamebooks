package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.reward.Reward;

import org.w3c.dom.Node;

/**
 * Class for transforming reward sections of a paragraph.
 * @author Tamas_Szekeres
 */
public class RewardTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final Reward reward = new Reward();
        reward.setId(extractAttribute(node, "id"));
        reward.setSeriesId(extractBooleanAttribute(node, "useSeriesId", false));
        data.setReward(reward);
    }

}
