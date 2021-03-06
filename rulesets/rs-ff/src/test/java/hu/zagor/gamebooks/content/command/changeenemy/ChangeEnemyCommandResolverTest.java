package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.Paragraph;
import java.util.HashMap;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfChangeEnemyCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeEnemyCommandResolverTest {

    private FfChangeEnemyCommandResolver underTest;
    private IMocksControl mockControl;
    private ChangeEnemyCommand command;
    private ResolvationData resolvationData;
    private Map<String, Enemy> enemies;
    private FfEnemy enemy;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FfChangeEnemyCommandResolver();
        command = new ChangeEnemyCommand();
        enemies = new HashMap<>();
        enemy = new FfEnemy();
        enemies.put("26a", enemy);
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(null);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(null).withCharacter(null).withEnemies(enemies).build();
        command.setId("26a");
        command.setAttribute("skill");
    }

    @BeforeMethod
    public void setUpMethod() {
        command.setNewValue(null);
        command.setChangeValue(null);
        enemy.setSkill(7);
        mockControl.reset();
    }

    public void testDoResolveWhenNewValueIsSetShouldSetNewValueForEnemy() {
        // GIVEN
        command.setNewValue(9);
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(enemy.getSkill(), 9);
    }

    public void testDoResolveWhenChangeValueIsSetShouldModifyValueForEnemy() {
        // GIVEN
        command.setChangeValue("-1");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(enemy.getSkill(), 6);
    }

    public void testDoResolveWhenChangeValueIsSetWithMultiplicationShouldModifyValueForEnemy() {
        // GIVEN
        command.setChangeValue("*2");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(enemy.getSkill(), 14);
    }

    public void testDoResolveWhenChangeValueIsSetWithDivisionShouldModifyValueForEnemy() {
        // GIVEN
        command.setChangeValue("/2");
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(enemy.getSkill(), 3);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
