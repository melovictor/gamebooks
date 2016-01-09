package hu.zagor.gamebooks.controller.session;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link HttpSessionWrapper}.
 * @author Tamas_Szekeres
 */
@Test
public class HttpSessionWrapperTest {
    private static final String REQUEST_URI = "/gamebooks/books/99842332001/new";
    private static final String BOOK_ID = "99842332001";
    @MockControl private IMocksControl mockControl;
    @Mock private HttpSession session;
    @Mock private PlayerUser player;
    @Instance private Character character;
    @Mock private Paragraph paragraph;
    @Mock private Model model;
    @Mock private HttpServletRequest request;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenSessionIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new HttpSessionWrapper(null).getClass();
        // THEN throws exception
    }

    public void testGetPlayerShouldReturnPlayerUserFromSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(session.getAttribute(ControllerAddresses.USER_STORE_KEY)).andReturn(player);
        mockControl.replay();
        // WHEN
        final PlayerUser returned = new HttpSessionWrapper(request).getPlayer();
        // THEN
        Assert.assertSame(returned, player);
    }

    public void testGetCharacterShouldReturnCharacterFromSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        expect(session.getAttribute(ControllerAddresses.CHARACTER_STORE_KEY + BOOK_ID)).andReturn(character);
        mockControl.replay();
        // WHEN
        final Character returned = new HttpSessionWrapper(request).getCharacter();
        // THEN
        Assert.assertSame(returned, character);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetCharacterWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        mockControl.replay();
        // WHEN
        new HttpSessionWrapper(request).setCharacter(null);
        // THEN throws exception
    }

    public void testSetCharacterWhenCharacterIsNotNullShouldSetCharacterIntoSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        session.setAttribute(ControllerAddresses.CHARACTER_STORE_KEY + BOOK_ID, character);
        mockControl.replay();
        // WHEN
        final Character returned = new HttpSessionWrapper(request).setCharacter(character);
        // THEN
        Assert.assertSame(returned, character);
    }

    public void testGetParagraphShouldReturnParagraphFromSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        expect(session.getAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY + BOOK_ID)).andReturn(paragraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = new HttpSessionWrapper(request).getParagraph();
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetParagraphWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        mockControl.replay();
        // WHEN
        new HttpSessionWrapper(request).setParagraph(null);
        // THEN throws exception
    }

    public void testSetParagraphWhenParagraphIsNotNullShouldSetParagraphIntoSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        session.setAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY + BOOK_ID, paragraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = new HttpSessionWrapper(request).setParagraph(paragraph);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testSetEnemiesWhenEnemiesIsNotNullShouldSetToSessionAndReturnSetEnemies() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        session.setAttribute(ControllerAddresses.ENEMY_STORE_KEY + BOOK_ID, enemies);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = new HttpSessionWrapper(request).setEnemies(enemies);
        // THEN
        Assert.assertSame(returned, enemies);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetEnemiesWhenEnemiesIsNullShouldThrowException() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        mockControl.replay();
        // WHEN
        new HttpSessionWrapper(request).setEnemies(null);
        // THEN throws exception
    }

    public void testGetEnemiesShouldReturnEnemiesFromSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        expect(session.getAttribute(ControllerAddresses.ENEMY_STORE_KEY + BOOK_ID)).andReturn(enemies);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = new HttpSessionWrapper(request).getEnemies();
        // THEN
        Assert.assertSame(returned, enemies);
    }

    public void testGetEnemiesShouldReturnEmptyListWhenOneIsNotSetInSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        expect(request.getRequestURI()).andReturn(REQUEST_URI);
        expect(session.getAttribute(ControllerAddresses.ENEMY_STORE_KEY + BOOK_ID)).andReturn(null);
        session.setAttribute(eq(ControllerAddresses.ENEMY_STORE_KEY + BOOK_ID), anyObject(List.class));
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = new HttpSessionWrapper(request).getEnemies();
        // THEN
        Assert.assertEquals(returned.size(), enemies.size());
        Assert.assertEquals(returned.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetModelWhenModelIsNullShouldThrowException() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        mockControl.replay();
        // WHEN
        new HttpSessionWrapper(request).setModel(null);
        // THEN throws exception
    }

    public void testSetModelWhenModelIsNotNullShouldSetModelIntoSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        session.setAttribute(ControllerAddresses.MODEL_STORE_KEY, model);
        mockControl.replay();
        // WHEN
        final Model returned = new HttpSessionWrapper(request).setModel(model);
        // THEN
        Assert.assertSame(returned, model);
    }

    public void testGetModelShouldReturnModelFromSession() {
        // GIVEN
        expect(request.getSession()).andReturn(session);
        expect(session.getAttribute(ControllerAddresses.MODEL_STORE_KEY)).andReturn(model);
        mockControl.replay();
        // WHEN
        final Model returned = new HttpSessionWrapper(request).getModel();
        // THEN
        Assert.assertSame(returned, model);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
