package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputTextualResponse;

import org.w3c.dom.Node;

/**
 * Transforms the default textual result into the {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
public class UserInputResponseTextualResultElseTransformer extends AbstractUserInputResponseResultTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command, final ChoicePositionCounter positionCounter) {
        final UserInputTextualResponse response = getBeanFactory().getBean(UserInputTextualResponse.class);
        command.addResponse(parseResponseContent(parent, node, response, positionCounter));
        extractResponseTimeAttributes(node, response);
    }
}
