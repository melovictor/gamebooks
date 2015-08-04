package hu.zagor.gamebooks.content.command.fight.domain;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BattleStatistics}.
 * @author Tamas_Szekeres
 */
@Test
public class BattleStatisticsTest {

    private BattleStatistics underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new BattleStatistics();
    }

    public void testUpdateStatsWhenWinShouldSetProperFields() {
        // GIVEN
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.TIE);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.IDLE);
        // WHEN
        underTest.updateStats(FightRoundResult.WIN);
        // THEN
        Assert.assertEquals(underTest.getSubsequentLose(), 0);
        Assert.assertEquals(underTest.getSubsequentNotWon(), 0);
        Assert.assertEquals(underTest.getSubsequentTie(), 0);
        Assert.assertEquals(underTest.getSubsequentWin(), 3);

        Assert.assertEquals(underTest.getTotalLose(), 2);
        Assert.assertEquals(underTest.getTotalNotWon(), 3);
        Assert.assertEquals(underTest.getTotalTie(), 1);
        Assert.assertEquals(underTest.getTotalWin(), 3);
    }

    public void testUpdateStatsWhenLoseShouldSetProperFields() {
        // GIVEN
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.TIE);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.IDLE);
        // WHEN
        underTest.updateStats(FightRoundResult.LOSE);
        // THEN
        Assert.assertEquals(underTest.getSubsequentLose(), 1);
        Assert.assertEquals(underTest.getSubsequentNotWon(), 1);
        Assert.assertEquals(underTest.getSubsequentTie(), 0);
        Assert.assertEquals(underTest.getSubsequentWin(), 0);

        Assert.assertEquals(underTest.getTotalLose(), 3);
        Assert.assertEquals(underTest.getTotalNotWon(), 4);
        Assert.assertEquals(underTest.getTotalTie(), 1);
        Assert.assertEquals(underTest.getTotalWin(), 2);
    }

    public void testUpdateStatsWhenTieShouldSetProperFields() {
        // GIVEN
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.TIE);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.IDLE);
        // WHEN
        underTest.updateStats(FightRoundResult.TIE);
        // THEN
        Assert.assertEquals(underTest.getSubsequentLose(), 0);
        Assert.assertEquals(underTest.getSubsequentNotWon(), 1);
        Assert.assertEquals(underTest.getSubsequentTie(), 1);
        Assert.assertEquals(underTest.getSubsequentWin(), 0);

        Assert.assertEquals(underTest.getTotalLose(), 2);
        Assert.assertEquals(underTest.getTotalNotWon(), 4);
        Assert.assertEquals(underTest.getTotalTie(), 2);
        Assert.assertEquals(underTest.getTotalWin(), 2);
    }

    public void testUpdateStatsWhenTieAfterLoseShouldSetProperFields() {
        // GIVEN
        underTest.updateStats(FightRoundResult.TIE);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.IDLE);
        underTest.updateStats(FightRoundResult.LOSE);
        // WHEN
        underTest.updateStats(FightRoundResult.TIE);
        // THEN
        Assert.assertEquals(underTest.getSubsequentLose(), 0);
        Assert.assertEquals(underTest.getSubsequentNotWon(), 3);
        Assert.assertEquals(underTest.getSubsequentTie(), 1);
        Assert.assertEquals(underTest.getSubsequentWin(), 0);

        Assert.assertEquals(underTest.getTotalLose(), 2);
        Assert.assertEquals(underTest.getTotalNotWon(), 4);
        Assert.assertEquals(underTest.getTotalTie(), 2);
        Assert.assertEquals(underTest.getTotalWin(), 2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateStatsWhenNotWinShouldThrowException() {
        // GIVEN
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.LOSE);
        underTest.updateStats(FightRoundResult.TIE);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.WIN);
        underTest.updateStats(FightRoundResult.IDLE);
        // WHEN
        underTest.updateStats(FightRoundResult.NOT_WIN);
        // THEN throws exceptions
    }

    public void testCloneShouldReturnClone() throws CloneNotSupportedException {
        // GIVEN
        // WHEN
        final BattleStatistics returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
    }
}
