package hu.zagor.gamebooks.books.contentransforming.section.stub;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import java.util.Map;
import org.springframework.util.Assert;
import org.w3c.dom.Node;

/**
 * Class for transforming a user choice block of a paragraph.
 * @author Tamas_Szekeres
 */
public class UserInputTransformer extends AbstractStubTransformer {

    private Map<String, CommandSubTransformer<UserInputCommand>> userInputTransformers;

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final ParagraphData data) {
        Assert.state(userInputTransformers != null, "The parameter 'userInputTransformers' cannot be null!");
        data.addCommand(parseUserInput(parent, node, data.getPositionCounter()));
    }

    private Command parseUserInput(final BookParagraphDataTransformer parent, final Node node, final ChoicePositionCounter positionCounter) {
        final UserInputCommand command = getBeanFactory().getBean(UserInputCommand.class);
        final String type = extractAttribute(node, "type");
        command.setMin(extractIntegerAttribute(node, "min", Integer.MIN_VALUE));
        command.setMax(extractIntegerAttribute(node, "max", Integer.MAX_VALUE));
        command.setType(type);
        final CommandSubTransformer<UserInputCommand> transformer = userInputTransformers.get(type);
        if (transformer == null) {
            throw new UnsupportedOperationException(type);
        }
        transformer.transform(parent, node, command, positionCounter);
        return command;
    }

    public void setUserInputTransformers(final Map<String, CommandSubTransformer<UserInputCommand>> userInputTransformers) {
        this.userInputTransformers = userInputTransformers;
    }

}
