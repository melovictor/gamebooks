package hu.zagor.gamebooks.content.command.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.CloneFailedException;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RandomCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class RandomCommandResolverBTest extends CoreTextResolvingTest {
    @UnderTest private RandomCommandResolver underTest;
    @MockControl private IMocksControl mockControl;
    private ResolvationData resolvationData;
    private RandomCommand command;
    @Instance private ParagraphData rootData;
    @Mock private Character character;
    private Paragraph paragraph;
    private BookInformations info;
    @Instance private ParagraphData resultElse;
    @Instance private RandomResult result;
    @Instance private RandomResult overlappingNullResult;
    @Instance private CharacterHandler characterHandler;
    @Mock private DefaultUserInteractionHandler interactionHandler;
    @Mock private ParagraphData resultData;
    @Inject private BeanFactory beanFactory;
    private DiceConfiguration diceConfig;
    @Inject private RandomNumberGenerator generator;
    @Inject private Logger logger;
    @Inject private DiceResultRenderer diceRenderer;
    @Mock private ParagraphData clonedData;
    @Instance private List<String> messages;
    @Inject private ExpressionResolver expressionResolver;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(9);
        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        result.setMin("2");
        result.setMax("3");
        result.setParagraphData(resultData);
        overlappingNullResult.setMin("2");
        overlappingNullResult.setMax("2");
        overlappingNullResult.setParagraphData(null);
        info.setCharacterHandler(characterHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        diceConfig = new DiceConfiguration(1, 1, 6);
    }

    @BeforeMethod
    public void setUpMethod() {
        rootData.setText("<p>Initial content.</p>");
        resultElse.setText("<p>ResultElse data text.</p>");
        command = new RandomCommand();
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        expect(resultData.clone()).andReturn(clonedData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, getLocale());
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), clonedData);
    }

    public void testResolveSilentlyWhenThereIsResultMatchingTheResultIntervalWithNullDataShouldReturnAppropriateData() throws CloneNotSupportedException {
        // GIVEN
        command.getResults().add(result);
        command.getResults().add(overlappingNullResult);
        command.setResultElse(resultElse);
        command.setLabel("Throw a die.");
        command.setDiceConfig("dice1d6");
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfig);
        final int[] randomResult = new int[]{2, 2};
        expect(generator.getRandomNumber(diceConfig)).andReturn(randomResult);
        logger.debug("Random command generated the number '{}'.", 2);
        expect(diceRenderer.render(diceConfig, randomResult)).andReturn("_2_");
        expectTextWoLocale("page.raw.label.random.after", new Object[]{"_2_", 2}, "randomAfter");
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2).times(2);
        expect(resultData.clone()).andReturn(clonedData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, getLocale());
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_2_");
        Assert.assertEquals(command.getDiceResult(), 2);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertSame(returned.get(0), clonedData);
    }

    @Test(expectedExceptions = CloneFailedException.class)
    public void testResolveSilentlyWhenThereIsResultMatchingTheResultIntervalButCloneFailsShouldThrowException() throws CloneNotSupportedException {
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        expect(resultData.clone()).andThrow(new CloneNotSupportedException());
        logger.error("Failed to clone object '{}'.", resultData);
        mockControl.replay();
        // WHEN
        underTest.resolveSilently(command, resolvationData, messages, getLocale());
        // THEN throws exception
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
        expect(expressionResolver.resolveValue(character, "2")).andReturn(2);
        expect(expressionResolver.resolveValue(character, "3")).andReturn(3);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.resolveSilently(command, resolvationData, messages, getLocale());
        // THEN
        Assert.assertEquals(rootData.getText(), "<p>Initial content.</p>");
        Assert.assertEquals(command.getDiceResultText(), "_6_");
        Assert.assertEquals(command.getDiceResult(), 6);
        Assert.assertSame(command.getDiceResults(), randomResult);
        Assert.assertTrue(returned.isEmpty());
    }

}
