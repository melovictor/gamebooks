package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.logging.LogInject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Abstract class containing some useful actions for resolving rounds.
 * @author Tamas_Szekeres
 */
public abstract class AbstractFightRoundResolver extends TextResolvingFightRoundResolver {

    @LogInject
    private Logger logger;
    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;

    /**
     * Records the attack strength for the hero.
     * @param messages the list of messages that will be displayed on screen
     * @param selfAttackStrengthValues the thrown values for the hero
     * @param selfAttackStrength the combined attack strength for the hero
     * @param character the {@link FfCharacter}
     */
    protected abstract void recordAttachStrength(final FightCommandMessageList messages, final int[] selfAttackStrengthValues, final int selfAttackStrength,
        final FfCharacter character);

    /**
     * Records the attack strength of a specific enemy.
     * @param dto the {@link FightDataDto} object containing all required beans
     * @param enemyAttackStrengthValues the thrown values for this specific enemy
     * @param enemyAttackStrength the total attack strength for this specific enemy
     */
    protected void recordAttachStrength(final FightDataDto dto, final int[] enemyAttackStrengthValues, final int enemyAttackStrength) {
        final FightCommandMessageList messages = dto.getMessages();
        final FfEnemy enemy = dto.getEnemy();

        messages.addKey("page.ff.label.fight.single.attackStrength.enemy", new Object[]{enemy.getName(), enemyAttackStrengthValues[1], enemyAttackStrengthValues[2],
            enemyAttackStrength});
        getLogger().debug("Attack strength for {}: {}", enemy.getName(), enemyAttackStrength);
    }

    /**
     * Makes the hero suffer the damage caused by a single opponent.
     * @param dto the {@link FightDataDto} object containing all required beans
     */
    protected void damageSelf(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfEnemy enemy = dto.getEnemy();

        character.changeStamina(-enemy.getStaminaDamage());
        character.changeSkill(-enemy.getSkillDamage());
        resolveDefeatMessage(dto);
    }

    /**
     * Resolves the message to display when the hero is defeated by the enemy.
     * @param dto the {@link FightDataDto} object
     */
    protected void resolveDefeatMessage(final FightDataDto dto) {
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.fight.single.failedDefense", new Object[]{dto.getEnemy().getName()});
    }

    /**
     * Checks whether the weapon the hero is using is effective against the current enemy.
     * @param dto the {@link FightDataDto} object containing all required beans
     * @return true if the weapon is effective, false otherwise
     */
    protected boolean isWeaponEffective(final FightDataDto dto) {
        boolean effective = true;
        final FfEnemy enemy = dto.getEnemy();
        if (!enemy.isKillableByNormal()) {
            final FfItem selectedWeapon = dto.getSelectedWeapon();
            final boolean damagableByMagic = enemy.isKillableByMagical() && selectedWeapon.isMagical();
            final boolean damagableByBlessed = enemy.isKillableByBlessed() && selectedWeapon.isBlessed();
            effective = damagableByMagic || damagableByBlessed;
        }
        return effective;
    }

    /**
     * Executes the luck test when the enemy hit the hero.
     * @param command the {@link FightCommand}
     * @param dto the {@link FightDataDto} object containing all required beans
     */
    protected void handleDefeatLuckTest(final FightCommand command, final FightDataDto dto) {
        final FfEnemy enemy = dto.getEnemy();
        final FightCommandMessageList messages = dto.getMessages();

        if (command.isLuckOnDefense()) {
            final FfCharacter character = dto.getCharacter();
            final FfCharacterHandler characterHandler = dto.getCharacterHandler();
            final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
            final BattleLuckTestParameters battleLuckTestParameters = characterHandler.getBattleLuckTestParameters();
            final int[] luckTestDices = generator.getRandomNumber(2);
            final int luckTestTotal = luckTestDices[0];

            messages.addKey("page.ff.label.fight.luck.roll", new Object[]{luckTestDices[1], luckTestDices[2], luckTestTotal});
            final int luck = attributeHandler.resolveValue(character, "luck");
            int staminaMod;
            if (luckTestTotal <= luck) {
                getLogger().debug("Successful luck test ({}) while receiving damage.", luckTestTotal);
                staminaMod = battleLuckTestParameters.getLuckyDefenseDeduction();
                messages.addKey("page.ff.label.fight.luck.defense.success", new Object[]{enemy.getCommonName()});
            } else {
                getLogger().debug("Unsuccessful luck test ({}) while receiving damage.", luckTestTotal);
                staminaMod = -battleLuckTestParameters.getUnluckyDefenseAddition();
                messages.addKey("page.ff.label.fight.luck.defense.failure", new Object[]{enemy.getCommonName()});
            }
            character.changeStamina(staminaMod);
            character.changeLuck(-1);
        }
    }

    /**
     * Method handling the stamina decrease and message generation that occurs when fleeing from a battle.
     * @param dto the {@link FightDataDto} object containing all required beans
     */
    protected void fleeFromEnemy(final FightDataDto dto) {
        final FfCharacter character = dto.getCharacter();
        final FfEnemy enemy = dto.getEnemy();
        final FightCommandMessageList messages = dto.getMessages();
        messages.addKey("page.ff.label.fight.single.flee", new Object[]{enemy.getCommonName()});
        character.changeStamina(-enemy.getStaminaDamage());
    }

    public Logger getLogger() {
        return logger;
    }

    public RandomNumberGenerator getGenerator() {
        return generator;
    }

    /**
     * Rolls the attack strength for the enemies, taking into account if a specific enemy always needs to have a given attack strength.
     * @param enemy the enemy for which to roll attack strength
     * @return the rolled values in the usual format
     */
    protected int[] getEnemyAttackStrength(final FfEnemy enemy) {
        int[] result;

        if (enemy.getAttackStrength() > 0) {
            final int firstRoll = getGenerator().getRandomNumber(1)[0];
            result = new int[]{enemy.getAttackStrength() - enemy.getSkill(), firstRoll, enemy.getAttackStrength() - enemy.getSkill() - firstRoll};
        } else {
            result = getGenerator().getRandomNumber(2);
        }

        return result;
    }

}
