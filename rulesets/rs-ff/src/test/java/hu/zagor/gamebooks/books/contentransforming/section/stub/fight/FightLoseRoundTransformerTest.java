package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightLoseRoundTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightLoseRoundTransformerTest {

    private FightLoseRoundTransformer underTest;

    public void testDoROundResultSpecificTransformShouldSetRoundResultToLose() {
        // GIVEN
        underTest = new FightLoseRoundTransformer();
        final RoundEvent roundEvent = new RoundEvent();
        // WHEN
        underTest.doRoundResultSpecificTransform(roundEvent);
        // THEN
        Assert.assertEquals(roundEvent.getRoundResult(), FightRoundResult.LOSE);
    }

}
