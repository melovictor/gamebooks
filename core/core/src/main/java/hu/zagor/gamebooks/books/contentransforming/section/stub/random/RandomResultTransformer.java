package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.random.RandomResult;
import org.w3c.dom.Node;

/**
 * Transforms the result element inside the random section.
 * @author Tamas_Szekeres
 */
public class RandomResultTransformer extends AbstractCommandSubTransformer<RandomCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final RandomCommand command, final ChoicePositionCounter positionCounter) {

        final RandomResult randomResult = getBeanFactory().getBean(RandomResult.class);

        randomResult.setMin(extractAttribute(node, "min"));
        randomResult.setMax(extractAttribute(node, "max"));

        final ParagraphData paragraphData = parent.parseParagraphData(positionCounter, node);
        randomResult.setParagraphData(paragraphData);
        command.addResult(randomResult);
    }

}
