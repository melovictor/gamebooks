package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.SingleAllyFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.SingleFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.domain.LuckTestSettings;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * FF2 implementation of custom battle.
 * @author Tamas_Szekeres
 */

public class Ff2FightCommandCustomSubResolver extends FightCommandSupportedSubResolver {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer diceResultRenderer;
    @Autowired
    @Qualifier("singleFightRoundResolver")
    private SingleFightRoundResolver superSingleResolver;

    @Autowired
    @Qualifier("singleAllyFightRoundResolver")
    private SingleAllyFightRoundResolver superAlliedResolver;

    @Override
    public List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = new ArrayList<>();
        resolveBattleRound(command, resolvationData, resolveList);
        return resolveList;
    }

    private void resolveBattleRound(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final String lastFightCommand = characterHandler.getInteractionHandler().peekLastFightCommand(character);
        if (!FightCommand.ATTACKING.equals(lastFightCommand)) {
            command.setBattleType("custom2");
            command.setOngoing(true);
        }
        resolveList.addAll(super.doResolve(command, resolvationData));
    }

    @Override
    void executeBattle(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        command.setKeepOpen(true);

        final int[] randomNumber;
        final FfAllyCharacter ally = command.getResolvedAllies().isEmpty() ? null : command.getFirstAlly();
        final List<FfEnemy> resolvedEnemies = command.getResolvedEnemies();
        final FightCommandMessageList messages = command.getMessages();
        if (allyStillAlive(ally)) {
            randomNumber = generator.getRandomNumber(1);

            final String diceText = diceResultRenderer.render(generator.getDefaultDiceSide(), randomNumber);
            messages.addKey("page.ff.label.random.after", diceText, randomNumber[0]);
        } else {
            randomNumber = new int[]{2};
        }

        if (randomNumber[0] % 2 == 0 || !allyStillAlive(ally)) {
            if (allyStillAlive(ally)) {
                messages.addKey("page.ff2.fight.tallAttacksUs");
            }
            final FightRoundResult[] roundResults = superSingleResolver.resolveRound(command, resolvationData, beforeRoundResult);
            updateBattleStatistics(command, roundResults);
        } else {
            messages.addKey("page.ff2.fight.tallAttacksShort");
            final LuckTestSettings luckTestSettings = prepareAllyLuckTest(command);
            if (!getEnemyStatusEvaluator().enemiesAreDead(resolvedEnemies, command.getRoundNumber())) {
                final ResolvationData newResolvationData = new ResolvationData(resolvationData.getRootData(), ally, resolvationData.getEnemies(),
                    resolvationData.getInfo());
                superAlliedResolver.resolveRound(command, newResolvationData, beforeRoundResult);
            }
            prepareAllyLuckTest(command, luckTestSettings);
            if (!allyStillAlive(ally)) {
                resolvationData.getInfo().getCharacterHandler().getItemHandler().addItem(resolvationData.getCharacter(), "4001", 1);
            }
        }
    }

    private boolean allyStillAlive(final FfCharacter ally) {
        return ally != null;
    }

}
