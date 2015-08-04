package hu.zagor.gamebooks.mvc.book.load.controller;

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
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookLoadController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookLoadControllerTest {

    protected static final String RETURNED = "111";
    private static final Integer PLAYER_ID = 7;
    private static final Long BOOK_ID = 56453413524564L;
    private GenericBookLoadController underTest;
    private IMocksControl mockControl;
    private BookInformations info;
    private HttpServletRequest request;
    private Model model;
    private HttpSession session;
    private BeanFactory beanFactory;
    private HttpSessionWrapper sessionWrapper;
    private GameStateHandler gameStateHandler;
    private PlayerUser playerUser;
    private SavedGameContainer container;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        sessionWrapper = mockControl.createMock(HttpSessionWrapper.class);
        gameStateHandler = mockControl.createMock(GameStateHandler.class);
        model = mockControl.createMock(Model.class);
        container = mockControl.createMock(SavedGameContainer.class);
        playerUser = mockControl.createMock(PlayerUser.class);
        logger = mockControl.createMock(Logger.class);

        info = new BookInformations(BOOK_ID);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new GenericBookLoadController() {

            @Override
            protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
                return RETURNED;
            }
        };
        Whitebox.setInternalState(underTest, "info", info);
        underTest.setBeanFactory(beanFactory);
        underTest.setGameStateHandler(gameStateHandler);
        Whitebox.setInternalState(underTest, "logger", logger);
        mockControl.reset();
    }

    public void testHandleLoadWhenCalledShouldCallAbstractMethod() {
        // GIVEN
        logger.debug("GenericBookLoadController.load");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(sessionWrapper);
        sessionWrapper.setRequest(request);
        expect(sessionWrapper.getPlayer()).andReturn(playerUser);
        expect(playerUser.getId()).andReturn(PLAYER_ID);
        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID)).andReturn(container);
        gameStateHandler.loadGame(container);
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleLoad(model, request);
        // THEN
        Assert.assertEquals(returned, RETURNED);
    }

    public void testGetInfoShouldReturnStoredInfo() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
