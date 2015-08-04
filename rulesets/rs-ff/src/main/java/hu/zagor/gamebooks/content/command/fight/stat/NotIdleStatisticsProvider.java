package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.springframework.stereotype.Component;

/**
 * Provides statistics that will fit on none of the battle rounds.
 * @author Tamas_Szekeres
 */
@Component("notIdleStatisticsProvider")
public class NotIdleStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        final int subsequentNotIdle = battleStatistics.getSubsequentNotWon() + battleStatistics.getSubsequentWin();
        return new EventStatistics(subsequentNotIdle > 0 ? battleStatistics.getTotalWin() + battleStatistics.getTotalNotWon() : RoundEvent.TOTAL_NOT_MEANINGFUL,
            subsequentNotIdle);
    }
}
