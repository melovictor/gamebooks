package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;

import org.w3c.dom.Node;

/**
 * Transforms the resultElse element inside the random section.
 * @author Tamas_Szekeres
 */
public class RandomResultElseTransformer extends AbstractCommandSubTransformer<RandomCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final RandomCommand command, final ChoicePositionCounter positionCounter) {
        final ParagraphData paragraphData = parent.parseParagraphData(positionCounter, node);
        command.setResultElse(paragraphData);
    }
}
