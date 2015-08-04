package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;

import org.w3c.dom.Node;

/**
 * Transforms the label element inside the random section.
 * @author Tamas_Szekeres
 */
public class RandomLabelTransformer extends AbstractCommandSubTransformer<RandomCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final RandomCommand command,
            final ChoicePositionCounter positionCounter) {
        final String label = getNodeText(node);
        command.setLabel(label);
    }

}
