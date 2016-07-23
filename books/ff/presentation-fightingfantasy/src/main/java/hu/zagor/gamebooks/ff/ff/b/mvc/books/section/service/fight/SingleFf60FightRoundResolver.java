package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF60.
 * @author Tamas_Szekeres
 */
@Component("singleff60FightRoundResolver")
public class SingleFf60FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<EnemyPrePostFightDataContainer> {
    @Autowired @Qualifier("singleFightRoundResolver") private FfFightRoundResolver decorated;

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff60BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected EnemyPrePostFightDataContainer getDataBean() {
        return new EnemyPrePostFightDataContainer();
    }
}
