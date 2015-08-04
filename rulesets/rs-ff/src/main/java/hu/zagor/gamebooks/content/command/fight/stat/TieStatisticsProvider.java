package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.springframework.stereotype.Component;

/**
 * Provides tie statistics.
 * @author Tamas_Szekeres
 */
@Component("tieStatisticsProvider")
public class TieStatisticsProvider implements StatisticsProvider {

    @Override
    public EventStatistics provideStatistics(final BattleStatistics battleStatistics, final RoundEvent roundEvent) {
        final int subsequentTie = battleStatistics.getSubsequentTie();
        return new EventStatistics(subsequentTie > 0 ? battleStatistics.getTotalTie() : RoundEvent.TOTAL_NOT_MEANINGFUL, subsequentTie);
    }
}
