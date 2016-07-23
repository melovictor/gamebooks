package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import org.w3c.dom.Node;

/**
 * Class for transforming the interrupt marker.
 * @author Tamas_Szekeres
 */
public class InterruptTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final FfParagraphData ffData = (FfParagraphData) data;
        ffData.setInterrupt(true);
    }

}
