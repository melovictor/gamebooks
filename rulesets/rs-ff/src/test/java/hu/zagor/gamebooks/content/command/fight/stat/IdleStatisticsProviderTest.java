package hu.zagor.gamebooks.content.command.fight.stat;

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
 * Unit test for class {@link IdleStatisticsProvider}.
 * @author Tamas_Szekeres
 */
@Test
public class IdleStatisticsProviderTest {

    private IdleStatisticsProvider underTest;
    private IMocksControl mockControl;
    private BattleStatistics battleStatistics;
    private RoundEvent roundEvent;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new IdleStatisticsProvider();
        battleStatistics = mockControl.createMock(BattleStatistics.class);
        roundEvent = mockControl.createMock(RoundEvent.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testProvideStatisticsShouldReturnStatisticsProvidedByMocks() {
        // GIVEN
        final int total = -1;
        final int subsequent = -1;
        mockControl.replay();
        // WHEN
        final EventStatistics returned = underTest.provideStatistics(battleStatistics, roundEvent);
        // THEN
        Assert.assertEquals(returned.getTotal(), total);
        Assert.assertEquals(returned.getSubsequent(), subsequent);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
