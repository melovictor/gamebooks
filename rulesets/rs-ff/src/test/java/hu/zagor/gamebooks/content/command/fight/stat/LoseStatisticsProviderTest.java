package hu.zagor.gamebooks.content.command.fight.stat;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link LoseStatisticsProvider}.
 * @author Tamas_Szekeres
 */
@Test
public class LoseStatisticsProviderTest {

    private LoseStatisticsProvider underTest;
    private IMocksControl mockControl;
    private BattleStatistics battleStatistics;
    private RoundEvent roundEvent;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new LoseStatisticsProvider();
        battleStatistics = mockControl.createMock(BattleStatistics.class);
        roundEvent = mockControl.createMock(RoundEvent.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testProvideStatisticsWhenLastRoundWasLostShouldReturnStatisticsProvidedByMocks() {
        // GIVEN
        final int total = 3;
        final int subsequent = 2;
        expect(battleStatistics.getSubsequentLose()).andReturn(subsequent);
        expect(battleStatistics.getTotalLose()).andReturn(total);
        mockControl.replay();
        // WHEN
        final EventStatistics returned = underTest.provideStatistics(battleStatistics, roundEvent);
        // THEN
        Assert.assertEquals(returned.getTotal(), total);
        Assert.assertEquals(returned.getSubsequent(), subsequent);
    }

    public void testProvideStatisticsWhenLastRoundWasNotLostShouldReturnStatisticsProvidedByMocksWithoutTotal() {
        // GIVEN
        final int subsequent = 0;
        expect(battleStatistics.getSubsequentLose()).andReturn(subsequent);
        mockControl.replay();
        // WHEN
        final EventStatistics returned = underTest.provideStatistics(battleStatistics, roundEvent);
        // THEN
        Assert.assertEquals(returned.getTotal(), RoundEvent.TOTAL_NOT_MEANINGFUL);
        Assert.assertEquals(returned.getSubsequent(), subsequent);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
