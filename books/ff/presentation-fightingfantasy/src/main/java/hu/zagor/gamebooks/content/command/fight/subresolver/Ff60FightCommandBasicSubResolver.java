package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

/**
 * Custom basic fight command sub resolver for FF60.
 * @author Tamas_Szekeres
 */
public class Ff60FightCommandBasicSubResolver extends FightCommandBasicSubResolver {
    private static final int MASK_POWER = 3;
    private static final String MASKED_ZOMBIE_SPECIAL_ATTACK = "4012";
    private static final int JAGUAR_BATTLE_OVER = 3;
    @Resource(name = "ff60AttackEffectivenessVerification") private Map<String, Set<String>> effectivenessVerification;

    @Override
    protected void handleAttacking(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        if (maskedZombieSpecialAttack(resolvationData)) {
            prepareCharacterSpecialAttack(resolvationData);
        }

        super.handleAttacking(command, resolvationData, resolveList);

        final FfEnemy enemy = getEnemy(resolvationData);

        if (maskedZombieSpecialAttack(resolvationData)) {
            final FightCommandMessageList messages = command.getMessages();
            revertCharacterSpecialAttack(resolvationData);
            if (maskNotTearedDownYet(enemy, resolvationData)) {
                if (wonLastRound(enemy, command)) {
                    markMaskTearDown(enemy, resolvationData);
                    weakenEnemy(enemy);
                    messages.addKey("page.ff60.fight.maskedZombies.maskCameDown", enemy.getName());
                    enemy.setName(messages.resolveKey("page.ff60.fight.enemy" + enemy.getId() + ".masklessName"));
                } else {
                    messages.addKey("page.ff60.fight.maskedZombies.maskStayed", enemy.getName());
                }
            } else {
                messages.addKey("page.ff60.fight.maskedZombies.maskAlreadyDown", enemy.getName());
            }
        }

        if (isJaguar(enemy)) {
            handleJaguar(command, resolvationData, resolveList);
        }
    }

    private void markMaskTearDown(final FfEnemy enemy, final ResolvationData resolvationData) {
        resolvationData.getCharacterHandler().getInteractionHandler().setInteractionState(resolvationData.getCharacter(), "mz" + enemy.getId(), "true");
    }

    private void weakenEnemy(final FfEnemy enemy) {
        enemy.setSkill(enemy.getSkill() - MASK_POWER);
    }

    private boolean maskNotTearedDownYet(final FfEnemy enemy, final ResolvationData resolvationData) {
        return resolvationData.getCharacterHandler().getInteractionHandler().peekInteractionState(resolvationData.getCharacter(), "mz" + enemy.getId()) == null;
    }

    private boolean wonLastRound(final FfEnemy enemy, final FightCommand command) {
        return command.getBattleStatistics(enemy.getId()).getSubsequentWin() > 0;
    }

    private void revertCharacterSpecialAttack(final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Character character = resolvationData.getCharacter();
        itemHandler.removeItem(character, "4001", 2);
    }

    private void prepareCharacterSpecialAttack(final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Character character = resolvationData.getCharacter();
        itemHandler.addItem(character, "4001", 2);
    }

    private boolean maskedZombieSpecialAttack(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfEnemy enemy = getEnemy(resolvationData);

        return "maskTearing".equals(interactionHandler.peekLastFightCommand(character, LastFightCommand.SPECIAL))
            && itemHandler.hasItem(character, MASKED_ZOMBIE_SPECIAL_ATTACK) && effectivenessVerification.get(MASKED_ZOMBIE_SPECIAL_ATTACK).contains(enemy.getId());
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
