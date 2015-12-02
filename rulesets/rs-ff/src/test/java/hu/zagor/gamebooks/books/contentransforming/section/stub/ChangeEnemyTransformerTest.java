package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChangeEnemyTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeEnemyTransformerTest extends AbstractTransformerTest {
    @UnderTest private ChangeEnemyTransformer underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private FfParagraphData data;
    @Mock private BookParagraphDataTransformer parent;

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
        final Capture<ChangeEnemyCommand> capture = newCapture();
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
        final Capture<ChangeEnemyCommand> capture = newCapture();
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
