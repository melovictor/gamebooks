package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
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
@Component("singleAllysor1FightRoundResolver")
public class SingleAllySor1FightRoundResolver implements FightRoundResolver {
    private static final int MANTICORE_EXTRA_DAMAGE = -4;
    private static final int MANTICORE_HIGHER_DAMAGE_ROLL = 5;
    private static final String MANTICORE_ID = "18";
    @Autowired @Qualifier("singleAllyFightRoundResolver") private SingleFightRoundResolver superResolver;
    @Autowired private DiceResultRenderer renderer;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] roundResult = superResolver.resolveRound(command, resolvationData, beforeRoundResult);

        if (isManticore(command) && weLost(roundResult)) {
            rollForHigherDamage(command, resolvationData);
        }

        return roundResult;
    }

    private void rollForHigherDamage(final FightCommand command, final ResolvationData resolvationData) {
        final int[] rolledNumbers = generator.getRandomNumber(1);
        final String rolledDice = renderer.render(generator.getDefaultDiceSide(), rolledNumbers);
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", rolledDice, rolledNumbers[0]);
        if (rolledNumbers[0] >= MANTICORE_HIGHER_DAMAGE_ROLL) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            noLuckTestHighDamage(character, messages);
        }
    }

    private void noLuckTestHighDamage(final FfCharacter character, final FightCommandMessageList messages) {
        messages.addKey("page.sor1.manticoreHighDamage.ally", character.getName());
        character.changeStamina(MANTICORE_EXTRA_DAMAGE);
    }

    private boolean weLost(final FightRoundResult[] roundResult) {
        return roundResult[0] == FightRoundResult.LOSE;
    }

    private boolean isManticore(final FightCommand command) {
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        return MANTICORE_ID.equals(enemy.getId());
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        superResolver.resolveFlee(command, resolvationData);
    }

}
