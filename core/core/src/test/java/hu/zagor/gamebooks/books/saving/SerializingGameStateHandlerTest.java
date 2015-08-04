package hu.zagor.gamebooks.books.saving;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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
    private SerializingGameStateHandler underTest;
    private IMocksControl mockControl;

    private BeanFactory beanFactory;
    private Logger logger;
    private Scanner scanner;
    private SavedGameContainer savedGameContainer;
    private File saveFile;
    private Map<String, Object> savedElements;
    private Writer writer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        savedGameContainer = new SavedGameContainer(PLAYER_ID, BOOK_ID);
        saveFile = mockControl.createMock(File.class);
        savedElements = new HashMap<>();
        scanner = mockControl.createMock(Scanner.class);
        writer = mockControl.createMock(Writer.class);

        underTest = new SerializingGameStateHandler();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
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
        logger.warn(eq("Failed to save game for book '{}' for user '{}' to location '{}'!"), aryEq(new Object[] {BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT}), eq(exception));
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
        logger.warn(eq("Failed to load game for book '{}' for user '{}' from location '{}'!"), aryEq(new Object[] {BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT}), eq(exception));
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadGameFromFile(savedGameContainer, saveFile);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
