package hu.zagor.gamebooks.mvc.book.save.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
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
    private GenericBookSaveController underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private HttpServletRequest request;
    private PlayerUser player;
    private HttpSessionWrapper wrapper;
    private SavedGameContainer container;
    private GameStateHandler gameStateHandler;
    private HttpSession session;
    private BeanFactory beanFactory;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        player = mockControl.createMock(PlayerUser.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        container = mockControl.createMock(SavedGameContainer.class);
        gameStateHandler = mockControl.createMock(GameStateHandler.class);
        session = mockControl.createMock(HttpSession.class);
        beanFactory = mockControl.createMock(BeanFactory.class);

        info = new BookInformations(BOOK_ID);

        underTest = new TestController();
        underTest.setGameStateHandler(gameStateHandler);
        underTest.setBeanFactory(beanFactory);
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
        underTest.handleSave(null);
        // THEN throws exception
    }

    public void testHandleSaveShouldSaveGame() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(player.getId()).andReturn(PLAYER_ID);
        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID)).andReturn(container);
        gameStateHandler.saveGame(container);
        mockControl.replay();
        // WHEN
        underTest.handleSave(request);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestController extends GenericBookSaveController {

        @Override
        protected void doSave(final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
        }

    }
}
