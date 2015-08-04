package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.stat.StatisticsProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightCommandRoundEventResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandRoundEventResolverTest {

    private static final String ENEMY_ID = "9";
    private FightCommandRoundEventResolver underTest;
    private IMocksControl mockControl;
    private List<ParagraphData> resolveList;
    private FightCommand command;
    private BattleStatistics stats;
    private Map<String, BattleStatistics> statMap;
    private StatisticsProvider statProvider;
    private EventStatistics eventStatistics;
    private FfParagraphData data;
    private RoundEvent roundEvent;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightCommandRoundEventResolver();
        resolveList = new ArrayList<>();
        stats = mockControl.createMock(BattleStatistics.class);
        statMap = new HashMap<String, BattleStatistics>();
        statMap.put(ENEMY_ID, stats);
        final Map<FightRoundResult, StatisticsProvider> statProviders = new HashMap<>();
        statProvider = mockControl.createMock(StatisticsProvider.class);
        statProviders.put(FightRoundResult.WIN, statProvider);
        underTest.setStatProviders(statProviders);
        eventStatistics = mockControl.createMock(EventStatistics.class);
        data = mockControl.createMock(FfParagraphData.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        resolveList.clear();
        mockControl.reset();
        command = new FightCommand();
        command.setOngoing(true);
        Whitebox.setInternalState(command, "battleStatistics", statMap);
        roundEvent = new RoundEvent();
        roundEvent.setEnemyId(ENEMY_ID);
        roundEvent.setParagraphData(data);
        roundEvent.setRoundResult(FightRoundResult.WIN);
    }

    public void testResolveRoundEventWhenRoundEventsAreEmptyShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertTrue(resolveList.isEmpty());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichNoActingIsRequiredShouldDoNothing() {
        // GIVEN
        roundEvent.setTotalCount(5);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertTrue(resolveList.isEmpty());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveRoundEventWhenWrongRoundEventAvailableShouldThrowException() {
        // GIVEN
        roundEvent.setRoundResult(FightRoundResult.LOSE);
        roundEvent.setTotalCount(5);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN throws exception
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForTotalShouldAddRoundEventDataToResolveList() {
        // GIVEN
        roundEvent.setTotalCount(3);
        roundEvent.setSubsequentCount(-1);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(data.isInterrupt()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), data);
        Assert.assertTrue(command.isOngoing());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForSubsequentShouldAddRoundEventDataToResolveList() {
        // GIVEN
        roundEvent.setTotalCount(-1);
        roundEvent.setSubsequentCount(2);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        expect(data.isInterrupt()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), data);
        Assert.assertTrue(command.isOngoing());
    }

    public void testResolveRoundEventWhenSingleRoundEventAvailableOnWhichActingIsRequiredForSubsequentAndInterruptsBattleShouldAddRoundEventDataToResolveList() {
        // GIVEN
        roundEvent.setTotalCount(-1);
        roundEvent.setSubsequentCount(2);
        command.getRoundEvents().add(roundEvent);
        expect(statProvider.provideStatistics(stats, roundEvent)).andReturn(eventStatistics);
        expect(eventStatistics.getTotal()).andReturn(3);
        expect(eventStatistics.getSubsequent()).andReturn(2);
        expect(data.isInterrupt()).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.resolveRoundEvent(command, resolveList);
        // THEN
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), data);
        Assert.assertFalse(command.isOngoing());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
