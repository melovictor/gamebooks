package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;

/**
 * Interface for moving a specific piece on the table.
 * @author Tamas_Szekeres
 */
public interface PieceMover {

    /**
     * Moves the piece it is associated with.
     * @param result the {@link HuntRoundResult} object to operate on
     */
    void movePiece(HuntRoundResult result);

    /**
     * Gets the stored position of a piece.
     * @param character the {@link Character} object
     * @param interactionHandler the {@link UserInteractionHandler} object
     * @return the stored piece position
     */
    String getPosition(final Character character, final FfUserInteractionHandler interactionHandler);

}
