package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

/**
 * Transforms the loseRound element inside the fight section.
 * @author Tamas_Szekeres
 */
public class FightLoseRoundTransformer extends FightRoundResultBorderingTransformer {

    @Override
    protected void doRoundResultSpecificTransform(final RoundEvent roundEvent) {
        roundEvent.setRoundResult(FightRoundResult.LOSE);
    }

}
