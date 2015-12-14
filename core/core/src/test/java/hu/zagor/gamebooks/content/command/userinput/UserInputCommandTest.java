package hu.zagor.gamebooks.content.command.userinput;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Iterator;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputCommandTest {

    private static final String RESPONSE = "response";
    @MockControl private IMocksControl mockControl;
    @UnderTest private UserInputCommand underTest;
    @Mock private UserInputResponse defaultResponse;
    @Mock private UserInputResponse specificResponse;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetLabelShouldReturnSettedValue() {
        // GIVEN
        underTest.setLabel(RESPONSE);
        mockControl.replay();
        // WHEN
        final String returned = underTest.getLabel();
        // THEN
        Assert.assertSame(returned, RESPONSE);
    }

    public void testGetCommandViewWhenNotOngoingShouldReturnProperlySetCommandViewWithoutHiddenChoices() {
        // GIVEN
        final String label = "user input command label";
        underTest.setLabel(label);
        underTest.setType("text");
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("raw");
        // THEN
        Assert.assertEquals(returned.getViewName(), "rawUserInputCommand");
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.size(), 1);
        Assert.assertTrue(model.containsKey("command"));
        Assert.assertEquals(model.get("command"), underTest);
        Assert.assertEquals(underTest.getType(), "text");
    }

    public void testGetCommandViewWhenOngoingShouldReturnProperlySetCommandViewWithHiddenChoices() {
        // GIVEN
        final String label = "user input command label";
        underTest.setLabel(label);
        underTest.setOngoing(true);
        underTest.setType("number");
        mockControl.replay();
        // WHEN
        final CommandView returned = underTest.getCommandView("raw");
        // THEN
        Assert.assertEquals(returned.getViewName(), "rawUserInputCommand");
        final Map<String, Object> model = returned.getModel();
        Assert.assertEquals(model.size(), 2);
        Assert.assertTrue(model.containsKey("command"));
        Assert.assertEquals(model.get("command"), underTest);
        Assert.assertTrue(model.containsKey("ffChoiceHidden"));
        Assert.assertEquals(model.get("ffChoiceHidden"), true);
        Assert.assertEquals(underTest.getType(), "number");
    }

    public void testCloneShouldReturnClonedObject() throws CloneNotSupportedException {
        // GIVEN
        final String label = "user input command label";
        underTest.setLabel(label);
        underTest.addResponse(defaultResponse);
        underTest.addResponse(specificResponse);
        final UserInputResponse clonedDefaultResponse = mockControl.createMock(UserInputResponse.class);
        final UserInputResponse clonedSpecificResponse = mockControl.createMock(UserInputResponse.class);
        expect(defaultResponse.clone()).andReturn(clonedDefaultResponse);
        expect(specificResponse.clone()).andReturn(clonedSpecificResponse);
        mockControl.replay();
        // WHEN
        final UserInputCommand returned = underTest.clone();
        // THEN
        Assert.assertEquals(returned.getLabel(), label);
        final Iterator<UserInputResponse> responseIterator = returned.getResponses().iterator();
        Assert.assertSame(responseIterator.next(), clonedDefaultResponse);
        Assert.assertSame(responseIterator.next(), clonedSpecificResponse);
    }

    public void testToStringShouldReturnIdentifyingText() {
        // GIVEN
        final String label = "user input command label";
        underTest.setLabel(label);
        mockControl.replay();
        // WHEN
        final String returned = underTest.toString();
        // THEN
        Assert.assertEquals(returned, "UserInputCommand: " + label);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
