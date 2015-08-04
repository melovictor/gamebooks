package hu.zagor.gamebooks.books.saving;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Implementation of the {@link GameStateHandler} interface that saves and loads the game state using serialization and a file on the server.
 * @author Tamas_Szekeres
 */
public class SerializingGameStateHandler extends AbstractGameStateHandler {

    @Override
    protected void saveGameToFile(final SavedGameContainer container, final File saveFileLocation) {
        final Writer writer = (Writer) getBeanFactory().getBean("writer", saveFileLocation);
        try {
            writer.write(container.getSavedElements());
            getLogger().info("Successfully finished saving game for user {} for book {}.", container.getUserId(), container.getBookId());
        } catch (final IOException e) {
            reportSaveFailure(container, saveFileLocation, e);
        }
        writer.close();
    }

    @Override
    protected void loadGameFromFile(final SavedGameContainer container, final File saveFileLocation) {
        final Scanner scanner = (Scanner) getBeanFactory().getBean("scanner", saveFileLocation);
        try {
            @SuppressWarnings("unchecked")
            final Map<String, Object> savedElements = (Map<String, Object>) scanner.nextObject();
            saveLoadedData(container, savedElements);
        } catch (final IOException | ClassNotFoundException e) {
            reportLoadFailure(container, saveFileLocation, e);
        }
        scanner.close();
    }

}
