package hu.zagor.gamebooks.content.command.changeenemy;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Main bean for resolving an enemy altering command.
 * @author Tamas_Szekeres
 */
public class ChangeEnemyCommandResolver extends AbstractChangeEnemyCommandResolver<FfEnemy> {

    @Override
    String getEnemyId(final ChangeEnemyCommand command, final ResolvationData resolvationData) {
        String id = command.getId();

        if ("activeEnemy".equals(id)) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
            id = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        }
        return id;
    }

}
