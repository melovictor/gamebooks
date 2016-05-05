package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.List;

/**
 * Custom basic fight command sub resolver for FF60.
 * @author Tamas_Szekeres
 */
public class Ff60FightCommandBasicSubResolver extends FightCommandBasicSubResolver {
    private static final int JAGUAR_BATTLE_OVER = 3;

    @Override
    protected void handleAttacking(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        super.handleAttacking(command, resolvationData, resolveList);

        final FfEnemy enemy = getEnemy(resolvationData);

        if (isJaguar(enemy)) {
            handleJaguar(command, resolvationData, resolveList);
        }
    }

    private void handleJaguar(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final AttributeHandler attributeHandler = resolvationData.getCharacterHandler().getAttributeHandler();
        final int stamina = attributeHandler.resolveValue(character, "stamina");
        if (stamina <= JAGUAR_BATTLE_OVER) {
            resolveList.add(command.getLose());
            command.setOngoing(false);
        }
    }

    private boolean isJaguar(final FfEnemy enemy) {
        return "62".equals(enemy.getId());
    }

    private FfEnemy getEnemy(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getInfo().getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        return (FfEnemy) resolvationData.getEnemies().get(enemyId);
    }
}
