package hu.zagor.gamebooks.lw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.lw.character.KaiDisciplines;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Weaponskill;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import hu.zagor.gamebooks.lw.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.lw.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.lw.character.item.LwItem;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Default fight handler for the Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultLwFightRoundResolver implements LwFightRoundResolver {
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private LwDamageResultProvider damageResultProvider;

    @Override
    public void resolveRound(final LwFightCommand command, final ResolvationData resolvationData) {
        resolveRound(command, resolvationData, false);
    }

    private void resolveRound(final LwFightCommand command, final ResolvationData resolvationData, final boolean fleeing) {
        final FightCommandMessageList messages = command.getMessages();
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();

        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final LwEnemy enemy = enemies.get(0);

        final int commandRatio = calculateCommandRatio(character, characterHandler, enemy);
        final int[] randomNumber = generator.getRandomNumber(new DiceConfiguration(1, 0, 9));

        messages.addKey("page.lw.label.fight.single.combatRatio", commandRatio, renderer.render(generator.getDefaultDiceSide(), randomNumber));

        final LwDamageResult lwDamageResult = damageResultProvider.getLwDamageResult(commandRatio, randomNumber[0]);
        if (lwDamageResult.isEnemyKilled()) {
            instantVictory(command, fleeing);
        } else if (lwDamageResult.isLwKilled()) {
            instantDeath(command, fleeing, resolvationData);
        } else {
            normalDamage(command, fleeing, resolvationData, lwDamageResult);
        }
    }

    private void normalDamage(final LwFightCommand command, final boolean fleeing, final ResolvationData resolvationData, final LwDamageResult lwDamageResult) {

        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final LwAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final LwEnemy enemy = enemies.get(0);

        final FightCommandMessageList messages = command.getMessages();

        final boolean lwZero = lwDamageResult.getLwSuffers() == 0;
        final boolean enZero = lwDamageResult.getEnemySuffers() == 0;

        String key = "page.lw.label.fight.single.nonLethal";
        if (lwZero) {
            key += ".lw0";
        } else if (enZero) {
            key += ".en0";
        }
        if (fleeing) {
            key += ".fleeing";
        }

        messages.addKey(key, lwDamageResult.getLwSuffers(), lwDamageResult.getEnemySuffers(), enemy.getName());

        attributeHandler.handleModification(character, "endurance", -lwDamageResult.getLwSuffers());
        attributeHandler.handleModification(character, "enduranceLostInCombat", lwDamageResult.getLwSuffers());
        if (!fleeing) {
            enemy.setEndurance(enemy.getEndurance() - lwDamageResult.getEnemySuffers());
        }
        if (!attributeHandler.isAlive(character)) {
            command.setOngoing(false);
        } else if (enemiesDead(command.getResolvedEnemies())) {
            command.setOngoing(false);
        }
    }

    private void instantDeath(final LwFightCommand command, final boolean fleeing, final ResolvationData resolvationData) {
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final LwAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final LwEnemy enemy = enemies.get(0);
        final FightCommandMessageList messages = command.getMessages();
        attributeHandler.handleModification(character, "endurance", 0, ModifyAttributeType.set);
        command.setOngoing(false);
        if (fleeing) {
            messages.addKey("page.lw.label.fight.single.killedDuringFleeing", enemy.getName());
        } else {
            messages.addKey("page.lw.label.fight.single.lwKilled", enemy.getName());
        }
    }

    private void instantVictory(final LwFightCommand command, final boolean fleeing) {
        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final LwEnemy enemy = enemies.get(0);
        final FightCommandMessageList messages = command.getMessages();
        if (fleeing) {
            messages.addKey("page.lw.label.fight.single.fledUnharmed", enemy.getName());
        } else {
            enemy.setEndurance(0);
            command.setOngoing(false);
            messages.addKey("page.lw.label.fight.single.enemyKilled", enemy.getName());
        }
    }

    private boolean enemiesDead(final List<LwEnemy> resolvedEnemies) {
        return resolvedEnemies.size() == 1 && resolvedEnemies.get(0).getEndurance() <= 0;
    }

    private int calculateCommandRatio(final LwCharacter character, final LwCharacterHandler characterHandler, final LwEnemy enemy) {
        final LwItem selectedWeapon = characterHandler.getItemHandler().getEquippedWeapon(character);
        int commandRatio = characterHandler.getAttributeHandler().resolveValue(character, "combatSkill") - enemy.getCombatSkill();
        final KaiDisciplines kaiDisciplines = character.getKaiDisciplines();
        if (hasWeaponSkillFor(kaiDisciplines.getWeaponskill(), selectedWeapon.getWeaponType())) {
            commandRatio += 2;
        }
        if (kaiDisciplines.isMindblast() && !enemy.isMindshield()) {
            commandRatio += 2;
        }
        if (enemy.isMindblast() && !kaiDisciplines.isMindshield()) {
            commandRatio -= 2;
        }

        return commandRatio;
    }

    private boolean hasWeaponSkillFor(final Weaponskill weaponskill, final String weaponType) {
        boolean hasSkill = false;
        try {
            hasSkill = (boolean) ReflectionUtils.findMethod(weaponskill.getClass(), "is" + StringUtils.capitalize(weaponType)).invoke(weaponskill);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("A weapon is querying about weapon type '" + weaponType + "', which doesn't exists.");
        }
        return hasSkill;
    }

    @Override
    public void resolveFlee(final LwFightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        final FightFleeData fleeData = command.getFleeData();
        getFleeTextResourceList(messages, fleeData);
        if (fleeData.isSufferDamage()) {
            resolveRound(command, resolvationData, true);
        }
    }

    private void getFleeTextResourceList(final FightCommandMessageList messages, final FightFleeData fightFleeData) {
        final String text = fightFleeData == null ? null : fightFleeData.getText();
        if (text == null) {
            messages.addKey("page.lw.label.fight.flee");
        } else {
            messages.add(text + ".");
        }
    }

}
