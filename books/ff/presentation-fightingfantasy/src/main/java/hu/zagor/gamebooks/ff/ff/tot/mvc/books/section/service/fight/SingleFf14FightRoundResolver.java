package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Custom single fight round handler for FF14.
 * @author Tamas_Szekeres
 */
@Component("singleff14FightRoundResolver")
public class SingleFf14FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<EnemyPrePostFightDataContainer> {
    @Autowired @Qualifier("singleFightRoundResolver") private FfFightRoundResolver decorated;

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff14BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected EnemyPrePostFightDataContainer getDataBean() {
        return new EnemyPrePostFightDataContainer();
    }

}
