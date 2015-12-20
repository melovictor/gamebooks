package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CoreTextResolvingTest;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private RandomCommandResolver underTest;
    @MockControl private IMocksControl mockControl;
    private ResolvationData resolvationData;
    private RandomCommand command;
    @Instance private ParagraphData rootData;
    @Mock private Character character;
    private BookInformations info;
    @Instance private ParagraphData resultElse;
    @Instance private RandomResult result;
    @Instance private CharacterHandler characterHandler;
    @Mock private DefaultUserInteractionHandler interactionHandler;
    @Instance private ParagraphData resultData;
    @Inject private BeanFactory beanFactory;
    private DiceConfiguration diceConfig;
    @Inject private RandomNumberGenerator generator;
    @Inject private Logger logger;
    @Inject private DiceResultRenderer diceRenderer;
    @Instance private ParagraphData afterData;
    @Instance private RandomResult overlappingResult;
    @Instance private ParagraphData overlappingData;
    @Inject private ExpressionResolver expressionResolver;
    private Paragraph paragraph;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(9);
        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        overlappingResult.setMin("2");
        overlappingResult.setMax("2");
        overlappingData.setText("<p>Overlapping text.</p>");
        overlappingResult.setParagraphData(overlappingData);
        result.setMin("2");
        result.setMax("3");
        result.setParagraphData(resultData);
        resultData.setBeanFactory(beanFactory);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        diceConfig = new DiceConfiguration(1, 1, 6);
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2).times(2);
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
