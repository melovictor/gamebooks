package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import org.springframework.stereotype.Component;

/**
 * Provides statistics with the combined tied-lost rounds.
 * @author Tamas_Szekeres
 */
@Component("notWinStatisticsProvider")
public class NotWinStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        final int subsequentNotWon = battleStatistics.getSubsequentNotWon();
        return new EventStatistics(subsequentNotWon > 0 ? battleStatistics.getTotalNotWon() : RoundEvent.TOTAL_NOT_MEANINGFUL, subsequentNotWon);
    }
}
