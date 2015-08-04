package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.Paragraph;

/**
 * Interface for a bean that can resolve the {@link Paragraph} bean's commands and finalize the paragraph using the {@link Character} as a reference point.
 * @author Tamas_Szekeres
 */
public interface BookParagraphResolver {

    /**
     * Resolves the {@link Paragraph} bean using the {@link Character} bean. During this transformation the contents of both the {@link Paragraph} and {@link Character} beans might
     * change!
     * @param resolvationData the {@link ResolvationData}, cannot be null
     * @param paragraph the {@link Paragraph} bean that needs resolving, cannot be null
     */
    void resolve(ResolvationData resolvationData, final Paragraph paragraph);

}
