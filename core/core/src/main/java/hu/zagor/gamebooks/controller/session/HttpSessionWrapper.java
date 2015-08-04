package hu.zagor.gamebooks.controller.session;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * A helper class that can be used to comfortably save and retrieve data from the {@link HttpSession} bean.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class HttpSessionWrapper {

    private final HttpSession session;
    private HttpServletRequest request;

    /**
     * Creates a new {@link HttpSessionWrapper} around the given {@link HttpSession} bean.
     * @param session the bean to wrap, cannot be null
     */
    public HttpSessionWrapper(final HttpSession session) {
        Assert.notNull(session, "The parameter 'session' cannot be null!");
        this.session = session;
    }

    public PlayerUser getPlayer() {
        return (PlayerUser) session.getAttribute(ControllerAddresses.USER_STORE_KEY);
    }

    public Character getCharacter() {
        return (Character) session.getAttribute(ControllerAddresses.CHARACTER_STORE_KEY);
    }

    /**
     * Stores the given {@link Character} bean in the session, then returns it for further use.
     * @param character the {@link Character} to store
     * @return the same {@link Character} bean
     */
    public Character setCharacter(final Character character) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        session.setAttribute(ControllerAddresses.CHARACTER_STORE_KEY, character);
        return character;
    }

    /**
     * Provides a map of enemies. If there is no map saved yet a new, empty one will be created, saved and returned.
     * @return the map of enemies
     */
    @SuppressWarnings("unchecked")
    public Map<String, Enemy> getEnemies() {
        Map<String, Enemy> enemies = (Map<String, Enemy>) session.getAttribute(ControllerAddresses.ENEMY_STORE_KEY);
        if (enemies == null) {
            enemies = new HashMap<>();
            setEnemies(enemies);
        }
        return enemies;
    }

    /**
     * Stores the given map of {@link Enemy} object beans in the session, then returns it for further use.
     * @param map the map to store
     * @return the same map
     */
    public Map<String, Enemy> setEnemies(final Map<String, Enemy> map) {
        Assert.notNull(map, "The parameter 'enemies' cannot be null!");
        session.setAttribute(ControllerAddresses.ENEMY_STORE_KEY, map);
        return map;
    }

    public Paragraph getParagraph() {
        return (Paragraph) session.getAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY);
    }

    /**
     * Stores the given {@link Paragraph} bean in the session, then returns it for further use.
     * @param paragraph the {@link Paragraph} to store
     * @return the same {@link Paragraph} bean
     */
    public Paragraph setParagraph(final Paragraph paragraph) {
        Assert.notNull(paragraph, "The parameter 'paragraph' cannot be null!");
        session.setAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY, paragraph);
        return paragraph;
    }

    /**
     * Stores the given {@link Model} bean in the session, then returns it for further use.
     * @param model the {@link Model} to store
     * @return the same {@link Model} bean
     */
    public Model setModel(final Model model) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        session.setAttribute(ControllerAddresses.MODEL_STORE_KEY, model);
        return model;
    }

    public Model getModel() {
        return (Model) session.getAttribute(ControllerAddresses.MODEL_STORE_KEY);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(final HttpServletRequest request) {
        this.request = request;
    }

}
