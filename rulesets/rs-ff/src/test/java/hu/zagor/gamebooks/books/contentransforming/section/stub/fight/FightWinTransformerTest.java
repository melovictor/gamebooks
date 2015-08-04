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
 * Unit test for class {@link FightWinTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightWinTransformerTest extends AbstractTransformerTest {

    private FightWinTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private FightCommand command;
    private ChoicePositionCounter positionCounter;
    private FfParagraphData win;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightWinTransformer();
        win = mockControl.createMock(FfParagraphData.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        command = new FightCommand();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        init(mockControl);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldSetWin() {
        // GIVEN
        expectAttribute("min");
        expectAttribute("max");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(win);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertEquals(command.getWin().get(0).getMin(), 0);
        Assert.assertEquals(command.getWin().get(0).getMax(), Integer.MAX_VALUE);
        Assert.assertSame(command.getWin().get(0).getParagraphData(), win);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
