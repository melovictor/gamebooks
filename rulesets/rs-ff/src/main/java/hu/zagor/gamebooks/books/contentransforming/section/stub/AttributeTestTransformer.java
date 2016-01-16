package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestSuccessType;
import java.util.Map;
import org.springframework.util.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for transforming the "test" elements of a paragraph.
 * @author Tamas_Szekeres
 */
public class AttributeTestTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<AttributeTestCommand>> attributeTestTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.state(attributeTestTransformers != null, "The parameter 'attributeTestTransformers' cannot be null!");
        data.addCommand(parseAttributeTest(parent, node, data));
    }

    private AttributeTestCommand parseAttributeTest(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        final AttributeTestCommand attributeTestCommand = getBeanFactory().getBean(AttributeTestCommand.class);

        final String against = extractAttribute(node, "against");
        final String compactAgainst = extractAttribute(node, "compactAgainst");
        final String configName = extractAttribute(node, "diceConfig", "2d6");
        final int amount = extractIntegerAttribute(node, "add", 0);
        final String success = extractAttribute(node, "success");

        attributeTestCommand.setAgainst(against);
        attributeTestCommand.setCompactAgainst(compactAgainst);
        attributeTestCommand.setAdd(amount);
        attributeTestCommand.setConfigurationName("dice" + configName);
        if (success != null) {
            final AttributeTestSuccessType successType = AttributeTestSuccessType.valueOf(success);
            attributeTestCommand.setSuccessType(successType);
        }

        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        final ChoicePositionCounter positionCounter = data.getPositionCounter();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<AttributeTestCommand> responseTransformer = attributeTestTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, attributeTestCommand, positionCounter);
            }
        }

        return attributeTestCommand;
    }

    public void setAttributeTestTransformers(final Map<String, CommandSubTransformer<AttributeTestCommand>> attributeTestTransformers) {
        this.attributeTestTransformers = attributeTestTransformers;
    }

}
