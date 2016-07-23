package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FfFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.Ship15FightRoundResolver;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import java.util.ArrayList;
import java.util.List;

/**
 * FF15 implementation of ship-to-ship battle.
 * @author Tamas_Szekeres
 */
public class Ff15FightCommandShipSubResolver extends FightCommandBasicSubResolver {

    @Override
    public List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> resolveList = new ArrayList<>();
        resolveBattlingParties(command, resolvationData, null);
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        command.setOngoing(true);
        resolveBattleRound(command, resolvationData, resolveList);
        characterHandler.getAttributeHandler().sanityCheck((FfCharacter) resolvationData.getCharacter());
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
            command.setBattleType("ship-15");
            command.setOngoing(true);
        }
    }

    @Override
    protected void handleAttacking(final FfFightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        command.increaseBattleRound();
        command.getMessages().setRoundMessage(command.getRoundNumber());

        final FfFightRoundResolver roundResolver = getBeanFactory().getBean(Ship15FightRoundResolver.class);
        roundResolver.resolveRound(command, resolvationData, null);

        if (allEnemiesDead(command.getResolvedEnemies())) {
            command.setOngoing(false);
            for (final FightOutcome outcome : command.getWin()) {
                if (command.getRoundNumber() >= outcome.getMin() && command.getRoundNumber() <= outcome.getMax()) {
                    resolveList.add(outcome.getParagraphData());
                }
            }
        } else if (shipIsDestroyed(resolvationData)) {
            command.setOngoing(false);
            resolveList.add(command.getLose());
        } else {
            command.setOngoing(true);
            command.setKeepOpen(true);
        }

    }

    private boolean allEnemiesDead(final List<FfEnemy> list) {
        boolean isEnemyAlive = false;
        for (final FfEnemy enemy : list) {
            isEnemyAlive |= enemy.getStamina() > 0;
        }
        return !isEnemyAlive;
    }

    private boolean shipIsDestroyed(final ResolvationData resolvationData) {
        final Ff15Character character = (Ff15Character) resolvationData.getCharacter();
        return character.getShipAttributes().getShield() <= 0;
    }

}
