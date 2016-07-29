package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Decorator that adds dynamic before-after fighting capabilities.
 * @author Tamas_Szekeres
 */
@Component
public class BeforeAfterWrappingLwFightRoundResolver implements LwFightRoundResolver {
    @Autowired @Qualifier("defaultLwFightRoundResolver") private LwFightRoundResolver decorated;
    @Autowired private ApplicationContext applicationContext;

    @Override
    public void resolveRound(final LwFightCommand command, final ResolvationData resolvationData) {
        final LwPrePostEnemyFightHandler handler = obtainPrePostHandler(command, resolvationData);
        Object prePostData = null;
        if (handler.shouldExecutePre(command, resolvationData)) {
            prePostData = handler.executePre(command, resolvationData);
        }
        decorated.resolveRound(command, resolvationData);
        if (handler.shouldExecutePost(command, resolvationData)) {
            handler.executePost(command, resolvationData, prePostData);
        }
    }

    private LwPrePostEnemyFightHandler obtainPrePostHandler(final LwFightCommand command, final ResolvationData resolvationData) {
        final String name = getPrePostBeanName(command, resolvationData);
        final LwPrePostEnemyFightHandler handler = getHandler(name);
        return handler;
    }

    private LwPrePostEnemyFightHandler getHandler(final String name) {
        LwPrePostEnemyFightHandler handler = LwPrePostEnemyFightHandler.EMPTY;
        if (applicationContext.containsBean(name)) {
            handler = applicationContext.getBean(name, LwPrePostEnemyFightHandler.class);
        }
        return handler;
    }

    private String getPrePostBeanName(final LwFightCommand command, final ResolvationData resolvationData) {
        final int bookPosition = resolvationData.getInfo().getPosition().intValue();
        final String name = "lw" + bookPosition + "PrePost" + command.getResolvedEnemies().get(0).getId() + "Handler";
        return name;
    }

    @Override
    public void resolveFlee(final LwFightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

}
