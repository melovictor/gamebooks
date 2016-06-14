package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.List;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.context.HierarchicalMessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link TypeAwareCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class TypeAwareCommandResolverTest {

    private TypeAwareCommandResolver<Command> underTest;
    private IMocksControl mockControl;
    private HierarchicalMessageSource messageSource;
    private LocaleProvider localeProvider;
    private DiceResultRenderer diceRenderer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        messageSource = mockControl.createMock(HierarchicalMessageSource.class);
        diceRenderer = mockControl.createMock(DiceResultRenderer.class);
        localeProvider = mockControl.createMock(LocaleProvider.class);

        underTest = new TestingCommandResolver();
        Whitebox.setInternalState(underTest, "messageSource", messageSource);
        Whitebox.setInternalState(underTest, "diceRenderer", diceRenderer);
        Whitebox.setInternalState(underTest, "localeProvider", localeProvider);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetMessageSourceShouldReturnMessageSource() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final HierarchicalMessageSource returned = underTest.getMessageSource();
        // THEN
        Assert.assertSame(returned, messageSource);
    }

    public void testGetDiceRendererShouldReturnDiceRenderer() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final DiceResultRenderer returned = underTest.getDiceRenderer();
        // THEN
        Assert.assertSame(returned, diceRenderer);
    }

    public void testGetLocaleProviderShouldReturnLocaleProvider() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final LocaleProvider returned = underTest.getLocaleProvider();
        // THEN
        Assert.assertSame(returned, localeProvider);
    }

    public void testAppendTextWhenDataTextIsNullShouldReplaceWithNewLabel() {
        // GIVEN
        final String label = "I am the new text";
        final ParagraphData data = new ParagraphData();
        data.setText(null);
        final boolean prefix = true;
        mockControl.replay();
        // WHEN
        underTest.appendText(data, label, prefix);
        // THEN
        Assert.assertEquals(data.getText(), "<p>I am the new text</p>");
    }

    public void testAppendTextWhenDataTextIsEmptyShouldReplaceWithNewLabel() {
        // GIVEN
        final String label = "I am the new text";
        final ParagraphData data = new ParagraphData();
        final boolean prefix = true;
        mockControl.replay();
        // WHEN
        underTest.appendText(data, label, prefix);
        // THEN
        Assert.assertEquals(data.getText(), "<p>I am the new text</p>");
    }

    public void testAppendTextWhenDataTextContainsDataAndIsNotPrefixShouldAddNewTextToTheEnd() {
        // GIVEN
        final String label = "I am the new text";
        final ParagraphData data = new ParagraphData();
        data.setText("[p]I am the old text[/p]");
        final boolean prefix = false;
        mockControl.replay();
        // WHEN
        underTest.appendText(data, label, prefix);
        // THEN
        Assert.assertEquals(data.getText(), "<p>I am the old text</p><p>I am the new text</p>");
    }

    public void testAppendTextWhenDataTextContainsDataAndIsPrefixShouldAddNewTextToTheBeginning() {
        // GIVEN
        final String label = "I am the new text";
        final ParagraphData data = new ParagraphData();
        data.setText("[p]I am the old text[/p]");
        final boolean prefix = true;
        mockControl.replay();
        // WHEN
        underTest.appendText(data, label, prefix);
        // THEN
        Assert.assertEquals(data.getText(), "<p>I am the new text</p><p>I am the old text</p>");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestingCommandResolver extends TypeAwareCommandResolver<Command> {

        @Override
        protected List<ParagraphData> doResolve(final Command command, final ResolvationData resolvationData) {
            return null;
        }
    }
}
