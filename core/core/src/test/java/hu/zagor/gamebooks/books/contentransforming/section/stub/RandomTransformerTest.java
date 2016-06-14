package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link RandomTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomTransformerTest extends AbstractTransformerTest {

    private RandomTransformer underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private RandomCommand command;
    private Map<String, CommandSubTransformer<RandomCommand>> randomTransformers;
    private CommandSubTransformer<RandomCommand> resultTransformer;
    private CommandSubTransformer<RandomCommand> resultElseTransformer;
    private ChoicePositionCounter positionCounter;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        beanFactory = mockControl.createMock(BeanFactory.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        data = mockControl.createMock(ParagraphData.class);
        node = mockControl.createMock(Node.class);
        nodeValue = mockControl.createMock(Node.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeList = mockControl.createMock(NodeList.class);
        resultTransformer = mockControl.createMock(CommandSubTransformer.class);
        resultElseTransformer = mockControl.createMock(CommandSubTransformer.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);

        underTest = new RandomTransformer();
        underTest.setBeanFactory(beanFactory);
        randomTransformers = new HashMap<String, CommandSubTransformer<RandomCommand>>();
        randomTransformers.put("result", resultTransformer);
        randomTransformers.put("resultElse", resultElseTransformer);
        final List<Object> irrelevantNodeNames = new ArrayList<>();
        irrelevantNodeNames.add("#text");
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest.setRandomTransformers(randomTransformers);
        command = new RandomCommand();
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testDoTransformWhenAttributeTestTransformersAreMissingShouldThrowException() {
        // GIVEN
        underTest.setRandomTransformers(null);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDoTransformWhenTransformerIsMissingShouldThrowException() {
        // GIVEN
        expect(beanFactory.getBean(RandomCommand.class)).andReturn(command);
        expectAttribute("diceConfig", "1d6");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("something").times(2);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    public void testDoTransformWhenTransformersArePresentShouldCallThemAndCreateCommandBean() {
        // GIVEN
        expect(beanFactory.getBean(RandomCommand.class)).andReturn(command);
        expectAttribute("diceConfig", "1d6");
        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(3);
        expect(data.getPositionCounter()).andReturn(positionCounter);

        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("result").times(2);
        resultTransformer.transform(parent, nodeValue, command, positionCounter);

        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("resultElse").times(2);
        resultElseTransformer.transform(parent, nodeValue, command, positionCounter);

        expect(nodeList.item(2)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");

        data.addCommand(command);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(command.getDiceConfig(), "dice1d6");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
