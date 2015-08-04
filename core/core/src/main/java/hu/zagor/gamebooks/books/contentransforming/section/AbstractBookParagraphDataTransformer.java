package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.stub.StubTransformer;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

import java.util.Map;

import org.springframework.util.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract transformer for paragraphs that contains common parts of the transforming process.
 * @author Tamas_Szekeres
 */
public abstract class AbstractBookParagraphDataTransformer extends AbstractTransformer implements BookParagraphDataTransformer {

    private Map<String, StubTransformer> stubTransformers;

    @Override
    public ParagraphData parseParagraphData(final ChoicePositionCounter positionCounter, final Node node) {
        Assert.notNull(node, "The parameter 'node' cannot be null!");
        Assert.notNull(positionCounter, "The parameter 'paragraph' cannot be null!");

        final ParagraphData data = getParagraphData(positionCounter);
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                parseParagraphDataElement(childNode, data);
            }
        }
        return data;
    }

    private void parseParagraphDataElement(final Node node, final ParagraphData data) {
        final String nodeName = node.getNodeName();
        final StubTransformer stubTransformer = stubTransformers.get(nodeName);
        if (stubTransformer == null) {
            throw new UnsupportedOperationException(nodeName);
        }
        stubTransformer.transform(this, node, data);
    }

    /**
     * Returns an instance of a {@link ParagraphData} bean or one of it's descendant that the transformer will
     * have to fill with the data.
     * @param positionCounter the {@link ChoicePositionCounter} object that belongs to the parent
     * {@link Paragraph}
     * @return the instance
     */
    protected abstract ParagraphData getParagraphData(final ChoicePositionCounter positionCounter);

    public void setStubTransformers(final Map<String, StubTransformer> stubTransformers) {
        this.stubTransformers = stubTransformers;
    }

}
