package hu.zagor.gamebooks.lw.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.changeenemy.AbstractChangeEnemyCommandResolver;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 */
public class LwChangeEnemyCommandResolver extends AbstractChangeEnemyCommandResolver<LwEnemy> {

    @Override
    protected String getEnemyId(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        return command.getId();
    }

}
