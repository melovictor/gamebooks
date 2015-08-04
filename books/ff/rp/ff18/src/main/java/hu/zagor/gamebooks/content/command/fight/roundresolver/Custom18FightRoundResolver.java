package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for playing a single round of custom battle in book FF18.
 * @author Tamas_Szekeres
 */
@Component
public class Custom18FightRoundResolver implements FightRoundResolver {

    private static final int HERO_WINS = 7;
    private static final int DAMAGE = 2;
    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer diceResultRenderer;
    @Autowired
    @Qualifier("singleFightRoundResolver")
    private SingleFightRoundResolver superResolver;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final int[] thrownResult = generator.getRandomNumber(2);
        final FightRoundResult[] result = new FightRoundResult[1];

        final FightCommandMessageList messages = command.getMessages();
        messages.addKey("page.ff.label.random.after", diceResultRenderer.render(generator.getDefaultDiceSide(), thrownResult), thrownResult[0]);

        final FfEnemy enemy = command.getResolvedEnemies().get(0);

        if (thrownResult[0] < HERO_WINS) {
            messages.addKey("page.ff.label.fight.single.failedDefense", enemy.getCommonName());
            result[0] = FightRoundResult.LOSE;
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(-DAMAGE);
            superResolver.handleDefeatLuckTest(command, new FightDataDto(enemy, messages, resolvationData, null));
        } else {
            messages.addKey("page.ff.label.fight.single.successfulAttack", enemy.getCommonName());
            result[0] = FightRoundResult.WIN;
            enemy.setStamina(enemy.getStamina() - DAMAGE);
            superResolver.handleVictoryLuckTest(command, new FightDataDto(enemy, messages, resolvationData, null));
        }

        return result;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        superResolver.resolveFlee(command, resolvationData);
    }

}
