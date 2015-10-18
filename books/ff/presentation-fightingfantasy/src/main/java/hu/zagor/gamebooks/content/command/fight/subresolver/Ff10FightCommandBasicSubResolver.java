package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import java.util.List;

/**
 * FF10-specific extension of {@link FightCommandBasicSubResolver}.
 * @author Tamas_Szekeres
 */
public class Ff10FightCommandBasicSubResolver extends FightCommandBasicSubResolver {

    private static final String ATTACK_STRENGTH_MALLUS_2 = "4001";

    private static final int HUNCHBACK_INITIALIZATION_ROUND = 0;
    private static final int HUNCHBACK_ESCAPE_ROUND = 3;
    private static final int DANISH_DOG_AS_MALLUS_ROUND = 4;

    private static final String HUNCHBACK = "18";

    @Override
    public List<ParagraphData> doResolve(final FightCommand command, final ResolvationData resolvationData) {
        final List<ParagraphData> doResolve = super.doResolve(command, resolvationData);

        final int roundNumber = command.getRoundNumber();
        handleHunchbackBattleInterruption(command, roundNumber);
        handleDanishDogAttackStrengthMallus(resolvationData, roundNumber);

        return doResolve;
    }

    private void handleDanishDogAttackStrengthMallus(final ResolvationData resolvationData, final int roundNumber) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Character character = resolvationData.getCharacter();
        if (itemHandler.hasItem(character, ATTACK_STRENGTH_MALLUS_2) && roundNumber >= DANISH_DOG_AS_MALLUS_ROUND) {
            itemHandler.removeItem(character, ATTACK_STRENGTH_MALLUS_2, 1);
        }
    }

    private void handleHunchbackBattleInterruption(final FightCommand command, final int roundNumber) {
        if (HUNCHBACK.equals(command.getEnemies().get(0))) {
            final FfEnemy ffEnemy = command.getResolvedEnemies().get(0);
            final boolean isFirstTry = ffEnemy.getStamina() < ffEnemy.getInitialStamina();
            if (roundNumber == HUNCHBACK_INITIALIZATION_ROUND && isFirstTry) {
                command.increaseBattleRound();
                command.increaseBattleRound();
                command.increaseBattleRound();
            } else if (roundNumber >= HUNCHBACK_ESCAPE_ROUND) {
                command.setOngoing(false);
            }
        }
    }

}
