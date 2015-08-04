package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;

/**
 * Transforms the afterRound element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightBeforeRoundTransformer extends FightRoundBorderingTransformer {

    @Override
    protected void setBounding(final FightCommand command, final FightRoundBoundingCommand fightRandom) {
        command.setBeforeBounding(fightRandom);
    }
}
