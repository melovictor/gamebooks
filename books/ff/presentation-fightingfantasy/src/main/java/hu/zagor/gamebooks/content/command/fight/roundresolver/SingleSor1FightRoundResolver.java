package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round handler for sorcery 1.
 * @author Tamas_Szekeres
 */
@Component("singlesor1FightRoundResolver")
public class SingleSor1FightRoundResolver implements FfFightRoundResolver {
    private static final int EXTRA_DAMAGE_OVER_SUCCESSFUL_LUCK_TEST = -1;
    private static final int EXTRA_DAMAGE_OVER_FAILED_LUCK_TEST = -3;
    private static final int LUCK_TEST_RESULT_POSITION = 4;
    private static final int MANTICORE_EXTRA_DAMAGE = -4;
    private static final int MANTICORE_HIGHER_DAMAGE_ROLL = 5;
    private static final String MANTICORE_ID = "18";
    @Autowired @Qualifier("singleFightRoundResolver") private SingleFightRoundResolver superResolver;
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] roundResult = superResolver.resolveRound(command, resolvationData, beforeRoundResult);

        if (isManticore(command) && weLost(roundResult)) {
            rollForHigherDamage(command, resolvationData);
        }

        return roundResult;
    }

    private void rollForHigherDamage(final FfFightCommand command, final ResolvationData resolvationData) {
        final int[] rolledNumbers = generator.getRandomNumber(1);
        final String rolledDice = renderer.render(generator.getDefaultDiceSide(), rolledNumbers);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", rolledDice, rolledNumbers[0]);
        if (rolledNumbers[0] >= MANTICORE_HIGHER_DAMAGE_ROLL) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();

            if (command.isLuckOnDefense()) {
                alterLuckTestHighDamageResult(command, resolvationData, messages);
            } else {
                noLuckTestHighDamage(character, messages);
            }
        }
    }

    private void alterLuckTestHighDamageResult(final FfFightCommand command, final ResolvationData resolvationData, final FightCommandMessageList messages) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        if (luckTestWasSuccessful(command, messages)) {
            ensureDeductionOfNormalDamageWithMessage(character, messages);
        } else {
            ensureDeductionOfHigherDamageWithMessage(character, messages);
        }
    }

    private boolean luckTestWasSuccessful(final FfFightCommand command, final FightCommandMessageList messages) {
        messages.remove(LUCK_TEST_RESULT_POSITION);
        return command.getLuckOnDefenseResult();
    }

    private void ensureDeductionOfHigherDamageWithMessage(final FfCharacter character, final FightCommandMessageList messages) {
        character.changeStamina(EXTRA_DAMAGE_OVER_FAILED_LUCK_TEST);
        messages.addKey("page.sor1.manticoreHighDamage");
    }

    private void ensureDeductionOfNormalDamageWithMessage(final FfCharacter character, final FightCommandMessageList messages) {
        character.changeStamina(EXTRA_DAMAGE_OVER_SUCCESSFUL_LUCK_TEST);
        messages.addKey("page.sor1.manticoreHighDamage.lucky");
    }

    private void noLuckTestHighDamage(final FfCharacter character, final FightCommandMessageList messages) {
        messages.addKey("page.sor1.manticoreHighDamage");
        character.changeStamina(MANTICORE_EXTRA_DAMAGE);
    }

    private boolean weLost(final FightRoundResult[] roundResult) {
        return roundResult[0] == FightRoundResult.LOSE;
    }

    private boolean isManticore(final FfFightCommand command) {
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        return MANTICORE_ID.equals(enemy.getId());
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        superResolver.resolveFlee(command, resolvationData);
    }

}
