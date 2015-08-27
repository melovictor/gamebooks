package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CoreTextResolvingTest;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RandomCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomCommandResolverBTest extends CoreTextResolvingTest {

    private RandomCommandResolver underTest;
    private IMocksControl mockControl;
    private ResolvationData resolvationData;
    private RandomCommand command;
    private ParagraphData rootData;
    private Character character;
    private BookInformations info;
    private ParagraphData resultElse;
    private RandomResult result;
    private CharacterHandler characterHandler;
    private DefaultUserInteractionHandler interactionHandler;
    private ParagraphData resultData;
    private BeanFactory beanFactory;
    private DiceConfiguration diceConfig;
    private RandomNumberGenerator generator;
    private Logger logger;
    private DiceResultRenderer diceRenderer;
    private ParagraphData clonedData;
    private Locale locale;
    private List<String> messages;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new RandomCommandResolver();
        info = new BookInformations(9);
        rootData = new ParagraphData();
        character = mockControl.createMock(Character.class);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();
        result = new RandomResult();
        result.setMin(2);
        result.setMax(3);
        resultData = mockControl.createMock(ParagraphData.class);
        clonedData = mockControl.createMock(ParagraphData.class);
        result.setParagraphData(resultData);
        resultElse = new ParagraphData();
        characterHandler = new CharacterHandler();
        info.setCharacterHandler(characterHandler);
        interactionHandler = mockControl.createMock(DefaultUserInteractionHandler.class);
        characterHandler.setInteractionHandler(interactionHandler);
        init(mockControl, underTest);
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest.setBeanFactory(beanFactory);
        diceConfig = new DiceConfiguration(1, 1, 6);
        generator = mockControl.createMock(RandomNumberGenerator.class);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "generator", generator);
        diceRenderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(underTest, "diceRenderer", diceRenderer);
        locale = Locale.ENGLISH;
        messages = new ArrayList<>();
    }

    @BeforeMethod
    public void setUpMethod() {
        rootData.setText("<p>Initial content.</p>");
        resultElse.setText("<p>ResultElse data text.</p>");
        command = new RandomCommand();
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveSilentlyWhenNoResultsAreSetShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveSilently(command, resolvationData, messages, locale);
        // THEN throws exception
    }

    public void testResolveSilentlyWhenThereIsResultMatchingTheResultIntervalShouldReturnAppropriateData() throws CloneNotSupportedException {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{2, 2};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 2);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_2_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_2_", 2}, "randomAfter");
        expect(resultData.clone()).andReturn(clonedData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, locale);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), clonedData);
    }

    public void testResolveSilentlyWhenThereIsResultMatchingTheResultIntervalButCloneFailsShouldReturnAppropriateData() throws CloneNotSupportedException {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{2, 2};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 2);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_2_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_2_", 2}, "randomAfter");
        expect(resultData.clone()).andThrow(new CloneNotSupportedException());
        logger.error("Failed to clone object '{}'.", resultData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, locale);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertTrue(returned.isEmpty());
    }

    public void testResolveSilentlyWhenThereIsNoResutlMatchingTheIntervalAndResultElseIsNullShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{6, 6};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 6);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_6_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_6_", 6}, "randomAfter");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, locale);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_6_");
        Assert.assertEquals(command.getDiceResult(), 6);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertTrue(returned.isEmpty());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
