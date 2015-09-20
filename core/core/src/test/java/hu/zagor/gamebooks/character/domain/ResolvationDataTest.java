package hu.zagor.gamebooks.character.domain;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
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
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character)
            .withPosition(9).build();
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
        Assert.assertEquals(underTest.getPosition().intValue(), 9);
    }

    public void testPositionShouldReturnPosition() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final int position = 9;
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character)
            .withPosition(position).build();
        // WHEN
        final int returned = underTest.getPosition();
        // THEN
        Assert.assertEquals(returned, position);
    }

    public void testGetPlayerUserShouldReturnPlayer() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final PlayerUser player = new PlayerUser(29, "FireFoX", true);
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character)
            .withPlayer(player).build();
        // WHEN
        final PlayerUser returned = underTest.getPlayerUser();
        // THEN
        Assert.assertSame(returned, player);
    }

    public void testGetEnemiesShouldReturnEnemies() {
        // GIVEN
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final BookInformations info = new BookInformations(1L);
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character)
            .withEnemies(enemies).build();
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
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character)
            .withEnemies(enemies).build();
        final PlayerUser playerUser = new PlayerUser(29, "FireFoX", true);
        underTest.setPlayerUser(playerUser);
        // WHEN
        final PlayerUser returned = underTest.getPlayerUser();
        // THEN
        Assert.assertSame(returned, playerUser);
    }

    public void testGetCharacterWhenObtainedFromResolvationDataShouldReturnCharacterOutsideOfOriginalResolvationData() {
        // GIVEN
        final ResolvationData resolvationData = new ResolvationData();
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final BookInformations info = new BookInformations(1L);
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final Character alternateCharacter = new Character();
        resolvationData.setCharacter(alternateCharacter);
        resolvationData.setRootData(rootData);
        resolvationData.setEnemies(enemies);
        resolvationData.setInfo(info);
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(character).build();
        // WHEN
        final Character returned = underTest.getCharacter();
        // THEN
        Assert.assertSame(returned, character);
    }

    public void testGetRootDataWhenObtainedFromResolvationDataShouldReturnRootDataFromOriginalResolvationData() {
        // GIVEN
        final ResolvationData resolvationData = new ResolvationData();
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final BookInformations info = new BookInformations(1L);
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final Character alternateCharacter = new Character();
        resolvationData.setCharacter(alternateCharacter);
        resolvationData.setRootData(rootData);
        resolvationData.setEnemies(enemies);
        resolvationData.setInfo(info);
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(character).build();
        // WHEN
        final ParagraphData returned = underTest.getRootData();
        // THEN
        Assert.assertSame(returned, rootData);
    }

    public void testGetEnemiesWhenObtainedFromResolvationDataShouldReturnEnemiesFromOriginalResolvationData() {
        // GIVEN
        final ResolvationData resolvationData = new ResolvationData();
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final BookInformations info = new BookInformations(1L);
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final Character alternateCharacter = new Character();
        resolvationData.setCharacter(alternateCharacter);
        resolvationData.setRootData(rootData);
        resolvationData.setEnemies(enemies);
        resolvationData.setInfo(info);
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(character).build();
        // WHEN
        final Map<String, Enemy> returned = underTest.getEnemies();
        // THEN
        Assert.assertSame(returned, enemies);
    }

    public void testGetInfoWhenObtainedFromResolvationDataShouldReturnInfoFromOriginalResolvationData() {
        // GIVEN
        final ResolvationData resolvationData = new ResolvationData();
        final Map<String, Enemy> enemies = new HashMap<String, Enemy>();
        final BookInformations info = new BookInformations(1L);
        final ParagraphData rootData = new ParagraphData();
        final Character character = new Character();
        final Character alternateCharacter = new Character();
        resolvationData.setCharacter(alternateCharacter);
        resolvationData.setRootData(rootData);
        resolvationData.setEnemies(enemies);
        resolvationData.setInfo(info);
        final ResolvationData underTest = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(character).build();
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
    }

}
