package hu.zagor.gamebooks.books.saving;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateLoader;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import hu.zagor.gamebooks.support.scanner.Scanner;
import java.io.File;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link XmlGameStateHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class XmlGameStateHandlerPreviousGameTest {

    private static final String SAVE_GAME_ROOT = "c:/savegameroot/prod";
    private static final long BOOK_ID = 11119L;
    private static final int PLAYER_ID = 29;
    private static final String PLAYER_ID_STRING = String.valueOf(PLAYER_ID);
    private static final String XML_CONTENT = "this is supposed to be an xml text";
    @UnderTest private XmlGameStateHandler underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private XmlGameStateLoader gameStateLoader;
    @Inject private BeanFactory beanFactory;
    @Inject private Logger logger;
    @Mock private Scanner scanner;
    @Mock private File saveFile;
    @Mock private File saveFileDir;
    @Instance private Map<String, Object> savedElements;
    @Inject private DirectoryProvider directoryProvider;
    @Instance private ContinuationData continuationData;
    private SavedGameContainer savedGameContainerPrevious;
    @Mock private Paragraph paragraph;

    @BeforeClass
    public void setUpClass() {
        savedGameContainerPrevious = new SavedGameContainer(PLAYER_ID, BOOK_ID - 1);
        continuationData.setContinuationPageName("s-background");
        continuationData.setPreviousBookId(BOOK_ID - 1);
        continuationData.setPreviousBookLastSectionId("456");
        savedElements.put(ControllerAddresses.PARAGRAPH_STORE_KEY, paragraph);
    }

    public void testCheckSavedGameWhenNoSavedGameExistsFromPreviousBookShouldReturnFalse() {
        // GIVEN
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID - 1)).andReturn(savedGameContainerPrevious);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, "11118")).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(false);

        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(PLAYER_ID, continuationData);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testCheckSavedGameWhenPreviousSavedGameIsAtWrongLocationShouldReturnFalse() {
        // GIVEN
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID - 1)).andReturn(savedGameContainerPrevious);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, "11118")).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(true);
        loadGame();
        expect(paragraph.getId()).andReturn("145");
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(PLAYER_ID, continuationData);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testCheckSavedGameWhenPreviousSavedGameIsAtCorrectLocationShouldReturnTrue() {
        // GIVEN
        expect(beanFactory.getBean("savedGameContainer", PLAYER_ID, BOOK_ID - 1)).andReturn(savedGameContainerPrevious);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, "11118")).andReturn(saveFile);
        expect(saveFile.exists()).andReturn(true);
        loadGame();
        expect(paragraph.getId()).andReturn("456");
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.checkSavedGame(PLAYER_ID, continuationData);
        // THEN
        Assert.assertTrue(returned);
    }

    private void loadGame() {
        logger.info("Started loading game for user {} for book {}.", PLAYER_ID, BOOK_ID - 1);
        expect(directoryProvider.getSaveGameDirectory()).andReturn(SAVE_GAME_ROOT);
        expect(beanFactory.getBean("file", SAVE_GAME_ROOT, PLAYER_ID_STRING)).andReturn(saveFileDir);
        expect(saveFileDir.exists()).andReturn(true);
        expect(beanFactory.getBean("file", saveFileDir, "11118")).andReturn(saveFile);
        expect(saveFile.canRead()).andReturn(true);
        expect(beanFactory.getBean("scanner", saveFile, "UTF-8")).andReturn(scanner);
        scanner.forceFullFileLoading();
        expect(scanner.next()).andReturn(XML_CONTENT);
        expect(gameStateLoader.load(XML_CONTENT)).andReturn(savedElements);
        logger.info("Successfully finished loading game for user {} for book {}.", PLAYER_ID, BOOK_ID - 1);
        scanner.close();
    }

}
