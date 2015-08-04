package hu.zagor.gamebooks.books.contentransforming.section.stub.attributetest;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link AttributeTestSuccessTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestSuccessTransformerTest extends AbstractTransformerTest {

    private AttributeTestSuccessTransformer underTest;
    private IMocksControl mockControl;
    private ChoicePositionCounter positionCounter;
    private BookParagraphDataTransformer parent;
    private AttributeTestCommand command;
    private FfParagraphData data;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        node = mockControl.createMock(Node.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        command = new AttributeTestCommand();
        data = mockControl.createMock(FfParagraphData.class);

        underTest = new AttributeTestSuccessTransformer();
    }

    public void testDoTransformShouldTransformFailure() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(data);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getSuccess(), data);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
