package hu.zagor.gamebooks.character.handler.paragraph;

import hu.zagor.gamebooks.character.Character;

import java.util.List;

import org.mvel2.MVEL;
import org.springframework.util.Assert;

/**
 * Interface for doing generic paragraph-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class DefaultCharacterParagraphHandler implements CharacterParagraphHandler {

    private static final String CHARACTER_NOT_NULL = "The parameter 'character' cannot be null!";
    private static final String PARAGRAPH_NOT_NULL = "The parameter 'paragraph' cannot be null!";

    @Override
    public void addParagraph(final Character character, final String paragraph) {
        Assert.notNull(paragraph, PARAGRAPH_NOT_NULL);
        Assert.notNull(character, CHARACTER_NOT_NULL);

        character.getParagraphs().add(paragraph);
    }

    @Override
    public boolean visitedParagraph(final Character character, final String paragraph) {
        Assert.notNull(paragraph, PARAGRAPH_NOT_NULL);
        Assert.notNull(character, CHARACTER_NOT_NULL);

        return resolveParagraphCodes(character.getParagraphs(), paragraph);
    }

    private boolean resolveParagraphCodes(final List<String> paragraphs, final String expression) {
        String resolvedExpression = expression;
        resolvedExpression = markVisitedParagraphs(paragraphs, resolvedExpression);
        resolvedExpression = markUnvisitedParagraphs(resolvedExpression);
        resolvedExpression = switchOperands(resolvedExpression);
        return (boolean) MVEL.eval(resolvedExpression);
    }

    private String switchOperands(final String resolvedExpression) {
        return resolvedExpression.replaceAll("or", "||").replaceAll("and", "&&").replaceAll("not", "!");
    }

    private String markVisitedParagraphs(final List<String> paragraphs, final String resolvedExpression) {
        String nextStep = resolvedExpression;
        for (final String paragraph : paragraphs) {
            nextStep = nextStep.replaceAll("\\b" + paragraph + "\\b", "true");
        }
        return nextStep;
    }

    private String markUnvisitedParagraphs(final String expression) {
        String resolvedExpression = expression;
        final String[] pieces = resolvedExpression.replaceAll("true", "").replaceAll("false", "").replace("not", "").replaceAll(" and ", " ").replaceAll(" or ", " ")
            .replaceAll("\\(", " ").replaceAll("\\)", " ").split(" ");
        for (final String piece : pieces) {
            if (piece.trim().length() > 0) {
                resolvedExpression = resolvedExpression.replaceAll(piece.trim(), "false");
            }
        }
        return resolvedExpression;
    }
}
