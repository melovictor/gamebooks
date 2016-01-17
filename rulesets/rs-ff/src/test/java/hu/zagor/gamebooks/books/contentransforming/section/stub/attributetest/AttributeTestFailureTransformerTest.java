package hu.zagor.gamebooks.books.contentransforming.section.stub.attributetest;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AttributeTestFailureTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestFailureTransformerTest extends AbstractTransformerTest {
    @UnderTest private AttributeTestFailureTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private ChoicePositionCounter positionCounter;
    @Mock private BookParagraphDataTransformer parent;
    @Instance private AttributeTestCommand command;
    @Mock private FfParagraphData data;

    public void testDoTransformShouldTransformFailure() {
        // GIVEN
        expectAttribute("rolled");
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(data);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getFailure().get(0).getData(), data);
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
