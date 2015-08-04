package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for playing a single round of ship-to-ship battle in book FF15.
 * @author Tamas_Szekeres
 */
@Component
public class Ship15FightRoundResolver implements FightRoundResolver {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer diceResultRenderer;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        final Ff15ShipAttributes ship = character.getShipAttributes();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final boolean usingMissle = Boolean.valueOf(interactionHandler.getLastFightCommand(character, "missile"));
        final int numberOfEnemies = command.getResolvedEnemies().size();

        boolean mainTargetKilled = false;
        if (usingMissle && ship.getSmartMissile() > 0) {
            if (numberOfEnemies == 1) {
                mainTargetKilled = mainEnemyKilled(command);
            } else {
                killNonSelectedEnemy(resolvationData, command);
            }
            ship.setSmartMissile(ship.getSmartMissile() - 1);
        }
        if (!mainTargetKilled) {
            attackMainTarget(resolvationData, command);
        }
        sufferReturnFires(resolvationData, command);

        return null;
    }

    private void sufferReturnFires(final ResolvationData resolvationData, final FightCommand command) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        final Ff15ShipAttributes ship = character.getShipAttributes();
        final FightCommandMessageList messages = command.getMessages();
        final List<FfEnemy> enemies = command.getResolvedEnemies();

        for (final FfEnemy enemy : enemies) {
            if (enemy.getStamina() > 0) {
                receiveEnemyAttack(messages, ship, enemy);
            }
        }
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException("Fleeing from battle is not supported in this fight mode.");
    }

    private void attackMainTarget(final ResolvationData resolvationData, final FightCommand command) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, "enemyId");
        final Ff15ShipAttributes ship = character.getShipAttributes();
        final FfEnemy enemy = fetchEnemy(command.getResolvedEnemies(), enemyId);

        attackEnemy(command.getMessages(), ship, enemy);
    }

    private void receiveEnemyAttack(final FightCommandMessageList messageList, final Ff15ShipAttributes ship, final FfEnemy enemy) {
        final int[] attackRolls = generator.getRandomNumber(2);
        final String diceResults = diceResultRenderer.render(generator.getDefaultDiceSide(), attackRolls);
        String resultMessageKey;
        final int attackRollTotal = attackRolls[0];
        if (attackRollTotal >= enemy.getSkill()) {
            resultMessageKey = "page.ff.fight.ff15.ship.laserMissedUs";
        } else {
            resultMessageKey = "page.ff.fight.ff15.ship.laserHitUs";
            ship.setShield(ship.getShield() - 1);
        }
        messageList.addKey("page.ff.fight.ff15.enemyAttackStrength", enemy.getName(), diceResults, attackRollTotal);
        messageList.addKey(resultMessageKey, enemy.getName());
    }

    private void attackEnemy(final FightCommandMessageList messageList, final Ff15ShipAttributes ship, final FfEnemy enemy) {
        final int[] attackRolls = generator.getRandomNumber(2);
        final String diceResults = diceResultRenderer.render(generator.getDefaultDiceSide(), attackRolls);
        String resultMessageKey;
        final int attackRollTotal = attackRolls[0];
        if (attackRollTotal >= ship.getWeaponStrength()) {
            resultMessageKey = "page.ff.fight.ff15.ship.laserMissedEnemy";
        } else {
            resultMessageKey = "page.ff.fight.ff15.ship.laserHitEnemy";
            enemy.setStamina(enemy.getStamina() - 1);
        }
        messageList.addKey("page.ff.fight.ff15.ourAttackStrength", diceResults, attackRollTotal);
        messageList.addKey(resultMessageKey, enemy.getName());
    }

    private void killNonSelectedEnemy(final ResolvationData resolvationData, final FightCommand command) {
        final List<FfEnemy> enemies = command.getResolvedEnemies();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, "enemyId");
        final List<String> enemyListClone = new ArrayList<String>(command.getEnemies());
        enemyListClone.remove(enemyId);
        killEnemy(command, fetchEnemy(enemies, enemyListClone.get(0)));
    }

    private FfEnemy fetchEnemy(final List<FfEnemy> enemies, final String enemyId) {
        FfEnemy mainEnemy = null;
        for (final FfEnemy enemy : enemies) {
            if (enemyId.equals(enemy.getId())) {
                mainEnemy = enemy;
            }
        }
        return mainEnemy;
    }

    private boolean mainEnemyKilled(final FightCommand command) {
        return killEnemy(command, command.getResolvedEnemies().get(0));
    }

    private boolean killEnemy(final FightCommand command, final FfEnemy enemy) {
        final FightCommandMessageList messages = command.getMessages();
        if ("25".equals(enemy.getId())) {
            messages.addKey("page.ff.fight.ff15.ship.damagedBySmartMissile");
            enemy.setStamina(enemy.getStamina() - 2);
        } else {
            messages.addKey("page.ff.fight.ff15.ship.killBySmartMissile", enemy.getName());
            enemy.setStamina(0);
        }
        return enemy.getStamina() <= 0;
    }
}
