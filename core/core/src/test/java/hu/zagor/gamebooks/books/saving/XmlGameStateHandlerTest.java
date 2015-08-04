package hu.zagor.gamebooks.books.saving;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateLoader;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateSaver;
import hu.zagor.gamebooks.directory.DirectoryProvider;
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
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link XmlGameStateHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class XmlGameStateHandlerTest {

    private static final String SAVE_GAME_ROOT = "c:/savegameroot/prod";
    private static final long BOOK_ID = 11119L;
    private static final String BOOK_ID_STRING = String.valueOf(11119L);
    private static final int PLAYER_ID = 29;
    private static final String PLAYER_ID_STRING = String.valueOf(PLAYER_ID);
    private static final String XML_CONTENT = "this is supposed to be an xml text";
    private XmlGameStateHandler underTest;
    private IMocksControl mockControl;
    private XmlGameStateLoader gameStateLoader;
    private XmlGameStateSaver gameStateSaver;
    private BeanFactory beanFactory;
    private Logger logger;
    private Scanner scanner;
    private SavedGameContainer savedGameContainer;
    private File saveFile;
    private File saveFileDir;
    private Map<String, Object> savedElements;
    private Writer writer;
    private DirectoryProvider directoryProvider;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        gameStateLoader = mockControl.createMock(XmlGameStateLoader.class);
        gameStateSaver = mockControl.createMock(XmlGameStateSaver.class);
        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        savedGameContainer = new SavedGameContainer(PLAYER_ID, BOOK_ID);
        saveFile = mockControl.createMock(File.class);
        saveFileDir = mockControl.createMock(File.class);
        savedElements = new HashMap<>();
        scanner = mockControl.createMock(Scanner.class);
        writer = mockControl.createMock(Writer.class);
        directoryProvider = mockControl.createMock(DirectoryProvider.class);
        underTest = new XmlGameStateHandler();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "gameStateLoader", gameStateLoader);
        Whitebox.setInternalState(underTest, "gameStateSaver", gameStateSaver);
        Whitebox.setInternalState(underTest, "directoryProvider", directoryProvider);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGenerateSaveGameContainerShouldReturnSavedGameContainer() {
        // GIVEN
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID)).andReturn(savedGameContainer);
        mockControl.replay();
        // WHEN
        final SavedGameContainer returned = underTest.generateSavedGameContainer(PLAYER_ID, BOOK_ID);
        // THEN
        Assert.assertSame(returned, savedGameContainer);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadGameWhenContainerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadGame(null);
        // THEN throws exception
    }

    public void testLoadGameWhenEverythingIsSetUpProperlyShouldLoadGame() throws Exception {
        // GIVEN
        logger.info("Started loading game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.canRead()).andReturn(true);
        expect(beanFactory.getBean("scanner", saveFile, "UTF-8")).andReturn(scanner);
        scanner.useDelimiter("/z");
        expect(scanner.next()).andReturn(XML_CONTENT);
        expect(gameStateLoader.load(XML_CONTENT)).andReturn(savedElements);
        logger.info("Successfully finished loading game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadGame(savedGameContainer);
        // THEN
    }

    public void testLoadGameWhenSaveFileLocationNotProperShouldWarn() throws Exception {
        // GIVEN
        logger.info("Started loading game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.canRead()).andReturn(false);
        expect(saveFile.getAbsolutePath()).andReturn(SAVE_GAME_ROOT);
        logger.warn("Cannot open saved file '{}' for reading!", SAVE_GAME_ROOT);
        mockControl.replay();
        // WHEN
        underTest.loadGame(savedGameContainer);
        // THEN
    }

    public void testLoadGameWhenLoadedMapIsNullShouldWarn() throws Exception {
        // GIVEN
        logger.info("Started loading game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.canRead()).andReturn(true);
        expect(beanFactory.getBean("scanner", saveFile, "UTF-8")).andReturn(scanner);
        scanner.useDelimiter("/z");
        expect(scanner.next()).andReturn(XML_CONTENT);
        expect(gameStateLoader.load(XML_CONTENT)).andReturn(null);
        expect(saveFile.getAbsolutePath()).andReturn(SAVE_GAME_ROOT);
        logger.warn(eq("Failed to load game for book '{}' for user '{}' from location '{}'!"), aryEq(new Object[]{BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT}), eq(null));
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadGame(savedGameContainer);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveGameWhenContainerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.saveGame(null);
        // THEN throws exception
    }

    public void testGetSaveFileLocationWhenDirectoryDoesNotExistsAndCreationFailsShouldLogErrorAndReturnTempReference() {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(false);
        expect(saveFileDir.mkdirs()).andReturn(false);
        expect(saveFileDir.getAbsolutePath()).andReturn("save file absolute path");
        logger.error("Failed to create save game directory '{}', using temp directory instead!", "save file absolute path");
        final String tempDir = System.getProperty("java.io.tmpdir");
        expect(beanFactory.getBean("file", tempDir, BOOK_ID_STRING)).andReturn(saveFile);
        mockControl.replay();
        // WHEN
        final File returned = underTest.getSaveFileLocation(savedGameContainer);
        // THEN
        Assert.assertSame(returned, saveFile);
    }

    public void testGetSaveFileLocationWhenDirectoryDoesNotExistsShouldCreateIt() {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(false);
        expect(saveFileDir.mkdirs()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        mockControl.replay();
        // WHEN
        final File returned = underTest.getSaveFileLocation(savedGameContainer);
        // THEN
        Assert.assertSame(returned, saveFile);
    }

    public void testSaveGameWhenEverythingFitsShouldSaveGame() throws IOException {
        // GIVEN
        savedGameContainer.addElements(savedElements);

        logger.info("Started saving game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(beanFactory.getBean("writer", saveFile, "UTF-8")).andReturn(writer);
        expect(gameStateSaver.save(savedElements)).andReturn(XML_CONTENT);
        writer.write(XML_CONTENT);
        logger.info("Successfully finished saving game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveGame(savedGameContainer);
        // THEN
    }

    public void testSaveGameWhenWritingFailsShouldReportProblem() throws IOException {
        // GIVEN
        savedGameContainer.addElements(savedElements);

        logger.info("Started saving game for user {} for book {}.", PLAYER_ID, BOOK_ID);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        final IOException exception = new IOException("Problem at opening.");
        expect(beanFactory.getBean("writer", saveFile, "UTF-8")).andReturn(writer);
        expect(gameStateSaver.save(savedElements)).andReturn(XML_CONTENT);
        writer.write(XML_CONTENT);
        expectLastCall().andThrow(exception);
        expect(saveFile.getAbsolutePath()).andReturn(SAVE_GAME_ROOT);
        logger.warn(eq("Failed to save game for book '{}' for user '{}' to location '{}'!"), aryEq(new Object[]{BOOK_ID, PLAYER_ID, SAVE_GAME_ROOT}), eq(exception));
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveGame(savedGameContainer);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCheckSavedGameWhenContainerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.checkSavedGame(null);
        // THEN throws exception
    }

    public void testCheckSavedGameWhenTargetFileExistsShouldReturnTrue() {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(true);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(savedGameContainer);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testCheckSavedGameWhenPlayerIdAndBookIdProvidedAndTargetFileExistsShouldReturnTrue() {
        // GIVEN
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID)).andReturn(savedGameContainer);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(true);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(PLAYER_ID, BOOK_ID);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testCheckSavedGameWhenTargetFileDoesNotExistsShouldReturnFalse() {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, BOOK_ID_STRING)).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(false);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(savedGameContainer);
        // THEN
        Assert.assertFalse(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
