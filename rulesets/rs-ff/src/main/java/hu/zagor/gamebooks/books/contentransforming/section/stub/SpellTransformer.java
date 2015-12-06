package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import org.w3c.dom.Node;

/**
 * Class for transforming spell choices of a paragraph.
 * @author Tamas_Szekeres
 */
public class SpellTransformer extends AbstractStubTransformer {
    private static final String ID_ATTRIBUTE_NAME = "id";

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final Choice choice = parseChoice(data.getPositionCounter(), node);
        ((SorParagraphData) data).addSpellChoice(choice);
    }

    private Choice parseChoice(final ChoicePositionCounter positionCounter, final Node node) {
        final String id = extractAttribute(node, ID_ATTRIBUTE_NAME);
        final String text = extractAttribute(node, "name");
        final String singleChoiceText = extractAttribute(node, "singleChoiceText");
        return (Choice) getBeanFactory().getBean("choice", id, text, positionCounter.updateAndGetPosition(1), singleChoiceText);
    }

}
