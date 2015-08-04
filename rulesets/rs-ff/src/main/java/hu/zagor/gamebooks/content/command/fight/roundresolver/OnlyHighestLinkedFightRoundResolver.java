package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there is either a single enemy, multiple enemies that must be handled as a single opponent or multiple enemies that must be fought one by
 * one.
 * @author Tamas_Szekeres
 */
@Component("onlyHighestLinkedFightRoundResolver")
public class OnlyHighestLinkedFightRoundResolver extends AbstractFightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> enemies = command.getResolvedEnemies();
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfEnemy referenceEnemy = enemies.get(0);
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final FightDataDto dto = new FightDataDto(referenceEnemy, messages, resolvationData, command.getUsableWeaponTypes());

        final int[] selfAttackStrengthValues = getGenerator().getRandomNumber(2, attributeHandler.resolveValue(character, "attackStrength"));
        final Map<String, int[]> enemiesAttackStrengthValues = getEnemiesAttackValues(enemies);
        final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
        final Map<String, Integer> enemiesAttackStrength = getEnemiesAttackStrengths(enemies, enemiesAttackStrengthValues);
        recordAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        recordAttachStrength(messages, enemies, enemiesAttackStrengthValues, enemiesAttackStrength);
        final FightRoundResult roundResult = calculateBattleResult(selfAttackStrength, enemiesAttackStrength, result);

        if (roundResult == FightRoundResult.TIE) {
            messages.addKey("page.ff.label.fight.single.tied", new Object[]{referenceEnemy.getCommonName()});
        } else if (roundResult == FightRoundResult.LOSE) {
            damageSelf(dto);
            handleDefeatLuckTest(command, dto);
        } else {
            if (isWeaponEffective(dto)) {
                damageEnemies(enemies, dto);
                handleVictoryLuckTest(command, dto, enemies);
            } else {
                messages.addKey("page.ff.label.fight.single.successfulAttack.ineffectual", new Object[]{referenceEnemy.getCommonName()});
            }
        }

        return result;
    }

    private Map<String, int[]> getEnemiesAttackValues(final List<FfEnemy> enemies) {
        final Map<String, int[]> enemyAttackValues = new HashMap<>();
        for (final FfEnemy enemy : enemies) {
            enemyAttackValues.put(enemy.getId(), getEnemyAttackStrength(enemy));
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

    private void damageEnemies(final List<FfEnemy> enemies, final FightDataDto dto) {
        final FfItem selectedWeapon = dto.getSelectedWeapon();
        final FightCommandMessageList messages = dto.getMessages();

        int weaponStaminaDamage = selectedWeapon.getStaminaDamage();
        final FfEnemy referenceEnemy = enemies.get(0);
        weaponStaminaDamage -= referenceEnemy.getDamageAbsorption();
        if (selectedWeapon.getSubType() == WeaponSubType.edged || selectedWeapon.getSubType() == WeaponSubType.weakBlunt) {
            weaponStaminaDamage -= referenceEnemy.getDamageAbsorptionEdged();
        }
        weaponStaminaDamage = Math.max(weaponStaminaDamage, 0);
        for (final FfEnemy enemy : enemies) {
            enemy.setStamina(enemy.getStamina() - weaponStaminaDamage);
            enemy.setSkill(enemy.getSkill() - selectedWeapon.getSkillDamage());
        }
        messages.addKey("page.ff.label.fight.single.successfulAttack", new Object[]{referenceEnemy.getCommonName()});
    }

    private void handleVictoryLuckTest(final FightCommand command, final FightDataDto dto, final List<FfEnemy> enemies) {
        if (command.isLuckOnHit()) {
            final FightCommandMessageList messages = dto.getMessages();

            final FfCharacter character = dto.getCharacter();
            final FfCharacterHandler characterHandler = dto.getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            final BattleLuckTestParameters battleLuckTestParameters = characterHandler.getBattleLuckTestParameters();
            final int[] luckTestDices = getGenerator().getRandomNumber(2);
            final int luckTestTotal = luckTestDices[0];

            messages.addKey("page.ff.label.fight.luck.roll", new Object[]{luckTestDices[1], luckTestDices[2], luckTestTotal});
            final int luck = attributeHandler.resolveValue(character, "luck");
            int staminaMod;
            final FfEnemy referenceEnemy = enemies.get(0);
            if (luckTestTotal <= luck) {
                getLogger().debug("Successful luck test ({}) while dealing damage.", luckTestTotal);
                staminaMod = -battleLuckTestParameters.getLuckyAttackAddition();
                messages.addKey("page.ff.label.fight.luck.attack.success", new Object[]{referenceEnemy.getCommonName()});
            } else {
                getLogger().debug("Unsuccessful luck test ({}) while dealing damage.", luckTestTotal);
                staminaMod = battleLuckTestParameters.getUnluckyAttackDeduction();
                messages.addKey("page.ff.label.fight.luck.attack.failure", new Object[]{referenceEnemy.getCommonName()});
            }
            for (final FfEnemy enemy : enemies) {
                enemy.setStamina(enemy.getStamina() + staminaMod);
            }
            character.changeLuck(-1);
        }
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        getFleeTextResourceList(messages);
        for (final FfEnemy enemy : command.getResolvedEnemies()) {
            fleeFromEnemy(new FightDataDto(enemy, messages, resolvationData, null));
        }
    }

    @Override
    protected void recordAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        messages.addKey("page.ff.label.fight.single.attackStrength.self", new Object[]{selfAttackStrengthValues[1], selfAttackStrengthValues[2], selfAttackStrength});
        getLogger().debug("Attack strength for self: {}", selfAttackStrength);
    }

    @Override
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.fight.single.failedDefense", new Object[]{dto.getEnemy().getCommonName()});
    }

}
