package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicAbstractCustomEnemyHandlingFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.CustomBeforeAfterRoundEnemyHandler;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF11.
 * @author Tamas_Szekeres
 */
@Component("singleff11FightRoundResolver")
public class SingleFf11FightRoundResolver extends BasicAbstractCustomEnemyHandlingFightRoundResolver<BasicEnemyPrePostFightDataContainer> {
    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver decorated;

    @Override
    protected Class<? extends CustomBeforeAfterRoundEnemyHandler<BasicEnemyPrePostFightDataContainer>> getType() {
        return Ff11BeforeAfterRoundEnemyHandler.class;
    }

    @Override
    protected BasicEnemyPrePostFightDataContainer getDataBean() {
        return new BasicEnemyPrePostFightDataContainer();
    }
}
