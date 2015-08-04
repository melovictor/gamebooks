package hu.zagor.gamebooks.books.contentransforming.section.stub;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.CommandSubTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Unit test for class {@link UserInputTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputTransformerTest extends AbstractTransformerTest {

    private static final String TYPE = "text";
    private UserInputTransformer underTest;
    private BookParagraphDataTransformer parent;
    private ParagraphData data;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private ChoicePositionCounter positionCounter;
    private UserInputCommand userInputCommand;
    private Map<String, CommandSubTransformer<UserInputCommand>> userInputTransformers;
    private CommandSubTransformer<UserInputCommand> userInputTransformer;

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
        userInputCommand = mockControl.createMock(UserInputCommand.class);
        userInputTransformer = mockControl.createMock(CommandSubTransformer.class);
        userInputTransformers = new HashMap<String, CommandSubTransformer<UserInputCommand>>();
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new UserInputTransformer();
        underTest.setBeanFactory(beanFactory);
        userInputTransformers.clear();
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

        userInputTransformer.transform(parent, node, userInputCommand, positionCounter);
        data.addCommand(userInputCommand);

        mockControl.replay();
        // WHEN
        underTest.transform(parent, node, data);
        // THEN
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
