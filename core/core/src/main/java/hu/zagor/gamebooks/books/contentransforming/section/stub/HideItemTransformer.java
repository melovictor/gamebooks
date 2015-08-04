package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;

import org.w3c.dom.Node;

/**
 * Class for transforming hide item sections of a paragraph.
 * @author Tamas_Szekeres
 */
public class HideItemTransformer extends GatherLoseItemTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        data.addHiddenItem(parseGatherLoseItem(node));
    }

}
