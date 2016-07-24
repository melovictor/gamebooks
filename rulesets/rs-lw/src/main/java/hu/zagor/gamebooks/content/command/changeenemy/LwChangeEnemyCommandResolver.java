package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 */
public class LwChangeEnemyCommandResolver extends AbstractChangeEnemyCommandResolver<LwEnemy> {

    @Override
    String getEnemyId(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        return command.getId();
    }

}
