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
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.List;
import java.util.Map;
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
        final String enemyId = characterHandler.getInteractionHandler().peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
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
                    storeHeroAttackStrength(command, enemy, selfAttackStrength, selfAttackStrengthValues);
                    storeEnemyAttackStrength(command, enemy, enemyAttackStrength, enemyAttackStrengthValues);
                    recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
                    recordEnemyAttachStrength(dto, enemyAttackStrengthValues, enemyAttackStrength);
                    if (enemyAttackStrength == selfAttackStrength) {
                        doTieFight(command, result, enemyIdx, dto);
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

    void storeHeroAttackStrength(final FightCommand command, final FfEnemy enemy, final int selfAttackStrength, final int[] selfAttackStrengthValues) {
        command.getAttackStrengths().put("h_" + enemy.getId(), selfAttackStrength);
        for (int i = 1; i < selfAttackStrengthValues.length; i++) {
            command.getAttackStrengths().put("h_d" + i + "_" + enemy.getId(), selfAttackStrengthValues[i]);
        }
    }

    void storeEnemyAttackStrength(final FightCommand command, final FfEnemy enemy, final int enemyAttackStrength, final int[] enemyAttackStrengthValues) {
        final Map<String, Integer> attackStrengths = command.getAttackStrengths();
        attackStrengths.put(enemy.getId(), enemyAttackStrength);
        for (int i = 1; i < enemyAttackStrengthValues.length; i++) {
            attackStrengths.put("d" + i + "_" + enemy.getId(), enemyAttackStrengthValues[i]);
        }
    }

    void doWinFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        result[enemyIdx] = FightRoundResult.WIN;
        damageEnemy(command, dto);
    }

    /**
     * Handles the eventuality where both Attack Strengths are the same, and the hero and the enemy tie.
     * @param command the {@link FightCommand} object
     * @param result the {@link FightRoundResult} object
     * @param enemyIdx the index of the enemy in the result array
     * @param dto the {@link FightDataDto}
     */
    void doTieFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        result[enemyIdx] = FightRoundResult.TIE;
        resolveTieMessage(dto);
    }

    void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
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
        messages.addKey("page.ff.label.fight.single.tied", enemy.getName());
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

        int weaponStaminaDamage = selectedWeapon.getStaminaDamage() + dto.getAttributeHandler().resolveValue(dto.getCharacter(), "baseStaminaDamage");
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
        messages.addKey(key, enemy.getName());
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
        final FightFleeData fleeData = command.getFleeData();
        getFleeTextResourceList(messages, fleeData);
        if (fleeData.isSufferDamage()) {
            fleeFromEnemy(new FightDataDto(enemy, messages, resolvationData, null));
        }
    }

    @Override
    protected void recordHeroAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character) {
        final String renderedDice = getDiceResultRenderer().render(6, selfAttackStrengthValues);
        messages.addKey("page.ff.label.fight.single.attackStrength.self", new Object[]{renderedDice, selfAttackStrength});
        getLogger().debug("Attack strength for self: {}", selfAttackStrength);
    }

}
