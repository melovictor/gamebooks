package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.content.command.AbstractCommandTest;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.random.RandomCommand;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightRoundBoundingCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class FightRoundBoundingCommandTest extends AbstractCommandTest {

    private FightRoundBoundingCommand underTest;
    private IMocksControl mockControl;
    private RandomCommand randomCloned;
    private RandomCommand random;
    private FightCommand command;
    private FightCommandMessageList messages;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        random = mockControl.createMock(RandomCommand.class);
        randomCloned = mockControl.createMock(RandomCommand.class);

        command = new FightCommand();
        messages = mockControl.createMock(FightCommandMessageList.class);
        Whitebox.setInternalState(command, "messages", messages);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new FightRoundBoundingCommand(command);
        mockControl.reset();
    }

    public void testGetValidMoveShouldReturnNull() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertNull(returned);
    }

    public void testCloneWhenRandomIsSetShouldReturnClonedItem() throws CloneNotSupportedException {
        // GIVEN
        underTest.getCommands().add(random);
        expectTc(random, randomCloned);
        mockControl.replay();
        // WHEN
        final FightRoundBoundingCommand returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getCommands().get(0), randomCloned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
