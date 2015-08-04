package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.itemcheck.CheckType;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
import org.easymock.CaptureType;
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
 * Unit test for class {@link ItemCheckTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckTransformerTest extends AbstractTransformerTest {

    private static final String IRRELEVANT_NODE = "#text";
    private static final String ID = "3001";
    private static final String INVALID_TYPE = "bread";
    private static final String VALID_TYPE = "item";
    private static final String UNSUPPORTED_EVENT = "maybeHave";
    private static final String SUPPORTED_EVENT = "have";
    private static final String HAVE_ID = "1";
    private static final String DONT_HAVE_ID = "2";
    private ItemCheckTransformer underTest;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private IMocksControl mockControl;
    private NodeList nodeList;
    private BeanFactory beanFactory;
    private ChoicePositionCounter positionCounter;
    private ItemCheckCommand itemCheckCommand;
    private CommandSubTransformer<ItemCheckCommand> commandStubTransformer;
    private ParagraphData dontHaveParagraph;
    private ParagraphData haveParagraph;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        node = mockControl.createMock(Node.class);
        data = mockControl.createMock(ParagraphData.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        nodeList = mockControl.createMock(NodeList.class);
        commandStubTransformer = mockControl.createMock(CommandSubTransformer.class);
        haveParagraph = mockControl.createMock(ParagraphData.class);
        dontHaveParagraph = mockControl.createMock(ParagraphData.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ItemCheckTransformer();
        underTest.setBeanFactory(beanFactory);

        final List<String> irrelevantNodeNames = new ArrayList<String>();
        irrelevantNodeNames.add(IRRELEVANT_NODE);
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);

        final Map<String, CommandSubTransformer<ItemCheckCommand>> stubs = new HashMap<String, CommandSubTransformer<ItemCheckCommand>>();
        stubs.put(SUPPORTED_EVENT, commandStubTransformer);
        underTest.setStubs(stubs);

        itemCheckCommand = new ItemCheckCommand();
        itemCheckCommand.setCheckType(CheckType.item);
        itemCheckCommand.setId(ID);

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenParentIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(null, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenNodeIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, null, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenDataIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTransformWhenCheckTypeIsInvalidShouldThrowException() {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(ItemCheckCommand.class)).andReturn(itemCheckCommand);

        expectAttribute("type", INVALID_TYPE);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testTransformWhenEventTypeIsNotSupportedByStubTransformersShouldThrowException() {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(ItemCheckCommand.class)).andReturn(itemCheckCommand);

        expectAttribute("type", VALID_TYPE);
        expectAttribute("id", ID);
        expectAttribute("amount");
        expectAttribute("have");
        expectAttribute("dontHave");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(1);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn(UNSUPPORTED_EVENT).times(2);

        Whitebox.setInternalState(underTest, "irrelevantNodeNames", new ArrayList<String>());
        underTest.setStubs(new HashMap<String, CommandSubTransformer<ItemCheckCommand>>());
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN throws exception
    }

    public void testTransformWhenEventTypeIsSupportedByStubTransformersShouldCallStubTransformer() {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(ItemCheckCommand.class)).andReturn(itemCheckCommand);

        expectAttribute("type", VALID_TYPE);
        expectAttribute("id", ID);
        expectAttribute("amount", "2");
        expectAttribute("have");
        expectAttribute("dontHave");

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(2);
        expect(nodeList.item(0)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn(IRRELEVANT_NODE);
        expect(nodeList.item(1)).andReturn(nodeValue);
        expect(nodeValue.getNodeName()).andReturn(SUPPORTED_EVENT).times(2);
        commandStubTransformer.transform(parent, nodeValue, itemCheckCommand, positionCounter);

        data.addCommand(itemCheckCommand);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
    }

    public void testTransformWhenOnlyExistsHaveDontHaveAttributesShouldNotCallStubTransformer() {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(ItemCheckCommand.class)).andReturn(itemCheckCommand);

        expectAttribute("type", VALID_TYPE);
        expectAttribute("id", ID);
        expectAttribute("amount", "3");

        final Capture<Choice> captured = new Capture<>(CaptureType.ALL);
        expectAttribute("have", HAVE_ID, haveParagraph, captured);
        expectAttribute("dontHave", DONT_HAVE_ID, dontHaveParagraph, captured);

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);

        data.addCommand(itemCheckCommand);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        final List<Choice> capturedValues = captured.getValues();
        final Choice haveChoice = capturedValues.get(0);
        final Choice dontHaveChoice = capturedValues.get(1);
        Assert.assertEquals(haveChoice.getId(), HAVE_ID);
        Assert.assertEquals(haveChoice.getPosition(), Integer.valueOf(HAVE_ID).intValue());
        Assert.assertNull(haveChoice.getText());
        Assert.assertEquals(dontHaveChoice.getId(), DONT_HAVE_ID);
        Assert.assertEquals(dontHaveChoice.getPosition(), Integer.valueOf(DONT_HAVE_ID).intValue());
        Assert.assertNull(dontHaveChoice.getText());
        Assert.assertSame(itemCheckCommand.getHave(), haveParagraph);
        Assert.assertSame(itemCheckCommand.getDontHave(), dontHaveParagraph);
        Assert.assertSame(itemCheckCommand.getHaveEquipped(), haveParagraph);
        Assert.assertSame(itemCheckCommand.getAmount(), 3);
    }

    private void expectAttribute(final String attributeName, final String id, final ParagraphData paragraph, final Capture<Choice> captured) {
        expectAttribute(attributeName, id);
        expect(positionCounter.updateAndGetPosition(null)).andReturn(Integer.valueOf(id));
        expect(parent.getParagraphData()).andReturn(paragraph);
        paragraph.addChoice(capture(captured));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
