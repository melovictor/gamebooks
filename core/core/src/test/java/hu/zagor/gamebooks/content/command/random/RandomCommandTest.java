package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandView;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RandomCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomCommandTest {

    private IMocksControl mockControl;
    private RandomCommand underTest;
    private ParagraphData resultElse;
    private ParagraphData after;
    private RandomResult resultA;
    private RandomResult resultB;
    private ParagraphData resultElseClone;
    private ParagraphData afterClone;
    private RandomResult resultAClone;
    private RandomResult resultBClone;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        resultElse = mockControl.createMock(ParagraphData.class);
        after = mockControl.createMock(ParagraphData.class);
        resultA = mockControl.createMock(RandomResult.class);
        resultB = mockControl.createMock(RandomResult.class);
        resultElseClone = mockControl.createMock(ParagraphData.class);
        afterClone = mockControl.createMock(ParagraphData.class);
        resultAClone = mockControl.createMock(RandomResult.class);
        resultBClone = mockControl.createMock(RandomResult.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new RandomCommand();
        mockControl.reset();
    }

    public void testGetValidMoveShouldReturnRandom() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertEquals(returned, "random");
    }

    public void testGetCommandViewWhenNoDiceResultIsAvailableShouldHideChoices() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("raw");
        // THEN
        Assert.assertEquals(returned.getViewName(), "rawRandom");
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.get("random"), underTest);
        Assert.assertEquals(model.get("choiceHidden"), true);
    }

    public void testGetCommandViewWhenResultIsAvailableShouldNotHideChoices() {
        // GIVEN
        underTest.setDiceResults(new int[]{7, 3, 4});
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("raw");
        // THEN
        Assert.assertEquals(returned.getViewName(), "rawRandom");
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.get("random"), underTest);
        Assert.assertEquals(model.get("choiceHidden"), null);
    }

    public void testCloneWhenAllFieldsAreSetShouldCloneMembers() throws CloneNotSupportedException {
        // GIVEN
        underTest.setAfter(after);
        underTest.setResultElse(resultElse);
        underTest.addResult(resultA);
        underTest.addResult(resultB);
        expect(resultElse.clone()).andReturn(resultElseClone);
        expect(after.clone()).andReturn(afterClone);
        expect(resultA.clone()).andReturn(resultAClone);
        expect(resultB.clone()).andReturn(resultBClone);
        mockControl.replay();
        // WHEN
        final RandomCommand returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getAfter(), afterClone);
        Assert.assertSame(returned.getResults().get(0), resultAClone);
        Assert.assertSame(returned.getResults().get(1), resultBClone);
        Assert.assertSame(returned.getResultElse(), resultElseClone);
    }

    public void testCloneWhenNoAfterAndElseAreSetShouldCloneMembers() throws CloneNotSupportedException {
        // GIVEN
        underTest.addResult(resultA);
        underTest.addResult(resultB);
        expect(resultA.clone()).andReturn(resultAClone);
        expect(resultB.clone()).andReturn(resultBClone);
        mockControl.replay();
        // WHEN
        final RandomCommand returned = underTest.clone();
        // THEN
        Assert.assertNull(returned.getAfter());
        Assert.assertSame(returned.getResults().get(0), resultAClone);
        Assert.assertSame(returned.getResults().get(1), resultBClone);
        Assert.assertNull(returned.getResultElse());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
