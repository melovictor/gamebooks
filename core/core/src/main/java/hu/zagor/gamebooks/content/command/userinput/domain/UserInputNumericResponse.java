package hu.zagor.gamebooks.content.command.userinput.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class for storing and evaluating numerical user inputs.
 * @author Tamas_Szekeres
 */
@Component("userInputNumericResponse")
@Scope("prototype")
public class UserInputNumericResponse extends UserInputResponse {

    private final Integer minBound;
    private final Integer maxBound;

    /**
     * Default constructor that will create a fallback response.
     */
    public UserInputNumericResponse() {
        minBound = null;
        maxBound = null;
    }

    /**
     * Constructor that requests a lower and higher bound (both inclusive) where this response is valid.
     * @param minBound the inclusive lower bound, cannot be null
     * @param maxBound the inclusive higher bound, cannot be null
     */
    public UserInputNumericResponse(final Integer minBound, final Integer maxBound) {
        Assert.notNull(minBound, "The parameter 'minBound' cannot be null!");
        Assert.notNull(maxBound, "The parameter 'maxBound' cannot be null!");
        Assert.isTrue(minBound <= maxBound, "The parameter 'minBound' cannot be bigger than 'maxBound'!");

        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    @Override
    public boolean matches(final String answer) {
        Assert.notNull(answer, "The parameter 'answer' cannot be null!");
        boolean result;
        try {
            final Integer value = Integer.valueOf(answer);
            result = minBound <= value && value <= maxBound;
        } catch (final NumberFormatException exception) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean isFallback() {
        return minBound == null;
    }

    Integer getMinBound() {
        return minBound;
    }

    Integer getMaxBound() {
        return maxBound;
    }

}
