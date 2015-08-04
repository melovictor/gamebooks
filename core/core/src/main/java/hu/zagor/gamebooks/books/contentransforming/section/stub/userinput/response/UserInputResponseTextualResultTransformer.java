package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputTextualResponse;

import org.w3c.dom.Node;

/**
 * Transforms a specific textual result into the {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
public class UserInputResponseTextualResultTransformer extends AbstractUserInputResponseResultTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command, final ChoicePositionCounter positionCounter) {
        command.addResponse(parseTextResponse(parent, node, positionCounter));
    }

    private UserInputResponse parseTextResponse(final BookParagraphDataTransformer parent, final Node node, final ChoicePositionCounter positionCounter) {
        final String solution = extractAttribute(node, "text");
        final UserInputTextualResponse response = (UserInputTextualResponse) getBeanFactory().getBean("userInputTextualResponse", solution);
        extractResponseTimeAttributes(node, response);
        return parseResponseContent(parent, node, response, positionCounter);
    }

}
