package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.AbstractCommandTest;
import hu.zagor.gamebooks.content.command.CommandView;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AttributeTestCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestCommandTest extends AbstractCommandTest {

    private AttributeTestCommand underTest;
    private IMocksControl mockControl;
    private FfParagraphData success;
    private FfParagraphData failure;
    private FfParagraphData successCloned;
    private FfParagraphData failureCloned;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        success = mockControl.createMock(FfParagraphData.class);
        failure = mockControl.createMock(FfParagraphData.class);
        successCloned = mockControl.createMock(FfParagraphData.class);
        failureCloned = mockControl.createMock(FfParagraphData.class);

        underTest = new AttributeTestCommand();
        underTest.setSuccess(success);
        underTest.setFailure(failure);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetCommandViewShouldReturnCommandViewWithModelContainingSelf() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("ff");
        // THEN
        Assert.assertSame(returned.getModel().get("attribTest"), underTest);
        Assert.assertEquals(returned.getViewName(), "ffAttributeTest");
    }

    public void testCloneShouldReturnClonedObjectWithClonedSuccessAndFailure() throws CloneNotSupportedException {
        // GIVEN
        expectTc(success, successCloned);
        expectTc(failure, failureCloned);
        mockControl.replay();
        // WHEN
        final AttributeTestCommand returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getSuccess(), successCloned);
        Assert.assertSame(returned.getFailure(), failureCloned);
    }

    public void testGetValidMoveShouldReturnValidMoveCode() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getValidMove();
        // THEN
        Assert.assertEquals(returned, "attributeTest");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
