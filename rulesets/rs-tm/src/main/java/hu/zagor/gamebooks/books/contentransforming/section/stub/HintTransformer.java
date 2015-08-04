package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.TmParagraphData;

import org.w3c.dom.Node;

/**
 * Class for transforming the hints of a paragraph.
 * @author Tamas_Szekeres
 */
public class HintTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final TmParagraphData tmData = (TmParagraphData) data;
        tmData.setHint(getNodeText(node));
    }

}
