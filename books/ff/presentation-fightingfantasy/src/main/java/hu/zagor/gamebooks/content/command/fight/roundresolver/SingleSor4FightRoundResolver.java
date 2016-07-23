package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link FightRoundResult} interface for resolving single fight rounds in SOR4.
 * @author Tamas_Szekeres
 */
@Component("singlesor4FightRoundResolver")
public class SingleSor4FightRoundResolver extends SingleSorFightRoundResolver {
    private static final int NUMBER_OF_TORTURER_BOUND_ROUNDS = 3;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        if (fightWithTorturer(command)) {
            handleTorturer(command, resolvationData);
        }
        final FightRoundResult[] resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);

        if (fightWithTorturer(command)) {
            loseOneWhipBoundRound(resolvationData);
        }

        return resolveRound;
    }

    private void handleTorturer(final FfFightCommand command, final ResolvationData resolvationData) {
        if (!alreadyInWhip(resolvationData)) {
            final int[] randomNumber = generator.getRandomNumber(1);
            final String diceRoll = renderer.render(generator.getDefaultDiceSide(), randomNumber);
            final FightCommandMessageList messages = command.getMessages();
            messages.switchToPreRoundMessages();
            messages.addKey("page.ff.label.random.after", diceRoll, randomNumber[0]);
            if (randomNumber[0] == 1) {
                messages.addKey("page.sor4.fight.torturer.whipped");
                final Character character = resolvationData.getCharacter();
                final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
                itemHandler.addItem(character, "4096", NUMBER_OF_TORTURER_BOUND_ROUNDS);
                itemHandler.addItem(character, "4097", 1);
            }

            messages.switchToRoundMessages();
        }
    }

    private boolean alreadyInWhip(final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        return itemHandler.hasItem(character, "4096");
    }

    private void loseOneWhipBoundRound(final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, "4096", 1);
        if (!itemHandler.hasItem(character, "4096")) {
            itemHandler.removeItem(character, "4097", 1);
        }
    }

    private boolean fightWithTorturer(final FfFightCommand command) {
        return command.getEnemies().contains("32");
    }

    @Override
    void doWinFight(final FfFightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doWinFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(true);
            final FightFleeData fleeData = new FightFleeData();
            fleeData.setSufferDamage(true);
            command.setFleeData(fleeData);
        }
    }

    @Override
    void doLoseFight(final FfFightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doLoseFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(false);
        }
    }

    @Override
    void doTieFight(final FfFightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        super.doTieFight(command, result, enemyIdx, dto);

        if ("1".equals(dto.getEnemy().getId())) {
            command.setFleeAllowed(false);
        }
    }
}
