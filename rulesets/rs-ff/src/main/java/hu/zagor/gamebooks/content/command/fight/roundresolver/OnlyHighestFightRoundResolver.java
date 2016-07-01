package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are multiple enemies. Only the party with the highest attack strength will score a blow.
 * @author Tamas_Szekeres
 */
@Component("onlyHighestFightRoundResolver")
public class OnlyHighestFightRoundResolver extends SingleFightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> enemies = command.getResolvedEnemies();
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfEnemy targetEnemy = getEnemy(enemies, getSelectedEnemyId(resolvationData));
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
        final Map<String, int[]> enemiesAttackStrengthValues = getEnemiesAttackValues(enemies, command);
        final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
        final Map<String, Integer> enemiesAttackStrength = getEnemiesAttackStrengths(enemies, enemiesAttackStrengthValues);
        recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        recordAttachStrength(messages, enemies, enemiesAttackStrengthValues, enemiesAttackStrength);
        final FightRoundResult roundResult = calculateBattleResult(selfAttackStrength, enemiesAttackStrength, result);

        if (roundResult == FightRoundResult.TIE) {
            messages.addKey("page.ff.label.fight.onlyHighest.tied");
        } else if (roundResult == FightRoundResult.LOSE) {
            final FfEnemy winningEnemy = getWinningEnemy(enemies, enemiesAttackStrength);
            final FightDataDto dto = new FightDataDto(winningEnemy, messages, resolvationData, command.getUsableWeaponTypes());
            damageSelf(dto);
            handleDefeatLuckTest(command, dto);
        } else {
            final FightDataDto dto = new FightDataDto(targetEnemy, messages, resolvationData, command.getUsableWeaponTypes());
            damageEnemy(command, dto);
        }

        return result;
    }

    private FfEnemy getWinningEnemy(final List<FfEnemy> enemies, final Map<String, Integer> enemiesAttackStrength) {
        String highestAttackStrengthId = null;
        int highestAttackStrength = 0;

        for (final Entry<String, Integer> entry : enemiesAttackStrength.entrySet()) {
            if (highestAttackStrength < entry.getValue()) {
                highestAttackStrength = entry.getValue();
                highestAttackStrengthId = entry.getKey();
            }
        }

        return getEnemy(enemies, highestAttackStrengthId);
    }

    private FfEnemy getEnemy(final List<FfEnemy> enemies, final String enemyId) {
        FfEnemy selected = null;

        for (final FfEnemy enemy : enemies) {
            if (enemyId.equals(enemy.getId())) {
                selected = enemy;
            }
        }

        return selected;
    }

    private String getSelectedEnemyId(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.getLastFightCommand((FfCharacter) resolvationData.getCharacter(), LastFightCommand.ENEMY_ID);
        return enemyId;
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
            recordEnemyAttachStrength(new FightDataDto(enemy, messages, null, null), enemyAttackStrengthValues, enemyAttackStrength);
        }
    }

    private FightRoundResult calculateBattleResult(final int selfAttackStrength, final Map<String, Integer> enemiesAttackStrength, final FightRoundResult[] result) {
        FightRoundResult roundResult = FightRoundResult.WIN;

        for (final Entry<String, Integer> entry : enemiesAttackStrength.entrySet()) {
            if (selfAttackStrength == entry.getValue() && roundResult == FightRoundResult.WIN) {
                roundResult = FightRoundResult.TIE;
            } else if (selfAttackStrength < entry.getValue()) {
                roundResult = FightRoundResult.LOSE;
            }
        }

        for (int i = 0; i < result.length; i++) {
            result[i] = roundResult;
        }

        return roundResult;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        final FightFleeData fleeData = command.getFleeData();
        getFleeTextResourceList(messages, fleeData);
        if (fleeData == null || fleeData.isSufferDamage()) {
            for (final FfEnemy enemy : command.getResolvedEnemies()) {
                fleeFromEnemy(new FightDataDto(enemy, messages, resolvationData, null));
            }
        }
    }

    @Override
    protected void recordHeroAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
        messages.addKey("page.ff.label.fight.single.attackStrength.self", new Object[]{renderedDice, selfAttackStrength});
        getLogger().debug("Attack strength for self: {}", selfAttackStrength);
    }

    @Override
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.fight.single.failedDefense", new Object[]{dto.getEnemy().getCommonName()});
    }

}
