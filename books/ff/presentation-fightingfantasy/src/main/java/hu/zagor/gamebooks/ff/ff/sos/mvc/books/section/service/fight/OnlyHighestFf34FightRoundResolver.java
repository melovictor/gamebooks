package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * All at once fight round resolver for FF34.
 * @author Tamas_Szekeres
 */
@Component("onlyHighestff34FightRoundResolver")
public class OnlyHighestFf34FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<EnemyPrePostFightDataContainer> {
    @Autowired @Qualifier("onlyHighestFightRoundResolver") private FfFightRoundResolver decorated;

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer>> getType() {
        return Ff34BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected EnemyPrePostFightDataContainer getDataBean() {
        return new EnemyPrePostFightDataContainer();
    }

}
