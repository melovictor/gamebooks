package hu.zagor.gamebooks.content.command.userinput.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class for storing and evaluating textual user inputs.
 * @author Tamas_Szekeres
 */
@Component("userInputTextualResponse")
@Scope("prototype")
public class UserInputTextualResponse extends UserInputResponse {

    private final String response;

    /**
     * Default constructor that will create a fallback response.
     */
    public UserInputTextualResponse() {
        response = null;
    }

    /**
     * Creates an object with the given response value.
     * @param response the accepted response for this object
     */
    public UserInputTextualResponse(final String response) {
        Assert.notNull(response, "The parameter 'response' cannot be null!");
        this.response = response;
    }

    @Override
    public boolean matches(final String answer) {
        Assert.notNull(answer, "The parameter 'answer' cannot be null!");
        return answer.toLowerCase().equals(response.toLowerCase());
    }

    @Override
    public boolean isFallback() {
        return response == null;
    }

}
