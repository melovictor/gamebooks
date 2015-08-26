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

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there is either a single enemy, multiple enemies that must be handled as a single opponent or multiple enemies that must be fought one by
 * one.
 * @author Tamas_Szekeres
 */
@Component("singleFightRoundResolver")
public class SingleFightRoundResolver extends AbstractFightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> enemies = command.getResolvedEnemies();
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final String enemyId = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        for (int enemyIdx = 0; enemyIdx < enemies.size(); enemyIdx++) {
            final FfEnemy enemy = enemies.get(enemyIdx);
            final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData, command.getUsableWeaponTypes());
            if (enemy.getId().equals(enemyId)) {
                if (beforeRoundResult.isLoseBattle()) {
                    doLoseFight(command, result, enemyIdx, dto);
                } else {
                    final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
                    final int[] enemyAttackStrengthValues = getEnemyAttackStrength(enemy, command);
                    final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
                    final int enemyAttackStrength = enemy.getSkill() + enemyAttackStrengthValues[0];
                    command.getAttackStrengths().put(enemy.getId(), enemyAttackStrength);
                    recordAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
                    recordAttachStrength(dto, enemyAttackStrengthValues, enemyAttackStrength);
                    if (enemyAttackStrength == selfAttackStrength) {
                        doTieFight(result, enemyIdx, dto);
                    } else if (enemyAttackStrength > selfAttackStrength) {
                        doLoseFight(command, result, enemyIdx, dto);
                    } else {
                        doWinFight(command, result, enemyIdx, dto);
                    }
                }
            } else {
                result[enemyIdx] = FightRoundResult.IDLE;
                inactiveDamageSelf(dto);
            }
            autoDamageSelf(dto);
        }
        return result;
    }

    private void doWinFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        result[enemyIdx] = FightRoundResult.WIN;
        damageEnemy(command, dto);
    }

    private void doTieFight(final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        result[enemyIdx] = FightRoundResult.TIE;
        resolveTieMessage(dto);
    }

    private void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        result[enemyIdx] = FightRoundResult.LOSE;
        if (noStoneSkinAvailable(dto)) {
            damageSelf(dto);
            handleDefeatLuckTest(command, dto);
        }
    }

    private boolean noStoneSkinAvailable(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final int stoneSkin = character.getStoneSkin();
        final boolean hasStoneSkin = stoneSkin > 0;
        if (hasStoneSkin) {
            character.setStoneSkin(stoneSkin - 1);
            final FightCommandMessageList messages = dto.getMessages();
            messages.addKey("page.ff.label.fight.single.failedDefense.noEffect", new Object[]{dto.getEnemy().getCommonName()});
        }
        return !hasStoneSkin;
    }

    /**
     * Resolves the message to display when the hero and enemy ties.
     * @param dto the {@link FightDataDto} object
     */
    protected void resolveTieMessage(final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.fight.single.tied", new Object[]{enemy.getName()});
    }

    /**
     * Method to handle the enemy receiving a successful hit from the hero.
     * @param command the {@link FightCommand} object
     * @param dto the {@link FightDataDto} object
     */
    protected void damageEnemy(final FightCommand command, final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        final FightCommandMessageList messages = dto.getMessages();
        if (isWeaponEffective(dto)) {
            damageEnemy(dto);
            handleVictoryLuckTest(command, dto);
        } else {
            messages.addKey("page.ff.label.fight.single.successfulAttack.ineffectual", new Object[]{enemy.getName()});
        }
    }

    /**
     * Method to calculate automatic damages suffered by the hero from the hands of the enemies.
     * @param dto the {@link FightDataDto} object
     */
    protected void autoDamageSelf(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfEnemy enemy = dto.getEnemy();

        if (enemy.getStamina() > 0 && enemy.getStaminaAutoDamage() > 0) {
            character.changeStamina(-enemy.getStaminaAutoDamage());
            resolveDefeatMessage(dto);
        }
    }

    private void inactiveDamageSelf(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfEnemy enemy = dto.getEnemy();

        final int inactiveDamage = enemy.getStaminaDamageWhileInactive();
        if (inactiveDamage > 0) {
            character.changeStamina(-inactiveDamage);
            resolveDefeatMessage(dto);
        }
    }

    private void damageEnemy(final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        final FfItem selectedWeapon = dto.getSelectedWeapon();

        int weaponStaminaDamage = selectedWeapon.getStaminaDamage();
        weaponStaminaDamage -= enemy.getDamageAbsorption();
        final WeaponSubType subType = selectedWeapon.getSubType();
        if (subType == WeaponSubType.edged || subType == WeaponSubType.weakBlunt) {
            weaponStaminaDamage -= enemy.getDamageAbsorptionEdged();
        }
        weaponStaminaDamage = Math.max(weaponStaminaDamage, 0);
        enemy.setStamina(enemy.getStamina() - weaponStaminaDamage);
        enemy.setSkill(enemy.getSkill() - selectedWeapon.getSkillDamage());

        if (enemy.getStaminaDamageWhenHit() > 0) {
            final FfCharacter character = dto.getCharacter();
            character.changeStamina(-enemy.getStaminaDamageWhenHit());
        }

        resolveVictoryMessage(dto);
    }

    /**
     * Resolves the message to display when the hero defeats the enemy.
     * @param dto the {@link FightDataDto} object
     */
    protected void resolveVictoryMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();
        String key = "page.ff.label.fight.single.successfulAttack";
        if (enemy.getStaminaDamageWhenHit() > 0) {
            key += ".returnDamage";
        }
        messages.addKey(key, new Object[]{enemy.getName()});
    }

    /**
     * Executes the luck test when the hero hit the enemy.
     * @param command the {@link FightCommand}
     * @param dto the {@link FightDataDto} object containing all required beans
     */

    protected void handleVictoryLuckTest(final FightCommand command, final FightDataDto dto) {
        if (command.isLuckOnHit()) {
            final FfEnemy enemy = dto.getEnemy();
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
            if (luckTestTotal <= luck) {
                getLogger().debug("Successful luck test ({}) while dealing damage.", luckTestTotal);
                staminaMod = -battleLuckTestParameters.getLuckyAttackAddition();
                messages.addKey("page.ff.label.fight.luck.attack.success", enemy.getName());
            } else {
                getLogger().debug("Unsuccessful luck test ({}) while dealing damage.", luckTestTotal);
                staminaMod = battleLuckTestParameters.getUnluckyAttackDeduction();
                messages.addKey("page.ff.label.fight.luck.attack.failure", enemy.getName());
            }
            enemy.setStamina(enemy.getStamina() + staminaMod);
            character.changeLuck(-1);
        }
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        final FightCommandMessageList messages = command.getMessages();
        getFleeTextResourceList(messages);
        fleeFromEnemy(new FightDataDto(enemy, messages, resolvationData, null));
    }

    @Override
    protected void recordAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
        messages.addKey("page.ff.label.fight.single.attackStrength.self", new Object[]{renderedDice, selfAttackStrength});
        getLogger().debug("Attack strength for self: {}", selfAttackStrength);
    }

}
