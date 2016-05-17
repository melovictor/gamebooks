package hu.zagor.gamebooks.books.saving;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SerializingGameStateHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class SerializingGameStateHandlerTest {

    private static final String SAVE_GAME_ROOT = "c:/savegameroot";
    private static final long BOOK_ID = 11119L;
    private static final int PLAYER_ID = 29;
    @UnderTest private SerializingGameStateHandler underTest;
    @MockControl private IMocksControl mockControl;

    @Inject private BeanFactory beanFactory;
    @Inject private Logger logger;
    @Mock private Scanner scanner;
    private SavedGameContainer savedGameContainer;
    @Mock private File saveFile;
    @Instance private Map<String, Object> savedElements;
    @Mock private Writer writer;

    @BeforeClass
    public void setUpClass() {
        savedGameContainer = new SavedGameContainer(PLAYER_ID, BOOK_ID);
    }

    public void testSaveGameToFileWhenEverythingIsGoodShouldSaveObject() throws IOException {
        // GIVEN
        savedGameContainer.addElements(savedElements);

        expect(beanFactory.getBean("writer", saveFile)).andReturn(writer);
        writer.write(savedElements);
        logger.info("Successfully finished saving game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveGameToFile(savedGameContainer, saveFile);
        // THEN
    }

    public void testSaveGameToFileWhenWritingFailsShouldReportProblem() throws IOException {
        // GIVEN
        savedGameContainer.addElements(savedElements);

        expect(beanFactory.getBean("writer", saveFile)).andReturn(writer);
        writer.write(savedElements);
        final IOException exception = new IOException();
        expectLastCall().andThrow(exception);
        expect(saveFile.getAbsolutePath()).andReturn(SAVE_GAME_ROOT);
        logger.warn("Failed to save game for book '{}' for user '{}' to location '{}'!", BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT);
        logger.warn("Thrown exception was: ", exception);
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveGameToFile(savedGameContainer, saveFile);
        // THEN
    }

    public void testLoadGameFromFileWhenEverythingIsGoodShouldLoadObject() throws ClassNotFoundException, IOException {
        // GIVEN
        expect(beanFactory.getBean("scanner", saveFile)).andReturn(scanner);
        expect(scanner.nextObject()).andReturn(savedElements);
        logger.info("Successfully finished loading game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadGameFromFile(savedGameContainer, saveFile);
        // THEN
    }

    public void testLoadGameFromFileWhenReadingFailsShouldReportProblem() throws ClassNotFoundException, IOException {
        // GIVEN
        expect(beanFactory.getBean("scanner", saveFile)).andReturn(scanner);
        final IOException exception = new IOException();
        expect(scanner.nextObject()).andThrow(exception);
        expect(saveFile.getAbsolutePath()).andReturn(SAVE_GAME_ROOT);
        logger.warn("Failed to load game for book '{}' for user '{}' from location '{}'!", BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT);
        logger.warn("Thrown exception was: ", exception);
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadGameFromFile(savedGameContainer, saveFile);
        // THEN
    }

}
