package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;

import org.springframework.util.Assert;
import org.w3c.dom.Node;

/**
 * Abstract {@link StubTransformer} implementation that adds input parameter validation.
 * @author Tamas_Szekeres
 */
public abstract class AbstractStubTransformer extends AbstractTransformer implements StubTransformer {

    @Override
    public final void transform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.notNull(parent, "The parameter 'parent' cannot be null!");
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(data, "The parameter 'data' cannot be null!");

        doTransform(parent, node, data);
    }

    /**
     * Does the actual transformation.
     * @param parent the parent transformer
     * @param node the node that contains the data that has to be transformed
     * @param data the {@link ParagraphData} bean we're transforming into
     */
    protected abstract void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data);

}
