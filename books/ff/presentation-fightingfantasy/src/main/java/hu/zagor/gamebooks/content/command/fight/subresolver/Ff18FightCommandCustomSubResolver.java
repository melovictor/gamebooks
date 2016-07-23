package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.roundresolver.Custom18FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.ArrayList;
import java.util.List;

/**
 * FF18 implementation of custom battle.
 * @author Tamas_Szekeres
 */

public class Ff18FightCommandCustomSubResolver extends FightCommandBasicSubResolver {

    @Override
    public List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = new ArrayList<>();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        prepareLuckTest(command, character, characterHandler.getInteractionHandler());
        resolveBattlingParties(command, resolvationData, null);
        command.setOngoing(true);
        resolveBattleRound(command, resolvationData, resolveList);
        characterHandler.getAttributeHandler().sanityCheck(character);
        resolveBattlingParties(command, resolvationData, resolveList);
        return resolveList;
    }

    private void resolveBattleRound(final FfFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final String lastFightCommand = characterHandler.getInteractionHandler().getLastFightCommand(character);
        if (FfFightCommand.ATTACKING.equals(lastFightCommand)) {
            handleAttacking(command, resolvationData, resolveList);
        } else {
            command.setBattleType("custom-18");
            command.setOngoing(true);
        }
    }

    @Override
    protected void handleAttacking(final FfFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        command.increaseBattleRound();
        command.getMessages().setRoundMessage(command.getRoundNumber());

        final FfFightRoundResolver roundResolver = getBeanFactory().getBean(Custom18FightRoundResolver.class);
        roundResolver.resolveRound(command, resolvationData, null);

        if (allEnemiesDead(command.getResolvedEnemies())) {
            handleWinning(command, resolveList);
        } else if (weAreDead(resolvationData)) {
            handleLosing(command, resolveList);
        } else {
            handleContinuation(command);
        }

    }

    private void handleContinuation(final FfFightCommand command) {
        command.setOngoing(true);
        command.setKeepOpen(true);
    }

    private void handleLosing(final FfFightCommand command, final List<ParagraphData> resolveList) {
        command.setOngoing(false);
        resolveList.add(command.getLose());
    }

    private void handleWinning(final FfFightCommand command, final List<ParagraphData> resolveList) {
        command.setOngoing(false);
        final int roundNumber = command.getRoundNumber();
        for (final FightOutcome outcome : command.getWin()) {
            if (roundNumber >= outcome.getMin() && roundNumber <= outcome.getMax()) {
                resolveList.add(outcome.getParagraphData());
            }
        }
    }

    private boolean allEnemiesDead(final List<FfEnemy> list) {
        boolean isEnemyAlive = false;
        for (final FfEnemy enemy : list) {
            isEnemyAlive |= enemy.getStamina() > 0;
        }
        return !isEnemyAlive;
    }

    private boolean weAreDead(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        return !attributeHandler.isAlive(character);
    }
}
