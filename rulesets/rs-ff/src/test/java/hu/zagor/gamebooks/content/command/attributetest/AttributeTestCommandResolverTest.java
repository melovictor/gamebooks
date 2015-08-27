package hu.zagor.gamebooks.content.command.attributetest;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.FfTextResolvingTest;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AttributeTestCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeTestCommandResolverTest extends FfTextResolvingTest {

    private static final int[] RANDOM_RESULT = new int[]{9, 4, 5};
    private static final String DICE_RENDER_RESULT = "dice render result";
    private IMocksControl mockControl;
    private AttributeTestCommandResolver underTest;
    private RandomNumberGenerator generator;
    private BeanFactory beanFactory;
    private ResolvationData resolvationData;
    private FfParagraphData rootData;
    private FfCharacter character;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfParagraphData success;
    private FfParagraphData failure;
    private FfRuleBookParagraphResolver paragraphResolver;
    private FfAttributeHandler attributeHandler;
    private AttributeTestCommand command;
    private DiceConfiguration diceConfiguration;
    private DiceResultRenderer diceRenderer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        success = mockControl.createMock(FfParagraphData.class);
        failure = mockControl.createMock(FfParagraphData.class);
        diceConfiguration = new DiceConfiguration(2, 1, 6);
        diceRenderer = mockControl.createMock(DiceResultRenderer.class);

        info = new FfBookInformations(1L);
        character = new FfCharacter();
        rootData = new FfParagraphData();
        characterHandler = new FfCharacterHandler();
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        paragraphResolver = mockControl.createMock(FfRuleBookParagraphResolver.class);

        info.setCharacterHandler(characterHandler);
        info.setParagraphResolver(paragraphResolver);
        characterHandler.setAttributeHandler(attributeHandler);

        characterHandler.setInteractionHandler(interactionHandler);

        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();

        underTest = new AttributeTestCommandResolver();
        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "diceRenderer", diceRenderer);
        underTest.setBeanFactory(beanFactory);
        init(mockControl, underTest);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new AttributeTestCommand();
        command.setSuccess(success);
        command.setFailure(failure);
        command.setAgainst("skill");
        command.setLabel("testing label");
        command.setConfigurationName("2d6");
        command.setAdd(0);
        command.setSuccessType(AttributeTestSuccessType.lowerEquals);
        command.setResultString(null);

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenNoSuccessIsSetUpShouldThrowException() {
        // GIVEN
        command.setSuccess(null);
        mockControl.replay();
        // WHEN
        new AttributeTestCommandResolver().resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResolveWhenNoFailureIsSetUpShouldThrowException() {
        // GIVEN
        command.setFailure(null);
        mockControl.replay();
        // WHEN
        new AttributeTestCommandResolver().resolve(command, resolvationData);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testProvideSuccessTypeWhenSuccessTypeNotSpecifiedAndParagraphResolverIsNotAwareOfDefaultTypeShouldThrowException() throws Throwable {
        // GIVEN
        command.setSuccessType(null);
        expect(paragraphResolver.getAttributeTestDefaultSuccessType("skill")).andReturn(null);
        mockControl.replay();
        // WHEN
        triggerVoidCall("provideSuccessType", new Class[]{AttributeTestCommand.class, ResolvationData.class}, new Object[]{command, resolvationData});
        // THEN throws exception
    }

    public void testProvideSuccessTypeWhenSuccessTypeNotSpecifiedAndParagraphResolverIsAwareOfDefaultTypeShouldSetSuccessTypeFromResolver() throws Throwable {
        // GIVEN
        command.setSuccessType(null);
        expect(paragraphResolver.getAttributeTestDefaultSuccessType("skill")).andReturn(AttributeTestSuccessType.lower);
        mockControl.replay();
        // WHEN
        triggerVoidCall("provideSuccessType", new Class[]{AttributeTestCommand.class, ResolvationData.class}, new Object[]{command, resolvationData});
        // THEN
        Assert.assertEquals(command.getSuccessType(), AttributeTestSuccessType.lower);
    }

    public void testProvideSuccessTypeWhenSuccessTypeIsSpecifiedShouldLeaveSuccessType() throws Throwable {
        // GIVEN
        mockControl.replay();
        // WHEN
        triggerVoidCall("provideSuccessType", new Class[]{AttributeTestCommand.class, ResolvationData.class}, new Object[]{command, resolvationData});
        // THEN
        Assert.assertEquals(command.getSuccessType(), AttributeTestSuccessType.lowerEquals);
    }

    public void testProvideLabelMessageWhenLabelIsNotNullShouldDoNothing() throws Throwable {
        // GIVEN
        mockControl.replay();
        // WHEN
        triggerVoidCall("provideLabelMessage", new Class[]{AttributeTestCommand.class, Locale.class}, new Object[]{command, getLocale()});
        // THEN
        Assert.assertEquals(command.getLabel(), "testing label");
    }

    public void testProvideLabelMessageWhenLabelIsNullShouldGetMessageFromMessageSource() throws Throwable {
        // GIVEN
        command.setLabel(null);
        expectTextWoLocale("page.ff.label.test.skill", "message source label", false);
        mockControl.replay();
        // WHEN
        triggerVoidCall("provideLabelMessage", new Class[]{AttributeTestCommand.class, Locale.class}, new Object[]{command, getLocale()});
        // THEN
        Assert.assertEquals(command.getLabel(), "message source label");
    }

    public void testResolveWhenNoResponseShouldBeSetShouldSetAttribOnCharAndReturnNull() {
        // GIVEN
        expectLocale();
        expect(interactionHandler.hasAttributeTestResult(character)).andReturn(false);
        interactionHandler.setAttributeTestResult(character);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned.getResolveList());
        Assert.assertFalse(returned.isFinished());
    }

    public void testResolveWhenHasResponseAndLowerEqualAndIsEqualAndNotLuckShouldReturnSuccess() {
        // GIVEN
        expectLocale();
        expect(interactionHandler.hasAttributeTestResult(character)).andReturn(true);
        expect(beanFactory.getBean("2d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(RANDOM_RESULT);
        expect(diceRenderer.render(diceConfiguration, RANDOM_RESULT)).andReturn(DICE_RENDER_RESULT);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expectTextWoLocale("page.ff.label.test.success", "Success!", false);
        expectTextWoLocale("page.ff.label.test.after", "this is the final message", false, DICE_RENDER_RESULT, 9, "Success!");
        expect(success.getText()).andReturn("[p]Something or other.[/p]");
        success.setText("[p]this is the final message[/p][p]Something or other.[/p]");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned.getResolveList().get(0), success);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenHasResponseAndLowerAndIsEqualAndNotLuckShouldReturnFailure() {
        // GIVEN
        command.setSuccessType(AttributeTestSuccessType.lower);
        expectLocale();
        expect(interactionHandler.hasAttributeTestResult(character)).andReturn(true);
        expect(beanFactory.getBean("2d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(RANDOM_RESULT);
        expect(diceRenderer.render(diceConfiguration, RANDOM_RESULT)).andReturn(DICE_RENDER_RESULT);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expectTextWoLocale("page.ff.label.test.failure", "Failure!", false);
        expectTextWoLocale("page.ff.label.test.after", "this is the final message", false, DICE_RENDER_RESULT, 9, "Failure!");
        expect(failure.getText()).andReturn(null);
        failure.setText("[p]this is the final message[/p]");
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned.getResolveList().get(0), failure);
        Assert.assertTrue(returned.isFinished());
    }

    public void testResolveWhenHasResponseAndLowerEqualAndIsEqualAndLuckShouldDecreaseLuckAndReturnSuccess() {
        // GIVEN
        command.setAgainst("luck");
        expectLocale();
        expect(interactionHandler.hasAttributeTestResult(character)).andReturn(true);
        expect(beanFactory.getBean("2d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(RANDOM_RESULT);
        expect(diceRenderer.render(diceConfiguration, RANDOM_RESULT)).andReturn(DICE_RENDER_RESULT);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(9);
        expectTextWoLocale("page.ff.label.test.success", "Success!", false);
        expectTextWoLocale("page.ff.label.test.after", "this is the final message", false, DICE_RENDER_RESULT, 9, "Success!");
        expect(success.getText()).andReturn("[p]Something or other.[/p]");
        success.setText("[p]this is the final message[/p][p]Something or other.[/p]");
        attributeHandler.handleModification(character, "luck", -1);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned.getResolveList().get(0), success);
        Assert.assertTrue(returned.isFinished());
    }

    private void triggerVoidCall(final String methodName, final Class<?>[] parameterTypes, final Object[] parameterObjects) throws Throwable {
        final Method method = Whitebox.getMethod(AttributeTestCommandResolver.class, methodName, parameterTypes);
        method.setAccessible(true);
        try {
            method.invoke(underTest, parameterObjects);
        } catch (final InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
