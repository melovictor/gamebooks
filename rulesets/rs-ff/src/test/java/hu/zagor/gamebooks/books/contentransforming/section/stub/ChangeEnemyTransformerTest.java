package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link ChangeEnemyTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeEnemyTransformerTest extends AbstractTransformerTest {

    private ChangeEnemyTransformer underTest;
    private IMocksControl mockControl;
    private FfParagraphData data;
    private BookParagraphDataTransformer parent;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        node = mockControl.createMock(Node.class);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        data = mockControl.createMock(FfParagraphData.class);
        nodeMap = mockControl.createMock(NamedNodeMap.class);
        nodeValue = mockControl.createMock(Node.class);

        underTest = new ChangeEnemyTransformer();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoTransformWhenSettingStaminaShouldCreateChangeEnemyCommand() {
        // GIVEN
        expectAttribute("id", "29a");
        expectAttribute("attribute", "stamina");
        expectAttribute("set", "9");
        expectAttribute("change");
        final Capture<ChangeEnemyCommand> capture = new Capture<>();
        data.addEnemyModifyAttributes(capture(capture));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final ChangeEnemyCommand command = capture.getValue();
        Assert.assertEquals(command.getId(), "29a");
        Assert.assertEquals(command.getAttribute(), "stamina");
        Assert.assertEquals(command.getNewValue(), Integer.valueOf(9));
        Assert.assertNull(command.getChangeValue());
    }

    public void testDoTransformWhenChangingStaminaShouldCreateChangeEnemyCommand() {
        // GIVEN
        expectAttribute("id", "29a");
        expectAttribute("attribute", "stamina");
        expectAttribute("set");
        expectAttribute("change", "-2");
        final Capture<ChangeEnemyCommand> capture = new Capture<>();
        data.addEnemyModifyAttributes(capture(capture));
        mockControl.replay();
        // WHEN
        underTest.doTransform(parent, node, data);
        // THEN
        final ChangeEnemyCommand command = capture.getValue();
        Assert.assertEquals(command.getId(), "29a");
        Assert.assertEquals(command.getAttribute(), "stamina");
        Assert.assertNull(command.getNewValue());
        Assert.assertEquals(command.getChangeValue(), Integer.valueOf(-2));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
