package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestSuccessType;

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
 * Unit test for class {@link AttributeTestTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestTransformerTest extends AbstractTransformerTest {

    private AttributeTestTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private FfParagraphData data;
    private BeanFactory beanFactory;
    private Map<String, CommandSubTransformer<AttributeTestCommand>> attributeTestTransformers;
    private CommandSubTransformer<AttributeTestCommand> successTransformer;
    private CommandSubTransformer<AttributeTestCommand> failureTransformer;
    private AttributeTestCommand command;
    private ChoicePositionCounter positionCounter;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        nodeList = mockControl.createMock(NodeList.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        data = mockControl.createMock(FfParagraphData.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        successTransformer = mockControl.createMock(CommandSubTransformer.class);
        failureTransformer = mockControl.createMock(CommandSubTransformer.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);

        attributeTestTransformers = new HashMap<>();
        attributeTestTransformers.put("success", successTransformer);
        attributeTestTransformers.put("failure", failureTransformer);

        underTest = new AttributeTestTransformer();
        underTest.setBeanFactory(beanFactory);
        final List<String> irrelevantNodeNames = new ArrayList<>();
        irrelevantNodeNames.add("#text");
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest.setAttributeTestTransformers(attributeTestTransformers);

        command = new AttributeTestCommand();

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testDoTransformWhenAttributeTestTransformersIsNullShouldThrowException() {
        // GIVEN
        underTest.setAttributeTestTransformers(null);
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testDoTransformWhenChildNodeDoesNotHaveTransformerShouldThrowException() {
        // GIVEN
        expect(beanFactory.getBean(AttributeTestCommand.class)).andReturn(command);
        expectAttribute("against", "skill");
        expectAttribute("diceConfig");
        expectAttribute("add");
        expectAttribute("success");

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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDoTransformWhenSuccessTypeIsInvalidShouldThrowException() {
        // GIVEN
        expect(beanFactory.getBean(AttributeTestCommand.class)).andReturn(command);
        expectAttribute("against", "skill");
        expectAttribute("diceConfig");
        expectAttribute("add");
        expectAttribute("success", "badName");

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN throws exception
    }

    public void testDoTransformWhenInputDataIsCorrectAndDefaultShouldCallTransformersAndCreateDefaultBean() {
        // GIVEN
        expect(beanFactory.getBean(AttributeTestCommand.class)).andReturn(command);
        expectAttribute("against", "skill");
        expectAttribute("diceConfig");
        expectAttribute("add");
        expectAttribute("success");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(3);

        expect(data.getPositionCounter()).andReturn(positionCounter);

        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("#text");

        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("success").times(2);
        successTransformer.transform(parent, nodeValue, command, positionCounter);

        expect(nodeList.item(2)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("failure").times(2);
        failureTransformer.transform(parent, nodeValue, command, positionCounter);

        data.addCommand(command);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(command.getConfigurationName(), "dice2d6");
        Assert.assertEquals(command.getAdd(), 0);
        Assert.assertNull(command.getSuccessType());
    }

    public void testDoTransformWhenInputDataIsCorrectAndNotDefaultShouldCallTransformersAndCreateSpecificBean() {
        // GIVEN
        expect(beanFactory.getBean(AttributeTestCommand.class)).andReturn(command);
        expectAttribute("against", "skill");
        expectAttribute("diceConfig", "4d6");
        expectAttribute("add");
        expectAttribute("success", "lower");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);

        expect(data.getPositionCounter()).andReturn(positionCounter);

        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("success").times(2);
        successTransformer.transform(parent, nodeValue, command, positionCounter);

        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn("failure").times(2);
        failureTransformer.transform(parent, nodeValue, command, positionCounter);

        data.addCommand(command);

        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        Assert.assertEquals(command.getConfigurationName(), "dice4d6");
        Assert.assertEquals(command.getAdd(), 0);
        Assert.assertSame(command.getSuccessType(), AttributeTestSuccessType.lower);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
