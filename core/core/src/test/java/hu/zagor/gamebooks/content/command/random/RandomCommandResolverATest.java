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

import java.util.List;

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
public class RandomCommandResolverATest extends CoreTextResolvingTest {

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
    private ParagraphData afterData;
    private RandomResult overlappingResult;
    private ParagraphData overlappingData;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new RandomCommandResolver();
        info = new BookInformations(9);
        rootData = new ParagraphData();
        character = mockControl.createMock(Character.class);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();
        overlappingResult = new RandomResult();
        overlappingResult.setMin(2);
        overlappingResult.setMax(2);
        overlappingData = new ParagraphData();
        overlappingData.setText("<p>Overlapping text.</p>");
        overlappingResult.setParagraphData(overlappingData);
        result = new RandomResult();
        result.setMin(2);
        result.setMax(3);
        resultData = new ParagraphData();
        afterData = new ParagraphData();
        result.setParagraphData(resultData);
        resultElse = new ParagraphData();
        resultData.setBeanFactory(beanFactory);
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
    }

    @BeforeMethod
    public void setUpMethod() {
        rootData.setText("<p>Initial content.</p>");
        resultData.setText("<p>Result data text.</p>");
        afterData.setText("<p>After data text.</p>");
        resultElse.setText("<p>ResultElse data text.</p>");
        command = new RandomCommand();
        mockControl.reset();
        command.setAfter(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDoResolveWhenResolvationDataIsNullShouldThrowException() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, null);
        // THEN throws exception
    }

    public void testDoResolveWhenThereIsNoResultYetShouldDisplayRequest() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(false);
        interactionHandler.setRandomResult(character);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p><p>Throw a die.</p>");
    }

    public void testDoResolveWhenThereIsNoResultAndLableShouldCreateLabelAndDisplayRequest() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setDiceConfig("dice1d6");
        expectLocale();
        expectTextWoLocale("page.raw.label.random.dice1d6", null, "Throw a die.");
        expect(interactionHandler.hasRandomResult(character)).andReturn(false);
        interactionHandler.setRandomResult(character);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p><p>Throw a die.</p>");
    }

    public void testDoResolveWhenThereIsResultMatchingTheResultIntervalShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{2, 2};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 2);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_2_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_2_", 2}, "Thrown value: _2_. Total: 2.");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), resultData);
        Assert.assertEquals(returned.get(0).getText(), "<p>Thrown value: _2_. Total: 2.</p><p>Result data text.</p>");
    }

    public void testDoResolveWhenThereIsNoResultMatchingTheResultIntervalShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{1, 1};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 1);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_1_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_1_", 1}, "Thrown value: _1_. Total: 1.");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_1_");
        Assert.assertEquals(command.getDiceResult(), 1);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), resultElse);
        Assert.assertEquals(returned.get(0).getText(), "<p>Thrown value: _1_. Total: 1.</p><p>ResultElse data text.</p>");
    }

    public void testDoResolveWhenThereIsNoResultMatchingTheResultIntervalButHasAfterShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        command.setAfter(afterData);
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{6, 6};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 6);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_6_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_6_", 6}, "Thrown value: _6_. Total: 6.");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_6_");
        Assert.assertEquals(command.getDiceResult(), 6);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), resultElse);
        Assert.assertSame(returned.get(1), afterData);
        Assert.assertEquals(returned.get(0).getText(), "<p>Thrown value: _6_. Total: 6.</p><p>ResultElse data text.</p>");
    }

    public void testDoResolveWhenThereIsNoResultMatchingTheResultIntervalAndNoResultElseButHasAfterShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.getResults().add(result);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        command.setAfter(afterData);
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{6, 6};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 6);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_6_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_6_", 6}, "randomAfter");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_6_");
        Assert.assertEquals(command.getDiceResult(), 6);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), afterData);
    }

    public void testDoResolveWhenThereIsTwoOverlappingResultMatchingTheResultIntervalShouldReturnAppropriateData() {
        // GIVEN
        command.getResults().add(result);
        command.getResults().add(overlappingResult);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expectLocale();
        expect(interactionHandler.hasRandomResult(character)).andReturn(true);
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{2, 2};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 2);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_2_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_2_", 2}, "Thrown value: _2_. Total: 2.");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), resultData);
        Assert.assertEquals(returned.get(0).getText(), "<p>Thrown value: _2_. Total: 2.</p><p>Result data text.</p>");
        Assert.assertSame(returned.get(1), overlappingData);
        Assert.assertSame(returned.get(1).getText(), "<p>Overlapping text.</p>");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
