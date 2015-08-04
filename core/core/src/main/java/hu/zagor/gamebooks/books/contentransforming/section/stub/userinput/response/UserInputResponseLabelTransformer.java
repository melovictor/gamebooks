package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import org.w3c.dom.Node;

/**
 * Transformer for transforming the label on a user input.
 * @author Tamas_Szekeres
 */
public class UserInputResponseLabelTransformer extends AbstractCommandSubTransformer<UserInputCommand> {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command,
            final ChoicePositionCounter positionCounter) {
        command.setLabel(getNodeText(node));
    }

}
