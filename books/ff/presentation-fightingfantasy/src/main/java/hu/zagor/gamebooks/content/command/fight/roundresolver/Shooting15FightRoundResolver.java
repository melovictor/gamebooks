package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for playing a single round of blaster battle in book FF15.
 * @author Tamas_Szekeres
 */
@Component
public class Shooting15FightRoundResolver implements FightRoundResolver {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer diceResultRenderer;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        attackMainTarget(resolvationData, command);
        sufferReturnFires(resolvationData, command);
        return null;
    }

    private void sufferReturnFires(final ResolvationData resolvationData, final FightCommand command) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        final FightCommandMessageList messages = command.getMessages();
        final List<FfEnemy> enemies = command.getResolvedEnemies();

        for (final FfEnemy enemy : enemies) {
            if (enemy.getStamina() > 0) {
                receiveEnemyAttack(messages, character, enemy);
            }
        }
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException("Fleeing from battle is not supported in this fight mode.");
    }

    private void attackMainTarget(final ResolvationData resolvationData, final FightCommand command) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final String enemyId = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        final FfEnemy enemy = fetchEnemy(command.getResolvedEnemies(), enemyId);

        final int[] attackRolls = generator.getRandomNumber(2);
        final String diceResults = diceResultRenderer.render(generator.getDefaultDiceSide(), attackRolls);
        String resultMessageKey;
        final int attackRollTotal = attackRolls[0];

        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final int skill = attributeHandler.resolveValue(character, "skill");

        final int staminaDamage = characterHandler.getItemHandler().getEquippedWeapon(character, command.getUsableWeaponTypes()).getStaminaDamage();
        if (attackRollTotal >= skill) {
            resultMessageKey = "page.ff.fight.ff15.shoot.blasterMissedEnemy";
        } else {
            resultMessageKey = "page.ff.fight.ff15.shoot.blasterHitEnemy";
            enemy.setStamina(enemy.getStamina() - staminaDamage);
        }
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.fight.ff15.ourAttackStrength", diceResults, attackRollTotal);
        messages.addKey(resultMessageKey, enemy.getName(), staminaDamage);
    }

    private void receiveEnemyAttack(final FightCommandMessageList messageList, final Ff15Character character, final FfEnemy enemy) {
        final int[] attackRolls = generator.getRandomNumber(2);
        final String diceResults = diceResultRenderer.render(generator.getDefaultDiceSide(), attackRolls);
        String resultMessageKey;
        final int attackRollTotal = attackRolls[0];
        if (attackRollTotal >= enemy.getSkill()) {
            resultMessageKey = "page.ff.fight.ff15.shoot.blasterMissedUs";
        } else {
            resultMessageKey = "page.ff.fight.ff15.shoot.blasterHitUs";
            character.changeStamina(-enemy.getStaminaDamage());
        }
        messageList.addKey("page.ff.fight.ff15.enemyAttackStrength", enemy.getName(), diceResults, attackRollTotal);
        messageList.addKey(resultMessageKey, enemy.getName(), enemy.getStaminaDamage());
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

}
