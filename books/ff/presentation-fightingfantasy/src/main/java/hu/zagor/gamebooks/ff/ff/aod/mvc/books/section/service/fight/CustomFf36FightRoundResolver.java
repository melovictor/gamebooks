package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Custom fight round resolver for FF36.
 * @author Tamas_Szekeres
 */
@Component
public class CustomFf36FightRoundResolver implements FightRoundResolver {
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;
    @Resource(name = "ff36BattleRoundResults") private Map<Integer, BattleRoundResult> battleRoundResults;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {

        final FightCommandMessageList messages = command.getMessages();
        command.increaseBattleRound();
        messages.setRoundMessage(command.getRoundNumber());

        final List<FfAllyCharacter> resolvedAllies = command.getResolvedAllies();
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final int totalAllies = getAllyHealth(resolvedAllies);
        final int totalEnemies = getEnemyHealth(resolvedEnemies);

        final int[] randomNumber = generator.getRandomNumber(1);
        messages.addKey("page.ff36.fight.army.attackRoll", renderer.render(generator.getDefaultDiceSide(), randomNumber), randomNumber[0]);
        final BattleRoundResult battleRoundResult = battleRoundResults.get(randomNumber[0]);

        final int change;
        if (totalAllies < totalEnemies) {
            change = battleRoundResult.getSelfWeak();
        } else if (totalAllies > totalEnemies) {
            change = battleRoundResult.getSelfStrong();
        } else {
            change = battleRoundResult.getEgal();
        }

        if (change > 0) {
            killEnemies(change, resolvedEnemies);
            messages.addKey("page.ff36.fight.army.charge.won", change);
        } else {
            killAllies(change, resolvedAllies);
            messages.addKey("page.ff36.fight.army.charge.lost", -change);
        }

        return null;
    }

    private void killAllies(final int change, final List<FfAllyCharacter> resolvedAllies) {
        final FfAllyCharacter ffAllyCharacter = resolvedAllies.get(resolvedAllies.size() - 1);
        ffAllyCharacter.setStamina(ffAllyCharacter.getStamina() + change);
    }

    private void killEnemies(final int change, final List<FfEnemy> resolvedEnemies) {
        int toKill = change;
        for (final FfEnemy enemy : resolvedEnemies) {
            final int enemyStamina = enemy.getStamina();
            if (enemyStamina > 0) {
                if (enemyStamina >= toKill) {
                    enemy.setStamina(enemyStamina - toKill);
                    toKill = 0;
                } else {
                    toKill -= enemyStamina;
                    enemy.setStamina(0);
                }
            }
        }
    }

    private int getAllyHealth(final List<FfAllyCharacter> list) {
        int totalAlive = 0;
        for (final FfAllyCharacter ally : list) {
            totalAlive += ally.getStamina();
        }
        return totalAlive;
    }

    private int getEnemyHealth(final List<FfEnemy> list) {
        int totalAlive = 0;
        for (final FfEnemy enemy : list) {
            totalAlive += enemy.getStamina();
        }
        return totalAlive;
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        throw new UnsupportedOperationException();
    }

}
