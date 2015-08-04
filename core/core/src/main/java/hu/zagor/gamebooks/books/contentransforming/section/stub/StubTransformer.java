package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;

import org.w3c.dom.Node;

/**
 * Interface for a stub transformer that is capable of transforming a {@link Node} stub.
 * @author Tamas_Szekeres
 */
public interface StubTransformer {

    /**
     * Transforms the stub appropriate for the given {@link StubTransformer}.
     * @param parent the parent transformer
     * @param node the node that contains the data that has to be transformed
     * @param data the {@link ParagraphData} bean we're transforming into
     */
    void transform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data);

}
