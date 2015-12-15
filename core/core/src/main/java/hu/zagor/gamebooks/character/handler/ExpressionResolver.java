package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.Character;

/**
 * Interface for an object that is capable to resolve an expression that might contain references to a {@link Character} object's fields.
 * @author Tamas_Szekeres
 */
public interface ExpressionResolver {

    /**
     * Resolves a string expression using the provided characters' properties.
     * @param character the {@link Character} whose properties should be used if necessary
     * @param expression the expression to be resolved
     * @return the resolved value
     */
    int resolveValue(final Character character, final String expression);
}
