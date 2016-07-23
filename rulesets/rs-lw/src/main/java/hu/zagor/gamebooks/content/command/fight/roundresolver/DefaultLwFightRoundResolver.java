package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Default fight handler for the Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultLwFightRoundResolver implements LwFightRoundResolver {
    @Autowired @Qualifier("d10") private RandomNumberGenerator generator;
    @Autowired private LwDamageResultProvider damageResultProvider;

    @Override
    public void resolveRound(final LwFightCommand command, final ResolvationData resolvationData) {
        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final FightCommandMessageList messages = command.getMessages();
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final LwAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final LwEnemy enemy = enemies.get(0);

        final int commandRatio = calculateCommandRatio(character, attributeHandler, enemy);
        final int[] randomNumber = generator.getRandomNumber(new DiceConfiguration(1, 0, 9));

        final LwDamageResult lwDamageResult = damageResultProvider.getLwDamageResult(commandRatio, randomNumber[0]);
        if (lwDamageResult.isEnemyKilled()) {
            enemy.setEndurance(0);
            messages.add("Enemy killed instantly.");
            command.setOngoing(false);
        } else if (lwDamageResult.isLwKilled()) {
            attributeHandler.handleModification(character, "endurance", 0, ModifyAttributeType.set);
            messages.add("LW killed instantly.");
            command.setOngoing(false);
        } else {
            attributeHandler.handleModification(character, "endurance", -lwDamageResult.getLwSuffers());
            enemy.setEndurance(enemy.getEndurance() - lwDamageResult.getEnemySuffers());
            messages.add("LW hit enemy, enemy hit LW.");
            if (!attributeHandler.isAlive(character)) {
                command.setOngoing(false);
            } else if (enemiesDead(command.getResolvedEnemies())) {
                command.setOngoing(false);
            }
        }
    }

    private boolean enemiesDead(final List<LwEnemy> resolvedEnemies) {
        return resolvedEnemies.size() == 1 && resolvedEnemies.get(0).getEndurance() <= 0;
    }

    private int calculateCommandRatio(final LwCharacter character, final LwAttributeHandler attributeHandler, final LwEnemy enemy) {
        final int commandRatio = attributeHandler.resolveValue(character, "commandSkill") - enemy.getCombatSkill();
        // add kai discipline stuff as well...
        return commandRatio;
    }

    @Override
    public void resolveFlee(final LwFightCommand command, final ResolvationData resolvationData) {
        final LwEnemy enemy = command.getResolvedEnemies().get(0);
        final FightCommandMessageList messages = command.getMessages();
        final FightFleeData fleeData = command.getFleeData();
        getFleeTextResourceList(messages, fleeData);
        if (fleeData.isSufferDamage()) {
            fleeFromEnemy((LwCharacter) resolvationData.getCharacter(), enemy, messages);
        }
    }

    private void fleeFromEnemy(final LwCharacter character, final LwEnemy enemy, final FightCommandMessageList messages) {
        messages.addKey("page.ff.label.fight.single.flee", new Object[]{enemy.getName()});
        doLoseFightRound(character, messages, enemy);
    }

    private void doLoseFightRound(final LwCharacter character, final FightCommandMessageList messages, final LwEnemy enemy) {
        // TODO: damage self
        resolveDefeatMessage(messages, enemy);
    }

    private void resolveDefeatMessage(final FightCommandMessageList messages, final LwEnemy enemy) {
        messages.addKey("page.lw.label.fight.failedDefense", enemy.getName());
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
