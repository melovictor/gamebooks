package hu.zagor.gamebooks.books.contentransforming.section.stub.attributetest;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import org.w3c.dom.Node;

/**
 * Transforms the failure odd element inside the attribute test section.
 * @author Tamas_Szekeres
 */
public class AttributeTestFailureOddTransformer extends AbstractCommandSubTransformer<AttributeTestCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final AttributeTestCommand command, final ChoicePositionCounter positionCounter) {
        command.setFailureOdd((FfParagraphData) parent.parseParagraphData(positionCounter, node));
    }

}
