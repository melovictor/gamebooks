package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleFightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private SingleFightRoundResolver underTest;
    @Instance private FfEnemy enemy;
    @Mock private FfFightCommand command;
    @Inject private RandomNumberGenerator generator;

    public void testGetEnemyAttackStrengthWhenNoRequiredAttackStrengthAndBonusIsSetForEnemyShouldRollAndReturnResult() {
        // GIVEN
        enemy.setAttackStrength(0);
        enemy.setAttackStrengthBonus(0);
        final int[] randomNumbers = new int[]{9, 5, 4};
        expect(generator.getRandomNumber(2)).andReturn(randomNumbers);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertSame(returned, randomNumbers);
        Assert.assertEquals(returned[0], 9);
        Assert.assertEquals(returned[1], 5);
        Assert.assertEquals(returned[2], 4);
    }

    public void testGetEnemyAttackStrengthWhenNoRequiredAttackStrengthIsSetForEnemyButBonusIsShouldRollAndReturnIncreasedResult() {
        // GIVEN
        enemy.setAttackStrength(0);
        enemy.setAttackStrengthBonus(3);
        final int[] randomNumbers = new int[]{9, 5, 4};
        expect(generator.getRandomNumber(2)).andReturn(randomNumbers);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertSame(returned, randomNumbers);
        Assert.assertEquals(returned[0], 12);
        Assert.assertEquals(returned[1], 5);
        Assert.assertEquals(returned[2], 4);
    }

    public void testGetEnemyAttackStrengthWhenRequiredAttackStrengthIsSetForEnemyShouldFakeRollAndReturnRequestedResult() {
        // GIVEN
        enemy.setAttackStrength(10);
        enemy.setSkill(4);
        final Capture<DiceConfiguration> diceConfigCapture = newCapture();
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{4, 4});
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned[0], 6);
        Assert.assertEquals(returned[1], 4);
        Assert.assertEquals(returned[2], 2);
        final DiceConfiguration diceConfiguration = diceConfigCapture.getValue();
        Assert.assertEquals(diceConfiguration.getDiceNumber(), 1);
        Assert.assertEquals(diceConfiguration.getMinValue(), 1);
        Assert.assertEquals(diceConfiguration.getMaxValue(), 5);
    }

    public void testGetEnemyAttackStrengthWhenRequiredAttackStrengthIsSetForEnemyWithDouble1ResultShouldFakeRollAndReturnRequestedResult() {
        // GIVEN
        enemy.setAttackStrength(10);
        enemy.setSkill(8);
        final Capture<DiceConfiguration> diceConfigCapture = newCapture();
        expect(generator.getRandomNumber(capture(diceConfigCapture))).andReturn(new int[]{1, 1});
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getEnemyAttackStrength(enemy, command);
        // THEN
        Assert.assertEquals(returned[0], 2);
        Assert.assertEquals(returned[1], 1);
        Assert.assertEquals(returned[2], 1);
        final DiceConfiguration diceConfiguration = diceConfigCapture.getValue();
        Assert.assertEquals(diceConfiguration.getDiceNumber(), 1);
        Assert.assertEquals(diceConfiguration.getMinValue(), 1);
        Assert.assertEquals(diceConfiguration.getMaxValue(), 1);
    }

}
