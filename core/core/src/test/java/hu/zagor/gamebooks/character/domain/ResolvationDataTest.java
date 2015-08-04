package hu.zagor.gamebooks.character.domain;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ResolvationData}.
 * @author Tamas_Szekeres
 */
@Test
public class ResolvationDataTest {

    public void testGetInfoShouldReturnInfo() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final ResolvationData underTest = new ResolvationData(rootData, character, null, info);
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
    }

    public void testGetEnemiesShouldReturnEnemies() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final ResolvationData underTest = new ResolvationData(rootData, character, enemies, info);
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertSame(returned, enemies);
    }

    public void testGetPlayerUserShouldReturnSetPlayerUser() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final ResolvationData underTest = new ResolvationData(rootData, character, enemies, info);
        final PlayerUser playerUser = new PlayerUser(29, "FireFoX", true);
        underTest.setPlayerUser(playerUser);
        // WHEN
        final PlayerUser returned = underTest.getPlayerUser();
        // THEN
        Assert.assertSame(returned, playerUser);
    }
}
