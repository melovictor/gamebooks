package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputNumericResponse;

import org.w3c.dom.Node;

/**
 * Transforms the default numeric result into the {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
public class UserInputResponseNumericResultElseTransformer extends AbstractUserInputResponseResultTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command, final ChoicePositionCounter positionCounter) {
        final UserInputNumericResponse response = getBeanFactory().getBean(UserInputNumericResponse.class);
        command.addResponse(parseResponseContent(parent, node, response, positionCounter));
        extractResponseTimeAttributes(node, response);
    }

}
