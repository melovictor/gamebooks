package hu.zagor.gamebooks.books.contentransforming.section.stub.attributetest;

import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
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
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link AttributeTestLabelTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestLabelTransformerTest extends AbstractTransformerTest {

    private static final String LABEL = "label";
    private AttributeTestLabelTransformer underTest;
    private IMocksControl mockControl;
    private AttributeTestCommand command;
    private BookParagraphDataTransformer parent;
    private ChoicePositionCounter positionCounter;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        node = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        nodeValue = mockControl.createMock(Node.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        command = new AttributeTestCommand();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);

        underTest = new AttributeTestLabelTransformer();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformShouldTransformLabel() {
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
