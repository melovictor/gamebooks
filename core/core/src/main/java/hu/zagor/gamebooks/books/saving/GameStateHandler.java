package hu.zagor.gamebooks.books.saving;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.domain.ContinuationData;

/**
 * Interface for managing a game's state through a {@link SavedGameContainer} bean.
 * @author Tamas_Szekeres
 */
public interface GameStateHandler {

    /**
     * Loads the game state whose data (username and bookId) are provided by the given container. All the other data will be filled in by the implementor.
     * @param container the {@link SavedGameContainer} container that contains the data necessary to load the old state, and which will be filled with new data, cannot be null
     */
    void loadGame(SavedGameContainer container);

    /**
     * Saves the game state provided by the given container.
     * @param container the {@link SavedGameContainer} container containing the state that has to be saved, cannot be null
     */
    void saveGame(SavedGameContainer container);

    /**
     * Checks whether there is a saved game for the given user and book combination represented by the {@link SavedGameContainer}.
     * @param container the container containing the name of the user and the book's id for which we're looking for saved states, cannot be null
     * @return true, if there is a saved game, false otherwise
     */
    boolean checkSavedGame(SavedGameContainer container);

    /**
     * Checks whether there is a saved game for the given user and book combination.
     * @param playerId the id of the player
     * @param bookId the id of the book
     * @return true, if there is a saved game, false otherwise
     */
    boolean checkSavedGame(int playerId, long bookId);

    /**
     * Checks whether there is a saved game for the given user for the book that is the prequiel for the current one.
     * @param playerId the id of the player
     * @param continuationData the {@link ContinuationData} object containing information about the saved game
     * @return true, if there is a correct saved game from the previous adventure, false otherwise
     */
    boolean checkSavedGame(int playerId, ContinuationData continuationData);

    /**
     * Generates a basic {@link SavedGameContainer} from the player's and the book's id.
     * @param playerId the id of the player
     * @param bookId the id of the book
     * @return the {@link SavedGameContainer} bean containing the username and the currently opened book's id
     */
    SavedGameContainer generateSavedGameContainer(final int playerId, final long bookId);

}
