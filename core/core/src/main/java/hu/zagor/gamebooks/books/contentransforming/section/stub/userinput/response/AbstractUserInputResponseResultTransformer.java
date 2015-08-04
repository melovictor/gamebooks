package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import hu.zagor.gamebooks.books.contentransforming.section.AbstractCommandSubTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;

import org.w3c.dom.Node;

/**
 * An abstract transformer that can automatically parse the content of a response and put it into an already created {@link UserInputResponse} bean.
 * @author Tamas_Szekeres
 */
public abstract class AbstractUserInputResponseResultTransformer extends AbstractCommandSubTransformer<UserInputCommand> {

    /**
     * Parses the content of the response using the parent transformer then returns it.
     * @param parent the parent {@link BookParagraphTransformer}
     * @param node the {@link Node} to transform
     * @param response the {@link UserInputResponse} into which we have to put the content
     * @param positionCounter the {@link ChoicePositionCounter}
     * @return the filled {@link UserInputResponse}
     */
    protected UserInputResponse parseResponseContent(final BookParagraphDataTransformer parent, final Node node, final UserInputResponse response,
        final ChoicePositionCounter positionCounter) {
        response.setData(parent.parseParagraphData(positionCounter, node));
        return response;
    }

    /**
     * Extracts minimal and maximal response time restriction data.
     * @param node the {@link Node} to transform
     * @param response the {@link UserInputResponse} into which we have to put the content
     */
    protected void extractResponseTimeAttributes(final Node node, final UserInputResponse response) {
        response.setMinResponseTime(extractIntegerAttribute(node, "minResponse", 0));
        response.setMaxResponseTime(extractIntegerAttribute(node, "maxResponse", Integer.MAX_VALUE));
    }

}
