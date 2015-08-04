package hu.zagor.gamebooks.books.saving;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateLoader;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateSaver;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the {@link GameStateHandler} interface that saves and loads the game state using serialization and a file on the server.
 * @author Tamas_Szekeres
 */
public class XmlGameStateHandler extends AbstractGameStateHandler {

    private static final String UTF_8 = "UTF-8";
    @Autowired
    private XmlGameStateLoader gameStateLoader;
    @Autowired
    private XmlGameStateSaver gameStateSaver;

    @Override
    protected void saveGameToFile(final SavedGameContainer container, final File saveFileLocation) {
        final Writer writer = (Writer) getBeanFactory().getBean("writer", saveFileLocation, UTF_8);
        try {
            writer.write(gameStateSaver.save(container.getSavedElements()));
            getLogger().info("Successfully finished saving game for user {} for book {}.", container.getUserId(), container.getBookId());
        } catch (final IOException exception) {
            reportSaveFailure(container, saveFileLocation, exception);
        }
        writer.close();
    }

    @Override
    protected void loadGameFromFile(final SavedGameContainer container, final File saveFileLocation) {
        final Scanner scanner = (Scanner) getBeanFactory().getBean("scanner", saveFileLocation, UTF_8);
        scanner.useDelimiter("/z");
        @SuppressWarnings("unchecked")
        final Map<String, Object> savedElements = (Map<String, Object>) gameStateLoader.load(scanner.next());
        if (savedElements == null) {
            reportLoadFailure(container, saveFileLocation, null);
        } else {
            saveLoadedData(container, savedElements);
        }
        scanner.close();
    }

}
