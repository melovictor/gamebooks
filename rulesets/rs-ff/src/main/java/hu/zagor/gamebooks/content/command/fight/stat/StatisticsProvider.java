package hu.zagor.gamebooks.content.command.fight.stat;

import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.EventStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

/**
 * Interface for providing statistics results for a specific battle round outcome.
 * @author Tamas_Szekeres
 */
public interface StatisticsProvider {

    /**
     * Provides the statistics either from the {@link BattleStatistics} or from the {@link RoundEvent} object.
     * @param battleStatistics the {@link BattleStatistics} object
     * @param roundEvent the {@link RoundEvent} object
     * @return the specific statistic
     */
    EventStatistics provideStatistics(BattleStatistics battleStatistics, RoundEvent roundEvent);

}
