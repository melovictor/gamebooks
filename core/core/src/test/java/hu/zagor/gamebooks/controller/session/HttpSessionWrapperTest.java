package hu.zagor.gamebooks.controller.session;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link HttpSessionWrapper}.
 * @author Tamas_Szekeres
 */
@Test
public class HttpSessionWrapperTest {

    private IMocksControl mockControl;
    private HttpSessionWrapper underTest;
    private HttpSession session;
    private PlayerUser player;
    private Character character;
    private Paragraph paragraph;
    private Model model;
    private HttpServletRequest request;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        session = mockControl.createMock(HttpSession.class);
        player = mockControl.createMock(PlayerUser.class);
        character = new Character();
        paragraph = mockControl.createMock(Paragraph.class);
        model = mockControl.createMock(Model.class);
        request = mockControl.createMock(HttpServletRequest.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        underTest = new HttpSessionWrapper(session);
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
        expect(session.getAttribute(ControllerAddresses.USER_STORE_KEY)).andReturn(player);
        mockControl.replay();
        // WHEN
        final PlayerUser returned = underTest.getPlayer();
        // THEN
        Assert.assertSame(returned, player);
    }

    public void testGetCharacterShouldReturnCharacterFromSession() {
        // GIVEN
        expect(session.getAttribute(ControllerAddresses.CHARACTER_STORE_KEY)).andReturn(character);
        mockControl.replay();
        // WHEN
        final Character returned = underTest.getCharacter();
        // THEN
        Assert.assertSame(returned, character);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetCharacterWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.setCharacter(null);
        // THEN throws exception
    }

    public void testSetCharacterWhenCharacterIsNotNullShouldSetCharacterIntoSession() {
        // GIVEN
        session.setAttribute(ControllerAddresses.CHARACTER_STORE_KEY, character);
        mockControl.replay();
        // WHEN
        final Character returned = underTest.setCharacter(character);
        // THEN
        Assert.assertSame(returned, character);
    }

    public void testGetParagraphShouldReturnParagraphFromSession() {
        // GIVEN
        expect(session.getAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY)).andReturn(paragraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.getParagraph();
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetParagraphWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.setParagraph(null);
        // THEN throws exception
    }

    public void testSetParagraphWhenParagraphIsNotNullShouldSetParagraphIntoSession() {
        // GIVEN
        session.setAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY, paragraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.setParagraph(paragraph);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testSetEnemiesWhenEnemiesIsNotNullShouldSetToSessionAndReturnSetEnemies() {
        // GIVEN
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        session.setAttribute(ControllerAddresses.ENEMY_STORE_KEY, enemies);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.setEnemies(enemies);
        // THEN
        Assert.assertSame(returned, enemies);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetEnemiesWhenEnemiesIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.setEnemies(null);
        // THEN throws exception
    }

    public void testGetEnemiesShouldReturnEnemiesFromSession() {
        // GIVEN
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        expect(session.getAttribute(ControllerAddresses.ENEMY_STORE_KEY)).andReturn(enemies);
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertSame(returned, enemies);
    }

    public void testGetEnemiesShouldReturnEmptyListWhenOneIsNotSetInSession() {
        // GIVEN
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        expect(session.getAttribute(ControllerAddresses.ENEMY_STORE_KEY)).andReturn(null);
        session.setAttribute(eq(ControllerAddresses.ENEMY_STORE_KEY), anyObject(List.class));
        mockControl.replay();
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertEquals(returned.size(), enemies.size());
        Assert.assertEquals(returned.size(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetModelWhenModelIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.setModel(null);
        // THEN throws exception
    }

    public void testSetModelWhenModelIsNotNullShouldSetModelIntoSession() {
        // GIVEN
        session.setAttribute(ControllerAddresses.MODEL_STORE_KEY, model);
        mockControl.replay();
        // WHEN
        final Model returned = underTest.setModel(model);
        // THEN
        Assert.assertSame(returned, model);
    }

    public void testGetModelShouldReturnModelFromSession() {
        // GIVEN
        expect(session.getAttribute(ControllerAddresses.MODEL_STORE_KEY)).andReturn(model);
        mockControl.replay();
        // WHEN
        final Model returned = underTest.getModel();
        // THEN
        Assert.assertSame(returned, model);
    }

    public void testGetRequestShouldReturnRequestSet() {
        // GIVEN
        underTest.setRequest(request);
        mockControl.replay();
        // WHEN
        final HttpServletRequest returned = underTest.getRequest();
        // THEN
        Assert.assertSame(returned, request);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
