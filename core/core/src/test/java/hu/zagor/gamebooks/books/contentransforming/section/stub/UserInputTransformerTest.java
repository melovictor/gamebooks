package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputTransformerTest extends AbstractTransformerTest {

    private static final String TYPE = "text";
    @UnderTest private UserInputTransformer underTest;
    @Mock private BookParagraphDataTransformer parent;
    @Mock private ParagraphData data;
    @MockControl private IMocksControl mockControl;
    @Inject private BeanFactory beanFactory;
    @Mock private ChoicePositionCounter positionCounter;
    @Mock private UserInputCommand userInputCommand;
    @Instance private Map<String, CommandSubTransformer<UserInputCommand>> userInputTransformers;
    @Mock private CommandSubTransformer<UserInputCommand> userInputTransformer;

    @BeforeClass
    public void setUpClass() {
    }

    @BeforeMethod
    public void setUpMethod() {
        userInputTransformers.clear();
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

    @Test(expectedExceptions = IllegalStateException.class)
    public void testTransformWhenNoInputTransformersSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN throws exception
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testTransformWhenNoTransformerForTypeExistsShouldThrowException() {
        // GIVEN
        underTest.setUserInputTransformers(userInputTransformers);

        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(UserInputCommand.class)).andReturn(userInputCommand);

        expectAttribute("type", TYPE);
        expectAttribute("min");
        userInputCommand.setMin(Integer.MIN_VALUE);
        expectAttribute("max");
        userInputCommand.setMax(Integer.MAX_VALUE);
        userInputCommand.setType("text");

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN throws exception
    }

    public void testTransformWhenTransformerForTypeExistsShouldCallStubTransformerAndAddCreatedCommandToData() {
        // GIVEN
        underTest.setUserInputTransformers(userInputTransformers);
        userInputTransformers.put(TYPE, userInputTransformer);

        expect(data.getPositionCounter()).andReturn(positionCounter);
        expect(beanFactory.getBean(UserInputCommand.class)).andReturn(userInputCommand);

        expectAttribute("type", TYPE);
        expectAttribute("min", "1");
        userInputCommand.setMin(1);
        expectAttribute("max", "6");
        userInputCommand.setMax(6);
        userInputCommand.setType("text");

        userInputTransformer.transform(parent, node, userInputCommand, positionCounter);
        data.addCommand(userInputCommand);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }
}
