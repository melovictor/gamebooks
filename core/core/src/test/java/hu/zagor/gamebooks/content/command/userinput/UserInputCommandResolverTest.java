package hu.zagor.gamebooks.content.command.userinput;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputTextualResponse;
import hu.zagor.gamebooks.domain.BookInformations;

import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputCommandResolverTest {

    private static final String RESPONSE = "response";
    private static final Integer RESPONSE_TIME = 4753;
    private IMocksControl mockControl;
    private UserInputCommandResolver underTest;
    private UserInputResponse defaultResponse;
    private UserInputResponse specificResponse;
    private UserInputResponse specificFastResponse;
    private UserInputResponse specificMediumResponse;
    private UserInputResponse specificSlowResponse;
    private ParagraphData specificFastResponseData;
    private ParagraphData specificMediumResponseData;
    private ParagraphData specificSlowResponseData;
    private Character character;
    private ParagraphData rootDataElement;
    private ParagraphData defaultResponseData;
    private ParagraphData specificResponseData;
    private CharacterHandler characterHandler;
    private ResolvationData resolvationData;
    private BookInformations info;
    private DefaultUserInteractionHandler interactionHandler;
    private UserInputCommand command;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        defaultResponse = mockControl.createMock(UserInputResponse.class);
        specificResponse = mockControl.createMock(UserInputResponse.class);
        rootDataElement = mockControl.createMock(ParagraphData.class);
        specificFastResponseData = mockControl.createMock(ParagraphData.class);
        specificMediumResponseData = mockControl.createMock(ParagraphData.class);
        specificSlowResponseData = mockControl.createMock(ParagraphData.class);
        specificFastResponse = getUserInputTextualResponse(RESPONSE, 0, 5000, specificFastResponseData);
        specificMediumResponse = getUserInputTextualResponse(RESPONSE, 5001, 10000, specificMediumResponseData);
        specificSlowResponse = getUserInputTextualResponse(RESPONSE, 10001, Integer.MAX_VALUE, specificSlowResponseData);
        defaultResponseData = mockControl.createMock(ParagraphData.class);
        specificResponseData = mockControl.createMock(ParagraphData.class);
        interactionHandler = mockControl.createMock(DefaultUserInteractionHandler.class);
        characterHandler = new CharacterHandler();
        info = new BookInformations(11L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        character = new Character();
        underTest = new UserInputCommandResolver();
    }

    private UserInputResponse getUserInputTextualResponse(final String string, final int min, final int max, final ParagraphData data) {
        final UserInputTextualResponse userInputTextualResponse = new UserInputTextualResponse(string);
        userInputTextualResponse.setMinResponseTime(min);
        userInputTextualResponse.setMaxResponseTime(max);
        userInputTextualResponse.setData(data);
        return userInputTextualResponse;

    }

    @BeforeMethod
    public void setUpMethod() {
        command = new UserInputCommand();
        resolvationData = new ResolvationData(rootDataElement, character, null, info);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveWhenRootDataElementIsNullShouldThrowException() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, new ResolvationData(null, character, null, info));
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, new ResolvationData(rootDataElement, null, null, info));
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveWhenCharacterHandlerIsNullShouldThrowException() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, new ResolvationData(rootDataElement, character, null, null));
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenNoResponsesAreSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN throws exception
    }

    public void testResolveWhenCharacterHasNoUserResponseShouldReturnNull() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(null);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned.getResolveList());
        Assert.assertFalse(returned.isFinished());
    }

    public void testResolveWhenCharacterHasDefaultResponseShouldReturnDataFromDefault() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(RESPONSE_TIME);
        expect(defaultResponse.getMinResponseTime()).andReturn(0);
        expect(defaultResponse.getMaxResponseTime()).andReturn(Integer.MAX_VALUE);
        expect(defaultResponse.isFallback()).andReturn(true);
        expect(defaultResponse.getData()).andReturn(defaultResponseData);
        expect(specificResponse.getMinResponseTime()).andReturn(0);
        expect(specificResponse.getMaxResponseTime()).andReturn(Integer.MAX_VALUE);
        expect(specificResponse.isFallback()).andReturn(false);
        expect(specificResponse.matches(RESPONSE)).andReturn(false);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), defaultResponseData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenCharacterHasSpecificResponseShouldReturnDataFromSpecific() {
        // GIVEN
        command.addResponse(defaultResponse);
        command.addResponse(specificResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(RESPONSE_TIME);
        expect(defaultResponse.getMinResponseTime()).andReturn(0);
        expect(defaultResponse.getMaxResponseTime()).andReturn(Integer.MAX_VALUE);
        expect(defaultResponse.isFallback()).andReturn(true);
        expect(defaultResponse.getData()).andReturn(defaultResponseData);
        expect(specificResponse.getMinResponseTime()).andReturn(0);
        expect(specificResponse.getMaxResponseTime()).andReturn(Integer.MAX_VALUE);
        expect(specificResponse.isFallback()).andReturn(false);
        expect(specificResponse.matches(RESPONSE)).andReturn(true);
        expect(specificResponse.getData()).andReturn(specificResponseData);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), specificResponseData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenCharacterHasSpecificResponseInFastRangeShouldReturnDataFromFast() {
        // GIVEN
        command.addResponse(specificFastResponse);
        command.addResponse(specificMediumResponse);
        command.addResponse(specificSlowResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(1000);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), specificFastResponseData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenCharacterHasSpecificResponseInMediumRangeShouldReturnDataFromMedium() {
        // GIVEN
        command.addResponse(specificFastResponse);
        command.addResponse(specificMediumResponse);
        command.addResponse(specificSlowResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(8000);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), specificMediumResponseData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenCharacterHasSpecificResponseInSlowRangeShouldReturnDataFromSlow() {
        // GIVEN
        command.addResponse(specificFastResponse);
        command.addResponse(specificMediumResponse);
        command.addResponse(specificSlowResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(17000);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertEquals(resolveList.size(), 1);
        Assert.assertSame(resolveList.get(0), specificSlowResponseData);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenCharacterHasSpecificResponseInSlowRangeButNoSlowRangeDataIsAvailableShouldReturnEmptyData() {
        // GIVEN
        command.addResponse(specificFastResponse);
        command.addResponse(specificMediumResponse);
        expect(interactionHandler.getUserInput(character)).andReturn(RESPONSE);
        expect(interactionHandler.getUserInputTime(character)).andReturn(17000);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        final List<ParagraphData> resolveList = returned.getResolveList();
        Assert.assertTrue(resolveList.isEmpty());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
