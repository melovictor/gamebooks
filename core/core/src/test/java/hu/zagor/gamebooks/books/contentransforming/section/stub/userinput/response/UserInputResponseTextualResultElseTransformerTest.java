package hu.zagor.gamebooks.books.contentransforming.section.stub.userinput.response;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.AbstractTransformerTest;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphDataTransformer;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.command.userinput.UserInputCommand;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputTextualResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputResponseTextualResultElseTransformer}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputResponseTextualResultElseTransformerTest extends AbstractTransformerTest {

    private UserInputResponseTextualResultElseTransformer underTest;
    private IMocksControl mockControl;
    private BookParagraphDataTransformer parent;
    private UserInputCommand command;
    private ChoicePositionCounter positionCounter;
    private UserInputTextualResponse response;
    private BeanFactory beanFactory;
    private ParagraphData paragraphData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        init(mockControl);
        parent = mockControl.createMock(BookParagraphDataTransformer.class);
        command = mockControl.createMock(UserInputCommand.class);
        positionCounter = mockControl.createMock(ChoicePositionCounter.class);
        response = mockControl.createMock(UserInputTextualResponse.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        paragraphData = mockControl.createMock(ParagraphData.class);
        underTest = new UserInputResponseTextualResultElseTransformer();
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
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

    public void testTransformShouldParseTextAndCreateResponseIntoParent() {
        // GIVEN
        expect(beanFactory.getBean(UserInputTextualResponse.class)).andReturn(response);
        expect(parent.parseParagraphData(positionCounter, node)).andReturn(paragraphData);
        response.setData(paragraphData);
        command.addResponse(response);
        expectAttribute("minResponse");
        response.setMinResponseTime(0);
        expectAttribute("maxResponse");
        response.setMaxResponseTime(Integer.MAX_VALUE);
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
