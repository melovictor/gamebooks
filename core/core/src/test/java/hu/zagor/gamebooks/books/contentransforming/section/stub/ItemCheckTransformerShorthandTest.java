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

/**
 * Unit test for class {@link ItemCheckTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckTransformerShorthandTest extends AbstractTransformerTest {

    private static final String IRRELEVANT_NODE = "#text";
    private static final String ID = "3001";
    private static final String VALID_TYPE = "item";
    private static final String SUPPORTED_EVENT = "have";
    private static final String HAVE_ID = "1";
    private static final String DONT_HAVE_ID = "2";
    @UnderTest private ItemCheckTransformer underTest;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private ParagraphData data;
    @MockControl private IMocksControl mockControl;
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

    public void testTransformWhenShorthandHaveDontHaveIsPresentAndHaveEquippedIsNotYetSetShouldCreateShorthandParagraphData() {
        // GIVEN
        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(ItemCheckCommand.class)).andReturn(itemCheckCommand);

        expectAttribute("type", VALID_TYPE);
        expectAttribute("id", ID);
        expectAttribute("amount");

        final Capture<Choice> captured = newCapture(CaptureType.ALL);
        expectAttribute("have", HAVE_ID);
        expect(positionCounter.updateAndGetPosition(null)).andReturn(10);
        expect(parent.getParagraphData()).andReturn(haveParagraph);
        haveParagraph.addChoice(capture(captured));

        expectAttribute("dontHave", DONT_HAVE_ID);
        expect(positionCounter.updateAndGetPosition(null)).andReturn(11);
        expect(parent.getParagraphData()).andReturn(dontHaveParagraph);
        dontHaveParagraph.addChoice(capture(captured));

        expect(node.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);

        data.addCommand(itemCheckCommand);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        final List<Choice> choices = captured.getValues();
        checkChoice(choices.get(0), HAVE_ID, 10);
        checkChoice(choices.get(1), DONT_HAVE_ID, 11);

        Assert.assertSame(itemCheckCommand.getHave(), haveParagraph);
        Assert.assertSame(itemCheckCommand.getHaveEquipped(), haveParagraph);
        Assert.assertSame(itemCheckCommand.getDontHave(), dontHaveParagraph);
    }

    private void checkChoice(final Choice choice, final String id, final int position) {
        Assert.assertEquals(choice.getId(), id);
        Assert.assertEquals(choice.getPosition(), position);
        Assert.assertEquals(choice.getText(), null);
    }
}
