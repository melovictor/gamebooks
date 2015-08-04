package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Default transformer for paragraphs that contains common parts of the transforming process.
 * @author Tamas_Szekeres
 */
public class DefaultBookParagraphTransformer extends AbstractTransformer implements BookParagraphTransformer {

    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final String PARAGRAPH_ELEMENT_NAME = "p";
    private static final String DISPLAY_ID_ATTRIBUTE_NAME = "display";

    private BookParagraphDataTransformer paragraphDataTransformer;

    @LogInject
    private Logger logger;

    @Override
    public Map<String, Paragraph> transformParagraphs(final Document document) throws XmlTransformationException {
        Assert.notNull(document, "Parameter 'document' can not be null!");
        final Map<String, Paragraph> contents = new HashMap<>();

        final NodeList childNodes = document.getChildNodes();
        if (childNodes.getLength() > 0) {
            parseContent(getContentNode(childNodes), contents);
        } else {
            logger.error("Couldn't find 'content' element in xml file!");
            throw new XmlTransformationException("Missing element 'content'.");
        }

        return contents;
    }

    private Node getContentNode(final NodeList childNodes) {
        final int length = childNodes.getLength();
        Node itemNode = null;

        for (int i = 0; i < length; i++) {
            final Node node = childNodes.item(i);
            if ("content".equals(node.getNodeName())) {
                itemNode = node;
            }
        }

        return itemNode;
    }

    private void parseContent(final Node node, final Map<String, Paragraph> contents) {
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (isParagraph(childNode)) {
                final Paragraph paragraph = parseParagraph(childNode);
                final String id = paragraph.getId();
                logger.trace("Successfully parsed paragraph '{}'.", id);
                if (contents.containsKey(id)) {
                    throw new IllegalStateException("Book contains at least two instances of the paragraph id '" + id + "'!");
                }
                contents.put(id, paragraph);
            }
        }
    }

    private boolean isParagraph(final Node childNode) {
        return PARAGRAPH_ELEMENT_NAME.equals(childNode.getNodeName());
    }

    private Paragraph parseParagraph(final Node node) {
        final String id = extractAttribute(node, ID_ATTRIBUTE_NAME);
        final String displayId = extractAttribute(node, DISPLAY_ID_ATTRIBUTE_NAME);
        final int actions = extractIntegerAttribute(node, "actions", Integer.MAX_VALUE);
        final Paragraph paragraph = (Paragraph) getBeanFactory().getBean("paragraph", id, displayId, actions);

        final ParagraphData data = paragraphDataTransformer.parseParagraphData(paragraph.getPositionCounter(), node);
        paragraph.setData(data);
        return paragraph;
    }

    public void setParagraphDataTransformer(final BookParagraphDataTransformer paragraphDataTransformer) {
        this.paragraphDataTransformer = paragraphDataTransformer;
    }

}
