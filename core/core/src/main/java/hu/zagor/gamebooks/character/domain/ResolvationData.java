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

    private final ParagraphData rootData;
    private final Character character;
    private final Map<String, Enemy> enemies;
    private final BookInformations info;
    private PlayerUser playerUser;

    /**
     * Basic constructor.
     * @param rootData the root {@link ParagraphData} for a given section
     * @param character the {@link Character} in the book
     * @param enemies the current enemies
     * @param info the {@link BookInformations} object
     */
    public ResolvationData(final ParagraphData rootData, final Character character, final Map<String, Enemy> enemies, final BookInformations info) {
        this.rootData = rootData;
        this.character = character;
        this.enemies = enemies;
        this.info = info;
    }

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

}
