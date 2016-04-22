package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import org.springframework.util.Assert;
import org.w3c.dom.Node;

/**
 * Class for transforming the "codeword" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class CodewordTransformer extends AbstractStubTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.notNull(parent, "The parameter 'parent' cannot be null!");
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(data, "The parameter 'data' cannot be null!");

        final String codewordName = extractAttribute(node, "name");

        data.getCodewords().add(codewordName);
    }
}
