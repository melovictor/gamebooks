package hu.zagor.gamebooks.books.contentransforming.section.stub.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FightWinRoundTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class FightWinRoundTransformerTest extends AbstractTransformerTest {

    private FightWinRoundTransformer underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private BookParagraphDataTransformer parent;
    private FightCommand command;
    private ChoicePositionCounter positionCounter;
    private RoundEvent roundEvent;
    private FfParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new FightWinRoundTransformer();
        init(mockControl);
        beanFactory = mockControl.createMock(BeanFactory.class);
        Whitebox.setInternalState(underTest, "beanFactory", beanFactory);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        command = new FightCommand();
        roundEvent = new RoundEvent();
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        paragraphData = new FfParagraphData();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoRoundResultSpecificTransformShouldSetRoundResultToWin() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.doRoundResultSpecificTransform(roundEvent);
        // THEN
        Assert.assertEquals(roundEvent.getRoundResult(), FightRoundResult.WIN);
    }

    public void testDoTransformShouldParseDataAndFillRoundEvent() {
        // GIVEN
        expect(beanFactory.getBean(RoundEvent.class)).andReturn(roundEvent);
        expectAttribute("totalCount");
        expectAttribute("subsequentCount", "3");
        expectAttribute("enemy", "30");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getRoundEvents().get(0), roundEvent);
        Assert.assertEquals(roundEvent.getRoundResult(), FightRoundResult.WIN);
        Assert.assertSame(roundEvent.getParagraphData(), paragraphData);
        Assert.assertEquals(roundEvent.getTotalCount(), -1);
        Assert.assertEquals(roundEvent.getSubsequentCount(), 3);
        Assert.assertEquals(roundEvent.getEnemyId(), "30");

    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
