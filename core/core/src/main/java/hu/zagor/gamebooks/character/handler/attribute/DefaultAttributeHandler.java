package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default implementation of the {@link AttributeHandler} interface to use an {@link ExpressionResolver} for attribute resolvation.
 * @author Tamas_Szekeres
 */
public class DefaultAttributeHandler implements AttributeHandler {
    @Autowired private ExpressionResolver expressionResolver;

    @Override
    public int resolveValue(final Character character, final String against) {
        return expressionResolver.resolveValue(character, against);
    }
}
