package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;

import java.util.Map;

import org.springframework.util.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming the "random" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class RandomTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<RandomCommand>> randomTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.state(randomTransformers != null, "The parameter 'attributeTestTransformers' cannot be null!");
        data.addCommand(parseRandom(parent, node, data));
    }

    private RandomCommand parseRandom(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final RandomCommand randomCommand = getBeanFactory().getBean(RandomCommand.class);

        final String diceConfig = extractAttribute(node, "diceConfig", "1d6");

        randomCommand.setDiceConfig("dice" + diceConfig);

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        final ChoicePositionCounter positionCounter = data.getPositionCounter();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<RandomCommand> responseTransformer = randomTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, randomCommand, positionCounter);
            }
        }

        return randomCommand;
    }

    public void setRandomTransformers(final Map<String, CommandSubTransformer<RandomCommand>> randomTransformers) {
        this.randomTransformers = randomTransformers;
    }
}
