package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.ff.ff.sa.enemy.DeityWeapon;
import hu.zagor.gamebooks.ff.ff.sa.enemy.Ff12Enemy;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Class for playing a single round of blaster battle in book FF12.
 * @author Tamas_Szekeres
 */
@Component("shootingff12FightRoundResolver")
public class ShootingFf12FightRoundResolver implements FfFightRoundResolver {
    private static final int DISINTEGRATOR_DAMAGE = 24;
    private static final int STRANGLING_VINE_AUTO_DAMAGE = -2;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;
    @Autowired private LocaleProvider localeProvider;
    @Autowired private MessageSource source;
    @Resource(name = "deityWeapons") private Map<Integer, DeityWeapon> deityWeapons;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] results = new FightRoundResult[command.getResolvedEnemies().size()];
        final FightCommandMessageList messages = command.getMessages();

        if (!mustSkipThisRound(resolvationData)) {
            heroShootsAtTargetedEnemy(resolvationData, messages);
        }
        preFightMachinations(resolvationData, messages);
        enemiesAreShootingAtHero(command, resolvationData);
        enemiesDoingAutoDamage(messages, resolvationData);

        getSelectedEnemy(resolvationData).setActiveWeapon(null);

        return results;
    }

    private boolean mustSkipThisRound(final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        return !itemHandler.removeItem(character, "4002", 1).isEmpty();
    }

    private void enemiesDoingAutoDamage(final FightCommandMessageList messages, final ResolvationData resolvationData) {
        final Ff12Enemy enemy = getSelectedEnemy(resolvationData);
        if ("23".equals(enemy.getId()) && isAlive(enemy)) {
            messages.addKey("page.ff12.fight.noArmourHit", enemy.getCommonName(), 2);
            final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
            character.changeStamina(STRANGLING_VINE_AUTO_DAMAGE);
        }
    }

    private void preFightMachinations(final ResolvationData resolvationData, final FightCommandMessageList messages) {
        final Ff12Enemy selectedEnemy = getSelectedEnemy(resolvationData);
        if ("14".equals(selectedEnemy.getId())) {
            final int weaponId = generator.getRandomNumber(1)[0];
            final DeityWeapon selectedWeapon = deityWeapons.get(weaponId);
            final String weaponName = source.getMessage("page.ff12.fight.deityWeapon." + selectedWeapon.getNameKeyPostfix(), null, localeProvider.getLocale());
            messages.addKey("page.ff12.fight.deityWeaponRoll", weaponId, weaponName);
            selectedEnemy.setSkill(selectedWeapon.getSkill());
            if (selectedWeapon.isVariableDamage()) {
                selectedEnemy.setWeapon("1002");
            } else {
                selectedEnemy.setActiveWeapon(selectedWeapon);
            }
        }
    }

    private void enemiesAreShootingAtHero(final FfFightCommand command, final ResolvationData resolvationData) {
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FightCommandMessageList messages = command.getMessages();

        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        for (int enemyIdx = 0; enemyIdx < resolvedEnemies.size(); enemyIdx++) {
            final Ff12Enemy enemy = (Ff12Enemy) resolvedEnemies.get(enemyIdx);
            int enemyDamage = 0;
            if (isAlive(enemy)) {
                for (int attackNumber = 0; attackNumber < enemy.getAttackPerRound(); attackNumber++) {
                    if (isAlive(character, resolvationData)) {
                        if (enemyHitUs(messages, enemy)) {
                            if (hasArmour(resolvationData) && armourEffective(enemy)) {
                                if (armourDeflectedHit(messages, resolvationData)) {
                                    messages.addKey("page.ff12.fight.armourDeflected", enemy.getCommonName());
                                } else {
                                    enemyDamage = calculateDamage(enemy);
                                    messages.addKey("page.ff12.fight.armourFailed", enemy.getCommonName(), enemyDamage);
                                }
                                reduceArmour(resolvationData);
                            } else {
                                enemyDamage = calculateDamage(enemy);
                                messages.addKey("page.ff12.fight.noArmourHit", enemy.getCommonName(), enemyDamage);
                            }
                        } else {
                            messages.addKey("page.ff12.fight.missedHero", enemy.getCommonName());
                        }
                        character.changeStamina(-enemyDamage);
                    }
                }
            }
        }
    }

    private boolean isAlive(final Ff12Character character, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        return attributeHandler.isAlive(character);
    }

    private boolean armourEffective(final Ff12Enemy enemy) {
        boolean armourEffective = !"1003".equals(enemy.getWeapon());
        final DeityWeapon activeWeapon = enemy.getActiveWeapon();
        if (activeWeapon != null) {
            armourEffective = !activeWeapon.isIgnoreArmour();
        }
        return armourEffective;
    }

    private void heroShootsAtTargetedEnemy(final ResolvationData resolvationData, final FightCommandMessageList messages) {
        final FfEnemy targetEnemy = getSelectedEnemy(resolvationData);
        if (weHitEnemy(messages, resolvationData, targetEnemy)) {
            final int damage = calculateDamage(targetEnemy, resolvationData);
            messages.addKey("page.ff12.fight.hitEnemy", targetEnemy.getCommonName(), damage);
            targetEnemy.setStamina(targetEnemy.getStamina() - damage);
        } else {
            messages.addKey("page.ff12.fight.missedEnemy", targetEnemy.getCommonName());
        }
    }

    private int calculateDamage(final FfEnemy targetEnemy, final ResolvationData resolvationData) {
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FfCharacterItemHandler itemHandler = (FfCharacterItemHandler) resolvationData.getCharacterHandler().getItemHandler();
        final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
        int causedDamage;
        if ("1001".equals(equippedWeapon.getId())) {
            causedDamage = 2;
        } else {
            final int[] randomNumber = generator.getRandomNumber(1);
            causedDamage = randomNumber[0];
        }
        return Math.max(0, causedDamage - targetEnemy.getDamageAbsorption());
    }

    private boolean weHitEnemy(final FightCommandMessageList messages, final ResolvationData resolvationData, final FfEnemy targetEnemy) {
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final int skill = attributeHandler.resolveValue(character, "skill");

        final int[] randomNumber = generator.getRandomNumber(2);
        final String diceRender = renderer.render(generator.getDefaultDiceSide(), randomNumber);
        messages.addKey("page.ff12.fight.targetHero", targetEnemy.getCommonName(), diceRender, randomNumber[0]);

        return randomNumber[0] < skill;
    }

    private boolean enemyHitUs(final FightCommandMessageList messages, final FfEnemy enemy) {
        final int skill = enemy.getSkill();

        final int[] randomNumber = generator.getRandomNumber(2);
        final String diceRender = renderer.render(generator.getDefaultDiceSide(), randomNumber);
        messages.addKey("page.ff12.fight.targetEnemy", enemy.getCommonName(), diceRender, randomNumber[0]);

        return randomNumber[0] < skill;
    }

    private boolean isAlive(final Ff12Enemy enemy) {
        return enemy.getStamina() > 0;
    }

    private int calculateDamage(final Ff12Enemy enemy) {
        int enemyDamage;
        final DeityWeapon activeWeapon = enemy.getActiveWeapon();
        if (activeWeapon != null) {
            enemyDamage = activeWeapon.getDamage();
        } else {
            final String weapon = enemy.getWeapon();
            if ("1001".equals(weapon)) {
                enemyDamage = 2;
            } else if ("1003".equals(weapon)) {
                enemyDamage = DISINTEGRATOR_DAMAGE;
            } else {
                final int[] randomNumber = generator.getRandomNumber(1);
                enemyDamage = randomNumber[0];
            }
        }
        return enemyDamage;
    }

    private Ff12Enemy getSelectedEnemy(final ResolvationData resolvationData) {
        final Map<String, Enemy> enemies = resolvationData.getEnemies();
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        return (Ff12Enemy) enemies.get(enemyId);
    }

    private boolean armourDeflectedHit(final FightCommandMessageList messages, final ResolvationData resolvationData) {
        final int armour = getArmourValue(resolvationData);

        final int[] randomNumber = generator.getRandomNumber(2);
        final String renderedRoll = renderer.render(generator.getDefaultDiceSide(), randomNumber);
        messages.addKey("page.ff12.fight.armourDefense", renderedRoll, randomNumber[0]);

        return randomNumber[0] <= armour;
    }

    private void reduceArmour(final ResolvationData resolvationData) {
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        characterHandler.getAttributeHandler().handleModification(character, "armour", -1);
    }

    private boolean hasArmour(final ResolvationData resolvationData) {
        return getArmourValue(resolvationData) > 0;
    }

    private int getArmourValue(final ResolvationData resolvationData) {
        final Ff12Character character = (Ff12Character) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final int armour = characterHandler.getAttributeHandler().resolveValue(character, "armour");
        return armour;
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException("Fleeing from battle is not supported in this fight mode.");
    }

}
