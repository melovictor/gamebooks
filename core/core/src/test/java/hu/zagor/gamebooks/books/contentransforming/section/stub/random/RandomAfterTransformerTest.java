package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import static org.easymock.EasyMock.expect;
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
import org.w3c.dom.Node;

/**
 * Unit test for class {@link RandomAfterTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomAfterTransformerTest {

    private RandomAfterTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private Node node;
    private RandomCommand command;
    private ChoicePositionCounter positionCounter;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new RandomAfterTransformer();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        command = new RandomCommand();
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        paragraphData = new ParagraphData();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldParseParagraphAndSetAsAfter() {
        // GIVEN
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertSame(command.getAfter(), paragraphData);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
