package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;

/**
 * Transforms the afterRound element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightAfterRoundTransformer extends FightRoundBorderingTransformer {

    @Override
    protected void setBounding(final FfFightCommand command, final FightRoundBoundingCommand fightRandom) {
        command.setAfterBounding(fightRandom);
    }

}
