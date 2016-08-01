package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service.fight;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Round resolver for ship and crew battles for FF16.
 * @author Tamas_Szekeres
 */
@Component("ff16shipFightRoundResolver")
public class ShipFf16FightRoundResolver implements FfFightRoundResolver {
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer diceResultRenderer;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final FightCommandMessageList messages = command.getMessages();

        final FightRoundResult[] result = new FightRoundResult[resolvedEnemies.size()];
        final Ff16Character character = (Ff16Character) resolvationData.getCharacter();

        final FfEnemy enemy = getSelectedEnemy(resolvedEnemies, character, (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler());

        final int[] selfAttackRoll = generator.getRandomNumber(2);
        final int[] enemyAttackRoll = generator.getRandomNumber(2);

        final int selfAttackStrength = selfAttackRoll[0] + character.getCrewStrike();
        final int enemyAttackStrength = enemyAttackRoll[0] + enemy.getSkill();

        messages.addKey("page.ff16.label.fight.ship.attackStrength.self", diceResultRenderer.render(generator.getDefaultDiceSide(), selfAttackRoll), selfAttackStrength);
        messages.addKey("page.ff16.label.fight.ship.attackStrength.enemy", enemy.getCommonName(),
            diceResultRenderer.render(generator.getDefaultDiceSide(), enemyAttackRoll), enemyAttackStrength);

        if (selfAttackStrength > enemyAttackStrength) {
            enemy.setStamina(enemy.getStamina() - 2);
            messages.addKey("page.ff16.label.fight.ship.successfulAttack", enemy.getCommonName());
        } else if (selfAttackStrength == enemyAttackStrength) {
            messages.addKey("page.ff16.label.fight.ship.tied", enemy.getCommonName());
        } else {
            character.setCrewStrength(character.getCrewStrength() - 2);
            messages.addKey("page.ff16.label.fight.ship.failedDefense", enemy.getCommonName());
        }

        return result;
    }

    private FfEnemy getSelectedEnemy(final List<FfEnemy> resolvedEnemies, final Ff16Character character, final FfUserInteractionHandler interactionHandler) {
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        FfEnemy enemy = null;
        for (final FfEnemy e : resolvedEnemies) {
            if (enemyId.equals(e.getId())) {
                enemy = e;
                break;
            }
        }
        return enemy;
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        for (final FfEnemy enemy : command.getResolvedEnemies()) {

            final Ff16Character character = (Ff16Character) resolvationData.getCharacter();

            final int[] selfAttackRoll = generator.getRandomNumber(2);
            final int[] enemyAttackRoll = generator.getRandomNumber(2);

            final int selfAttackStrength = selfAttackRoll[0] + character.getCrewStrike();
            final int enemyAttackStrength = enemyAttackRoll[0] + enemy.getSkill();

            messages.addKey("page.ff16.label.fight.ship.attackStrength.self", diceResultRenderer.render(generator.getDefaultDiceSide(), selfAttackRoll),
                selfAttackStrength);
            messages.addKey("page.ff16.label.fight.ship.attackStrength.enemy", enemy.getCommonName(),
                diceResultRenderer.render(generator.getDefaultDiceSide(), enemyAttackRoll), enemyAttackStrength);

            if (selfAttackStrength >= enemyAttackStrength) {
                messages.addKey("page.ff16.label.fight.ship.successfulDefense", enemy.getCommonName());
            } else {
                character.setCrewStrength(character.getCrewStrength() - 2);
                messages.addKey("page.ff16.label.fight.ship.failedDefense", enemy.getCommonName());
            }
        }
    }

}
