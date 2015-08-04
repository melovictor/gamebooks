package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Transformer to transform a specific user input block using a specified set of response transformers.
 * @author Tamas_Szekeres
 */
public class DefaultUserInputStubTransformer extends AbstractCommandSubTransformer<UserInputCommand> {

    private Map<String, CommandSubTransformer<UserInputCommand>> responseTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command,
            final ChoicePositionCounter positionCounter) {
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node childNode = childNodes.item(i);
            if (!irrelevantNode(childNode)) {
                final String childNodeName = childNode.getNodeName();
                final CommandSubTransformer<UserInputCommand> responseTransformer = responseTransformers.get(childNodeName);
                if (responseTransformer == null) {
                    throw new UnsupportedOperationException(childNodeName);
                }
                responseTransformer.transform(parent, childNode, command, positionCounter);
            }
        }
    }

    public void setResponseTransformers(final Map<String, CommandSubTransformer<UserInputCommand>> responseTransformers) {
        this.responseTransformers = responseTransformers;
    }

}
