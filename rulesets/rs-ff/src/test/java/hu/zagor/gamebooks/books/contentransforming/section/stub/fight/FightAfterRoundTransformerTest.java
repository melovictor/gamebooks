package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightAfterRoundTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightAfterRoundTransformerTest extends AbstractTransformerTest {

    private FightAfterRoundTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private FightCommand command;
    private ChoicePositionCounter positionCounter;
    private FfParagraphData paragraphData;
    private FightCommand fightCommand;
    private FightCommandMessageList messages;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightAfterRoundTransformer();
        init(mockControl);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        fightCommand = new FightCommand();
        messages = mockControl.createMock(FightCommandMessageList.class);
        Whitebox.setInternalState(fightCommand, "messages", messages);
    }

    @BeforeMethod
    public void setUpMethod() {
        paragraphData = new FfParagraphData();
        command = new FightCommand();
        mockControl.reset();
    }

    public void testSetRandomShouldSetRandomToAfterRandom() {
        // GIVEN
        final FightRoundBoundingCommand fightRandom = new FightRoundBoundingCommand(fightCommand);
        mockControl.replay();
        // WHEN
        underTest.setBounding(command, fightRandom);
        // THEN
        Assert.assertSame(command.getAfterBounding(), fightRandom);
    }

    public void testDoTransformWhenNoCommandsAreAvailableShouldDoNothing() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        expectAttribute("canUseLuck");
        expectAttribute("everyNth");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertFalse(command.getAfterBounding().isLuckAllowed());
        Assert.assertEquals(Whitebox.getInternalState(command.getAfterBounding(), "nth"), 1);
    }

    public void testDoTransformWhenNonRandomCommandIsPresentShouldDoNothing() {
        // GIVEN
        paragraphData.getCommands().add(new UserInputCommand());
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        expectAttribute("canUseLuck", "true");
        expectAttribute("everyNth");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertTrue(command.getAfterBounding().isLuckAllowed());
        Assert.assertEquals(Whitebox.getInternalState(command.getAfterBounding(), "nth"), 1);
    }

    public void testDoTransformWhenRandomCommandIsPresentShouldSetItUp() {
        // GIVEN
        final RandomCommand random = new RandomCommand();
        paragraphData.getCommands().add(random);
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        expectAttribute("canUseLuck", "false");
        expectAttribute("everyNth", "3");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getAfterBounding().getCommands().get(0), random);
        Assert.assertFalse(command.getAfterBounding().isLuckAllowed());
        Assert.assertEquals(Whitebox.getInternalState(command.getAfterBounding(), "nth"), 3);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
