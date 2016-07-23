package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightBeforeRoundTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightBeforeRoundTransformerTest {

    private FightBeforeRoundTransformer underTest;

    public void testSetRandomShouldSetRandomToBeforeRandom() {
        // GIVEN
        underTest = new FightBeforeRoundTransformer();
        final FfFightCommand command = new FfFightCommand();
        final FightCommandMessageList messages = new FightCommandMessageList();
        Whitebox.setInternalState(command, "messages", messages);
        final FightRoundBoundingCommand fightRandom = new FightRoundBoundingCommand(command);
        // WHEN
        underTest.setBounding(command, fightRandom);
        // THEN
        Assert.assertSame(command.getBeforeBounding(), fightRandom);
    }

}
