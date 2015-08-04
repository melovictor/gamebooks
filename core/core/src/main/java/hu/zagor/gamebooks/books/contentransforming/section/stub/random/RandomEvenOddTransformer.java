package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.random.RandomResult;
import hu.zagor.gamebooks.support.logging.LogInject;

import org.slf4j.Logger;
import org.w3c.dom.Node;

/**
 * Transforms the even or odd element inside the random section.
 * @author Tamas_Szekeres
 */
public class RandomEvenOddTransformer extends AbstractCommandSubTransformer<RandomCommand> {

    @LogInject
    private Logger logger;
    private final int remainder;

    /**
     * Constructor that specifies what remainder this specific instance should be checking for.
     * @param remainder the remainder, should be either 0 or 1
     */
    public RandomEvenOddTransformer(final int remainder) {
        this.remainder = remainder;
    }

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final RandomCommand command, final ChoicePositionCounter positionCounter) {
        final int min = Integer.valueOf(this.extractAttribute(node, "min"));
        final int max = Integer.valueOf(this.extractAttribute(node, "max"));
        final ParagraphData paragraphData = parent.parseParagraphData(positionCounter, node);

        for (int i = min; i <= max; i++) {
            if (i % 2 == remainder) {
                final RandomResult randomResult = getBeanFactory().getBean(RandomResult.class);
                randomResult.setMin(i);
                randomResult.setMax(i);

                randomResult.setParagraphData(paragraphData);
                command.addResult(randomResult);
            }
        }
    }

}
