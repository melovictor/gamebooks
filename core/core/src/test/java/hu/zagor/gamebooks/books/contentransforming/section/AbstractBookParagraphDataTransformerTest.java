package hu.zagor.gamebooks.books.contentransforming.section;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentransforming.section.stub.StubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link DefaultBookParagraphTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractBookParagraphDataTransformerTest {

    private TestTransformer underTest;
    private IMocksControl mockControl;
    private ParagraphData paragraphData;
    private BeanFactory beanFactory;
    private Map<String, StubTransformer> stubTransformers;
    private List<String> irrelevantNodeNames;
    private NodeList nodeList;
    private ChoicePositionCounter positionCounter;
    private Node node;
    private Node nodeValue;
    private StubTransformer stubTransformer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        paragraphData = mockControl.createMock(ParagraphData.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        nodeList = mockControl.createMock(NodeList.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        node = mockControl.createMock(Node.class);
        nodeValue = mockControl.createMock(Node.class);
        stubTransformer = mockControl.createMock(StubTransformer.class);

        irrelevantNodeNames = new ArrayList<>();
        irrelevantNodeNames.add("#text");
        stubTransformers = new HashMap<>();
        stubTransformers.put("text", stubTransformer);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new TestTransformer();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);
        underTest.setStubTransformers(stubTransformers);

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseParagraphDataWhenPositionCounterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.parseParagraphData(null, node).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseParagraphDataWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.parseParagraphData(positionCounter, null).getClass();
        // THEN throws exception
    }

    public void testParseParagraphDataWhenNoChildNodesArePresentShouldReturnUnparsedData() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.parseParagraphData(positionCounter, node);
        // THEN
        Assert.assertSame(returned, paragraphData);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testParseParagraphWhenChildNodesWithoutProperTransformersExistsShouldThrowException() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("add").times(2);
        mockControl.replay();
        // WHEN
        underTest.parseParagraphData(positionCounter, node);
        // THEN throws exception
    }

    public void testParseParagraphWhenChildNodesWithProperTransformersExistsShouldCallStubTransformer() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("text").times(2);
        stubTransformer.transform(underTest, nodeValue, paragraphData);
        mockControl.replay();
        // WHEN
        underTest.parseParagraphData(positionCounter, node);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestTransformer extends AbstractBookParagraphDataTransformer {

        @Override
        protected ParagraphData getParagraphData(final ChoicePositionCounter positionCounter) {
            return paragraphData;
        }

        @Override
        public ParagraphData getParagraphData() {
            return paragraphData;
        }

    }

}
