package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import java.util.Map;

/**
 * Abstract class for pre-fight handling classes that need to handle items which affect a specific, selected enemy.
 * @author Tamas_Szekeres
 */
public abstract class EnemyDependentFfBookPreFightHandlingService implements FfBookPreFightHandlingService {

    /**
     * Fetches the {@link FfEnemy} object that was selected at the moment the user tried to use the pre-fight item.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link FfBookInformations} object
     * @return the {@link FfEnemy} object
     */
    protected FfEnemy getEnemy(final HttpSessionWrapper wrapper, final FfBookInformations info) {
        final Map<String, Enemy> enemies = wrapper.getEnemies();
        final FfUserInteractionHandler interactionHandler = info.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(wrapper.getCharacter(), LastFightCommand.ENEMY_ID);
        return (FfEnemy) enemies.get(enemyId);
    }
}
