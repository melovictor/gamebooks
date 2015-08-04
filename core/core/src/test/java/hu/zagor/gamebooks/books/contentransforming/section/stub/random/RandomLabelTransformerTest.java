package hu.zagor.gamebooks.books.contentransforming.section.stub.random;

import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
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
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link RandomLabelTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomLabelTransformerTest extends AbstractTransformerTest {

    private static final String LABEL = "label";
    private RandomLabelTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;
    private RandomCommand command;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);

        underTest = new RandomLabelTransformer();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        command = new RandomCommand();
    }

    public void testDoTransformShouldFillLabel() {
        // GIVEN
        expectContent(LABEL);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, command, positionCounter);
        // THEN
        Assert.assertEquals(command.getLabel(), LABEL);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
