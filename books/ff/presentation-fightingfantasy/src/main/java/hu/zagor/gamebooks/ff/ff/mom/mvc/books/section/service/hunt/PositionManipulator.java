package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.UserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;

/**
 * Class for doing position manipulations.
 * @author Tamas_Szekeres
 */
public interface PositionManipulator {

    /**
     * Verifying the positions of the pieces.
     * @param result the {@link HuntRoundResult} object
     * @param currentRound the current round number
     */
    void verifyPositions(HuntRoundResult result, int currentRound);

    /**
     * Converts a position string (eg. "E12") into a proper CSS class for the display.
     * @param position the position string
     * @return the css class string
     */
    String cssClassFromPosition(String position);

    /**
     * Saves the current positions.
     * @param character the {@link Character} object
     * @param interactionHandler the {@link UserInteractionHandler} object
     * @param currentRound the number of the current round
     * @param result the {@link HuntRoundResult} object
     */
    void saveData(Character character, FfUserInteractionHandler interactionHandler, int currentRound, HuntRoundResult result);

    /**
     * Gets the current round number.
     * @param character the {@link Character} object
     * @param interactionHandler the {@link UserInteractionHandler} object
     * @return the number of the current round
     */
    int getRound(Character character, FfUserInteractionHandler interactionHandler);

}
