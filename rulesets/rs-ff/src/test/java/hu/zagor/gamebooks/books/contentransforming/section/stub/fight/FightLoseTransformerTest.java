package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightLoseTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightLoseTransformerTest extends AbstractTransformerTest {

    private FightLoseTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private FightCommand command;
    private FfParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightLoseTransformer();
        init(mockControl);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        command = new FightCommand();
        paragraphData = new FfParagraphData();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldReadDataAndFillCommand() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        expectAttribute("autoTriggerRound");
        expectAttribute("autoTriggerStamina");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertEquals(command.getLose(), paragraphData);
        Assert.assertEquals(command.getAutoLoseRound(), 0);
        Assert.assertEquals(command.getAutoLoseStamina(), 0);
    }

    public void testDoTransformWhenAutoLoseParametersAreAvailableShouldReadDataAndFillCommand() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        expectAttribute("autoTriggerRound", "3");
        expectAttribute("autoTriggerStamina", "9");
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertEquals(command.getLose(), paragraphData);
        Assert.assertEquals(command.getAutoLoseRound(), 3);
        Assert.assertEquals(command.getAutoLoseStamina(), 9);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
