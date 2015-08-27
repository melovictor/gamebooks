package hu.zagor.gamebooks.character.domain;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.Map;

/**
 * Bean for transporting related instances.
 * @author Tamas_Szekeres
 */
public class ResolvationData {

    private ParagraphData rootData;
    private BookInformations info;
    private Character character;
    private Map<String, Enemy> enemies;
    private PlayerUser playerUser;
    private Integer position;

    public ParagraphData getRootData() {
        return rootData;
    }

    public Character getCharacter() {
        return character;
    }

    public CharacterHandler getCharacterHandler() {
        return info == null ? null : info.getCharacterHandler();
    }

    public BookInformations getInfo() {
        return info;
    }

    public Map<String, Enemy> getEnemies() {
        return enemies;
    }

    public PlayerUser getPlayerUser() {
        return playerUser;
    }

    public void setPlayerUser(final PlayerUser playerUser) {
        this.playerUser = playerUser;
    }

    public void setRootData(final ParagraphData rootData) {
        this.rootData = rootData;
    }

    public void setCharacter(final Character character) {
        this.character = character;
    }

    public void setEnemies(final Map<String, Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setInfo(final BookInformations info) {
        this.info = info;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

}
