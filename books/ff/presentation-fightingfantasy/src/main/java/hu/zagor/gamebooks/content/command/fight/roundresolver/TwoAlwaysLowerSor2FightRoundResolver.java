package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are two enemies, and if at least one of them has a lower Attack Strength than us, we hit it no matter what.
 * @author Tamas_Szekeres
 */
@Component("twoAlwaysLowersor2FightRoundResolver")
public class TwoAlwaysLowerSor2FightRoundResolver extends SingleFightRoundResolver {
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        FightRoundResult[] resolveRound;

        final List<FfEnemy> enemies = command.getResolvedEnemies();
        if (enemies.size() == 1) {
            resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);
        } else {
            resolveRound = resolveRoundWithTwoEnemies(command, resolvationData, enemies);
        }
        return resolveRound;
    }

    private FightRoundResult[] resolveRoundWithTwoEnemies(final FightCommand command, final ResolvationData resolvationData, final List<FfEnemy> enemies) {
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
        final Map<String, int[]> enemiesAttackStrengthValues = getEnemiesAttackValues(enemies, command);
        final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
        final Map<String, Integer> enemiesAttackStrength = getEnemiesAttackStrengths(enemies, enemiesAttackStrengthValues);
        recordAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        recordAttachStrength(messages, enemies, enemiesAttackStrengthValues, enemiesAttackStrength);

        final String selectedEnemyId = getSelectedEnemyId(resolvationData);
        final FfEnemy selectedEnemy = getSelectedEnemy(enemies, selectedEnemyId);
        final FfEnemy otherEnemy = getOtherEnemy(enemies, selectedEnemyId);

        if (canHitEnemy(selectedEnemy, selfAttackStrength, enemiesAttackStrength)) {
            final FightDataDto dto = new FightDataDto(selectedEnemy, messages, resolvationData, null);
            damageEnemy(command, dto);
            reportEnemy(selfAttackStrength, enemiesAttackStrength.get(otherEnemy.getId()), new FightDataDto(otherEnemy, messages, resolvationData, null), command);
            result[0] = FightRoundResult.WIN;
            result[1] = FightRoundResult.LOSE;
        } else {
            result[0] = FightRoundResult.LOSE;
            final FightDataDto dto = new FightDataDto(otherEnemy, messages, resolvationData, null);
            reportEnemy(selfAttackStrength, enemiesAttackStrength.get(selectedEnemy.getId()), new FightDataDto(selectedEnemy, messages, resolvationData, null), command);
            if (canHitEnemy(otherEnemy, selfAttackStrength, enemiesAttackStrength)) {
                result[1] = FightRoundResult.WIN;
                damageEnemy(command, dto);
            } else {
                result[1] = FightRoundResult.LOSE;
                reportEnemy(selfAttackStrength, enemiesAttackStrength.get(otherEnemy.getId()), new FightDataDto(otherEnemy, messages, resolvationData, null), command);
            }
        }

        return result;
    }

    private void reportEnemy(final int selfAttackStrength, final int enemyAttackStrength, final FightDataDto dto, final FightCommand command) {
        final FfEnemy enemy = dto.getEnemy();
        final FightCommandMessageList messages = dto.getMessages();
        if (selfAttackStrength >= enemyAttackStrength) {
            messages.addKey("page.ff.label.fight.single.tied", new Object[]{enemy.getCommonName()});
        } else if (selfAttackStrength < enemyAttackStrength) {
            damageSelf(dto);
            handleDefeatLuckTest(command, dto);
        }
    }

    private boolean canHitEnemy(final FfEnemy enemy, final int selfAttackStrength, final Map<String, Integer> enemiesAttackStrength) {
        final int enemyAttackStrength = enemiesAttackStrength.get(enemy.getId());
        return selfAttackStrength > enemyAttackStrength;
    }

    private FfEnemy getOtherEnemy(final List<FfEnemy> enemies, final String selectedEnemyId) {
        FfEnemy selectedEnemy = null;
        for (final FfEnemy enemy : enemies) {
            if (!selectedEnemyId.equals(enemy.getId())) {
                selectedEnemy = enemy;
            }
        }
        return selectedEnemy;
    }

    private FfEnemy getSelectedEnemy(final List<FfEnemy> enemies, final String selectedEnemyId) {
        FfEnemy selectedEnemy = null;
        for (final FfEnemy enemy : enemies) {
            if (selectedEnemyId.equals(enemy.getId())) {
                selectedEnemy = enemy;
            }
        }
        return selectedEnemy;
    }

    private String getSelectedEnemyId(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        return interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
    }

    private Map<String, int[]> getEnemiesAttackValues(final List<FfEnemy> enemies, final FightCommand command) {
        final Map<String, int[]> enemyAttackValues = new HashMap<>();
        for (final FfEnemy enemy : enemies) {
            enemyAttackValues.put(enemy.getId(), getEnemyAttackStrength(enemy, command));
        }
        return enemyAttackValues;
    }

    private Map<String, Integer> getEnemiesAttackStrengths(final List<FfEnemy> enemies, final Map<String, int[]> enemiesAttackStrengthValues) {
        final Map<String, Integer> enemyAttackStrengths = new HashMap<>();
        for (final FfEnemy enemy : enemies) {
            enemyAttackStrengths.put(enemy.getId(), enemy.getSkill() + enemiesAttackStrengthValues.get(enemy.getId())[0]);
        }
        return enemyAttackStrengths;
    }

    private void recordAttachStrength(final FightCommandMessageList messages, final List<FfEnemy> enemies, final Map<String, int[]> enemiesAttackStrengthValues,
        final Map<String, Integer> enemiesAttackStrength) {
        for (final FfEnemy enemy : enemies) {
            final int[] enemyAttackStrengthValues = enemiesAttackStrengthValues.get(enemy.getId());
            final int enemyAttackStrength = enemiesAttackStrength.get(enemy.getId());
            recordAttachStrength(new FightDataDto(enemy, messages, null, null), enemyAttackStrengthValues, enemyAttackStrength);
        }
    }

}
