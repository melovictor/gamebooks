package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RandomResultTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomResultElseTransformerTest extends AbstractTransformerTest {

    private RandomResultElseTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private RandomCommand command;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        paragraphData = mockControl.createMock(ParagraphData.class);

        underTest = new RandomResultElseTransformer();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        command = new RandomCommand();
    }

    public void testDoTransformShouldParseResultAttributesAndResultItself() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getResultElse(), paragraphData);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
