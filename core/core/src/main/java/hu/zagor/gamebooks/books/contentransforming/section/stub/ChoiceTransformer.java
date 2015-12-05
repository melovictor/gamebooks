package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import org.w3c.dom.Node;

/**
 * Class for transforming choices of a paragraph.
 * @author Tamas_Szekeres
 */
public class ChoiceTransformer extends AbstractStubTransformer {

    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final String POSITION_ATTRIBUTE_NAME = "pos";

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final Choice choice = parseChoice(data.getPositionCounter(), node);
        addChoice(data, choice);
    }

    /**
     * Adds the parsed {@link Choice} object to the {@link ParagraphData} object.
     * @param data the {@link ParagraphData} object into which the parsed {@link Choice} object must be added
     * @param choice the {@link Choice} object to add
     */
    protected void addChoice(final ParagraphData data, final Choice choice) {
        data.addChoice(choice);
    }

    private Choice parseChoice(final ChoicePositionCounter positionCounter, final Node node) {
        final String id = extractAttribute(node, ID_ATTRIBUTE_NAME);
        final String text = getNodeText(node);
        final int position = extractPosAttribute(positionCounter, node);
        final String singleChoiceText = extractAttribute(node, "singleChoiceText");
        return (Choice) getBeanFactory().getBean("choice", id, text, position, singleChoiceText);
    }

    private int extractPosAttribute(final ChoicePositionCounter positionCounter, final Node node) {
        final Integer pos = extractIntegerAttribute(node, POSITION_ATTRIBUTE_NAME);
        return positionCounter.updateAndGetPosition(pos);
    }

}
