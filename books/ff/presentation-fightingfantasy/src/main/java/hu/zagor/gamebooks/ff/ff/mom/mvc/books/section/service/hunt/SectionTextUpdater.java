package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;

/**
 * Interface for updating the text in the section.
 * @author Tamas_Szekeres
 */
public interface SectionTextUpdater {

    /**
     * Updates the text in the paragraph with the new positions and texts in the result.
     * @param paragraph the {@link Paragraph} object
     * @param result the {@link HuntRoundResult} object
     */
    void updateParagraphContent(Paragraph paragraph, HuntRoundResult result);

}
