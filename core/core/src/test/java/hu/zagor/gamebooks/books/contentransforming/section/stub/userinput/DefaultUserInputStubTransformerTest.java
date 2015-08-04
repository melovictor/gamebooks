package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link DefaultUserInputStubTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultUserInputStubTransformerTest {

    private static final String IRRELEVANT_NODE_NAME = "#text";
    private static final String RELEVANT_UNAVAILABLE_NODE_NAME = "gatherItem";
    private static final String RELEVANT_AVAILABLE_NODE_NAME = "label";
    private IMocksControl mockControl;
    private DefaultUserInputStubTransformer underTest;
    private BookParagraphDataTransformer parent;
    private Node node;
    private UserInputCommand command;
    private ChoicePositionCounter positionCounter;
    private NodeList nodeList;
    private List<String> irrelevantNodeNames;
    private Node childNode;
    private Map<String, CommandSubTransformer<UserInputCommand>> responseTransformers;
    private CommandSubTransformer<UserInputCommand> commandSubTransformer;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        command = mockControl.createMock(UserInputCommand.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        nodeList = mockControl.createMock(NodeList.class);
        irrelevantNodeNames = new ArrayList<String>();
        irrelevantNodeNames.add(IRRELEVANT_NODE_NAME);
        childNode = mockControl.createMock(Node.class);
        responseTransformers = new HashMap<String, CommandSubTransformer<UserInputCommand>>();
        commandSubTransformer = mockControl.createMock(CommandSubTransformer.class);
        responseTransformers.put(RELEVANT_AVAILABLE_NODE_NAME, commandSubTransformer);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultUserInputStubTransformer();
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);
        underTest.setResponseTransformers(responseTransformers);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(null, node, command, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, null, command, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenCommandIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, null, positionCounter);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenPositionCounterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, null);
        // THEN throws exception
    }

    public void testTransformWhenNoChildNodesAreAvailableShouldDoNothing() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, positionCounter);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenOneIrrelevantChildNodeIsAvailableShouldDoNothing() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(childNode);
        expect(childNode.getNodeName()).andReturn(IRRELEVANT_NODE_NAME);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, positionCounter);
        // THEN
        Assert.assertTrue(true);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testTransformWhenRelevantChildNodeHasNoStubTransformerShouldThrowException() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(childNode);
        expect(childNode.getNodeName()).andReturn(IRRELEVANT_NODE_NAME);
        expect(nodeList.item(1)).andReturn(childNode);
        expect(childNode.getNodeName()).andReturn(RELEVANT_UNAVAILABLE_NODE_NAME).times(2);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, positionCounter);
        // THEN
        Assert.assertTrue(true);
    }

    public void testTransformWhenRelevantChildNodeHasStubTransformerShouldCallTransformer() {
        // GIVEN
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(childNode);
        expect(childNode.getNodeName()).andReturn(IRRELEVANT_NODE_NAME);
        expect(nodeList.item(1)).andReturn(childNode);
        expect(childNode.getNodeName()).andReturn(RELEVANT_AVAILABLE_NODE_NAME).times(2);
        commandSubTransformer.transform(parent, childNode, command, positionCounter);
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, command, positionCounter);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
