package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightNotWinRoundTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightNotWinRoundTransformerTest {

    private FightNotWinRoundTransformer underTest;

    public void testDoRoundResultSpecificTransformShouldSetRoundResultToNotWin() {
        // GIVEN
        underTest = new FightNotWinRoundTransformer();
        final RoundEvent roundEvent = new RoundEvent();
        // WHEN
        underTest.doRoundResultSpecificTransform(roundEvent);
        // THEN
        Assert.assertEquals(roundEvent.getRoundResult(), FightRoundResult.NOT_WIN);
    }

}
