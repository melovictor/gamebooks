package hu.zagor.gamebooks.mvc.book.save.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.EasyMockAnnotations;
import hu.zagor.gamebooks.support.mock.Inject;
import hu.zagor.gamebooks.support.mock.UnderTest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookSaveController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookSaveControllerTest {

    private static final Integer PLAYER_ID = 1112;
    private static final Long BOOK_ID = 543547L;
    private static final String S_BOOK_ID = String.valueOf(BOOK_ID);

    @UnderTest
    private GenericBookSaveController underTest;

    private IMocksControl mockControl;
    private BookInformations info;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PlayerUser player;
    @Mock
    private HttpSessionWrapper wrapper;
    @Mock
    private SavedGameContainer container;
    @Mock
    private HttpSession session;
    @Mock
    private Paragraph paragraph;
    @Mock
    private Map<String, Enemy> enemies;
    @Mock
    private Character character;

    @Inject
    private BeanFactory beanFactory;
    @Inject
    private BookInformationFetcher fetcher;
    @Inject
    private GameStateHandler gameStateHandler;

    @BeforeClass
    public void setUpClass() {
        EasyMockAnnotations.initialize(this);
        info = new BookInformations(BOOK_ID);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleSaveWhenRequestIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleSave(null, S_BOOK_ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleSaveWhenBookIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleSave(request, null);
        // THEN throws exception
    }

    public void testHandleSaveShouldSaveGame() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getId()).andReturn(PLAYER_ID);

        expect(fetcher.getInfoById(S_BOOK_ID)).andReturn(info);

        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID)).andReturn(container);

        expect(wrapper.getParagraph()).andReturn(paragraph);
        container.addElement(ControllerAddresses.PARAGRAPH_STORE_KEY, paragraph);
        expect(wrapper.getEnemies()).andReturn(enemies);
        container.addElement(ControllerAddresses.ENEMY_STORE_KEY, enemies);
        expect(wrapper.getCharacter()).andReturn(character);
        container.addElement(ControllerAddresses.CHARACTER_STORE_KEY, character);

        gameStateHandler.saveGame(container);
        mockControl.replay();
        // WHEN
        underTest.handleSave(request, S_BOOK_ID);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
