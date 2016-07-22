package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.itemcheck.ItemCheckCommand;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

/**
 * Unit test for class {@link ItemCheckTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckTransformerTest extends AbstractTransformerTest {

    private static final String IRRELEVANT_NODE = "#text";
    private static final String ID = "3001";
    private static final String VALID_TYPE = "item";
    private static final String UNSUPPORTED_EVENT = "maybeHave";
    private static final String SUPPORTED_EVENT = "have";
    private static final String HAVE_ID = "1";
    private static final String DONT_HAVE_ID = "2";
    @UnderTest private ItemCheckTransformer underTest;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private ParagraphData data;
    @MockControl private IMocksControl mockControl;
    private NodeList nodeList;
    @Inject private BeanFactory beanFactory;
    @Mock private ChoicePositionCounter positionCounter;
    private ItemCheckCommand itemCheckCommand;
    @Mock private CommandSubTransformer<ItemCheckCommand> commandStubTransformer;
    @Mock private ParagraphData dontHaveParagraph;
    @Mock private ParagraphData haveParagraph;

    @BeforeMethod
    public void setUpMethod() {
        final List<String> irrelevantNodeNames = new ArrayList<String>();
        irrelevantNodeNames.add(IRRELEVANT_NODE);
        Whitebox.setInternalState(underTest, "irrelevantNodeNames", irrelevantNodeNames);

        final Map<String, CommandSubTransformer<ItemCheckCommand>> stubs = new HashMap<String, CommandSubTransformer<ItemCheckCommand>>();
        stubs.put(SUPPORTED_EVENT, commandStubTransformer);
        underTest.setStubs(stubs);

        itemCheckCommand = new ItemCheckCommand();
        itemCheckCommand.setCheckType("item");
        itemCheckCommand.setId(ID);
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

        final Capture<Choice> captured = newCapture(CaptureType.ALL);
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
}
