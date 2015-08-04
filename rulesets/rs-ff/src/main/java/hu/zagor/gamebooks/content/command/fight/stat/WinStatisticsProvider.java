package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.springframework.stereotype.Component;

/**
 * Provides win statistics.
 * @author Tamas_Szekeres
 */
@Component("winStatisticsProvider")
public class WinStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        final int subsequentWin = battleStatistics.getSubsequentWin();
        return new EventStatistics(subsequentWin > 0 ? battleStatistics.getTotalWin() : RoundEvent.TOTAL_NOT_MEANINGFUL, subsequentWin);
    }
}
