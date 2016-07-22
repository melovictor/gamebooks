package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 */
public class ChangeEnemyCommandResolver extends AbstractChangeEnemyCommandResolver<LwEnemy> {

    @Override
    String getEnemyId(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        final String id = command.getId();

        // TODO: implement, if and when necessary
        // if ("activeEnemy".equals(id)) {
        // final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        // final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        // id = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        // }
        return id;
    }

}
