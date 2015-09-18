package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputNumericResponse;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;

import org.w3c.dom.Node;

/**
 * Transforms a specific numeric result into the {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
public class UserInputResponseNumericResultTransformer extends AbstractUserInputResponseResultTransformer {

    @Override
    protected void doTransform(final BookParagraphDataTransformer parent, final Node node, final UserInputCommand command, final ChoicePositionCounter positionCounter) {
        command.addResponse(parseNumericResponse(parent, node, positionCounter));
    }

    private UserInputResponse parseNumericResponse(final BookParagraphDataTransformer parent, final Node node, final ChoicePositionCounter positionCounter) {
        final Integer minBound = extractIntegerAttribute(node, "minBound");
        final Integer maxBound = extractIntegerAttribute(node, "maxBound");
        final UserInputNumericResponse response = (UserInputNumericResponse) getBeanFactory().getBean("userInputNumericResponse", minBound, maxBound);
        extractResponseTimeAttributes(node, response);
        return parseResponseContent(parent, node, response, positionCounter);
    }

}
