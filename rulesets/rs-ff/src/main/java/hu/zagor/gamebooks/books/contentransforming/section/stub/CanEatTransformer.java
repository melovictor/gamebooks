package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import org.w3c.dom.Node;

/**
 * Transforms the canEat element of a paragraph.
 * @author Tamas_Szekeres
 */
public class CanEatTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        ((FfParagraphData) data).setCanEat(true);
    }

}
