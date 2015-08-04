package hu.zagor.gamebooks.content.command.fight.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightRoundResult}.
 * @author Tamas_Szekeres
 */
@Test
public class FightRoundResultTest {

    public void testValueOfShouldReturnProperEnum() {
        // GIVEN
        // WHEN
        final FightRoundResult returned = FightRoundResult.valueOf("WIN");
        // THEN
        Assert.assertEquals(returned, FightRoundResult.WIN);
    }

}
