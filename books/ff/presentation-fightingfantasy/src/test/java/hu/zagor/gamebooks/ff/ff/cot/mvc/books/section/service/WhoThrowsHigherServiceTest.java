package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import java.util.Locale;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.context.HierarchicalMessageSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link WhoThrowsHigherService}.
 * @author Tamas_Szekeres
 */
@Test
public class WhoThrowsHigherServiceTest {

    private IMocksControl mockControl;
    private WhoThrowsHigherService underTest;

    private RandomNumberGenerator generator;
    private DiceResultRenderer renderer;
    private HierarchicalMessageSource messageSource;
    private LocaleProvider localeProvider;
    private FfCharacter character;
    private FfParagraphData data;
    private Locale locale;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new WhoThrowsHigherService();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        renderer = mockControl.createMock(DiceResultRenderer.class);
        messageSource = mockControl.createMock(HierarchicalMessageSource.class);
        localeProvider = mockControl.createMock(LocaleProvider.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "renderer", renderer);
        Whitebox.setInternalState(underTest, "messageSource", messageSource);
        Whitebox.setInternalState(underTest, "localeProvider", localeProvider);
        character = mockControl.createMock(FfCharacter.class);
        data = mockControl.createMock(FfParagraphData.class);
        locale = Locale.ENGLISH;
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testPlayGameWhenAllDwarvesThrowBelowUsShouldWin() {
        // GIVEN
        final int[] dwarfA = new int[]{7, 3, 4};
        final int[] dwarfB = new int[]{11, 6, 5};
        final int[] dwarfC = new int[]{2, 1, 1};
        final int[] hero = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(dwarfA);
        expect(generator.getRandomNumber(2)).andReturn(dwarfB);
        expect(generator.getRandomNumber(2)).andReturn(dwarfC);
        expect(generator.getRandomNumber(2)).andReturn(hero);
        expect(character.getGold()).andReturn(9);
        character.setGold(15);
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(renderer.render(6, dwarfA)).andReturn("Thrown value is 7.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfA"), aryEq(new Object[]{"Thrown value is 7.", 7}), eq(locale))).andReturn(
            "First Dwarf thrown 7.");
        expect(renderer.render(6, dwarfB)).andReturn("Thrown value is 11.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfB"), aryEq(new Object[]{"Thrown value is 11.", 11}), eq(locale))).andReturn(
            "Second Dwarf thrown 11.");
        expect(renderer.render(6, dwarfC)).andReturn("Thrown value is 2.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfC"), aryEq(new Object[]{"Thrown value is 2.", 2}), eq(locale))).andReturn(
            "Third Dwarf thrown 2.");
        expect(renderer.render(6, hero)).andReturn("Thrown value is 12.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.hero"), aryEq(new Object[]{"Thrown value is 12.", 12}), eq(locale))).andReturn("We threw 12.");
        expect(messageSource.getMessage("page.ff5.whoThrowsHigher.win", null, locale)).andReturn("We won.");
        expect(data.getText()).andReturn("[p]Original text.[/p]");
        data.setText("[p]Original text.[/p][p]First Dwarf thrown 7.<br />Second Dwarf thrown 11.<br />Third Dwarf thrown 2.<br />We threw 12.<br />We won.[/p]");
        mockControl.replay();
        // WHEN
        underTest.playGame(character, data);
        // THEN
    }

    public void testPlayGameWhenWeTieShouldTie() {
        // GIVEN
        final int[] dwarfA = new int[]{7, 3, 4};
        final int[] dwarfB = new int[]{11, 6, 5};
        final int[] dwarfC = new int[]{2, 1, 1};
        final int[] hero = new int[]{11, 5, 6};
        expect(generator.getRandomNumber(2)).andReturn(dwarfA);
        expect(generator.getRandomNumber(2)).andReturn(dwarfB);
        expect(generator.getRandomNumber(2)).andReturn(dwarfC);
        expect(generator.getRandomNumber(2)).andReturn(hero);
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(renderer.render(6, dwarfA)).andReturn("Thrown value is 7.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfA"), aryEq(new Object[]{"Thrown value is 7.", 7}), eq(locale))).andReturn(
            "First Dwarf thrown 7.");
        expect(renderer.render(6, dwarfB)).andReturn("Thrown value is 11.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfB"), aryEq(new Object[]{"Thrown value is 11.", 11}), eq(locale))).andReturn(
            "Second Dwarf thrown 11.");
        expect(renderer.render(6, dwarfC)).andReturn("Thrown value is 2.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfC"), aryEq(new Object[]{"Thrown value is 2.", 2}), eq(locale))).andReturn(
            "Third Dwarf thrown 2.");
        expect(renderer.render(6, hero)).andReturn("Thrown value is 11.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.hero"), aryEq(new Object[]{"Thrown value is 11.", 11}), eq(locale))).andReturn("We threw 11.");
        expect(messageSource.getMessage("page.ff5.whoThrowsHigher.tie", null, locale)).andReturn("We tied.");
        expect(data.getText()).andReturn("[p]Original text.[/p]");
        data.setText("[p]Original text.[/p][p]First Dwarf thrown 7.<br />Second Dwarf thrown 11.<br />Third Dwarf thrown 2.<br />We threw 11.<br />We tied.[/p]");
        mockControl.replay();
        // WHEN
        underTest.playGame(character, data);
        // THEN
    }

    public void testPlayGameWhenHighestDwarfThrowsAboveUsShouldLose() {
        // GIVEN
        final int[] dwarfA = new int[]{7, 3, 4};
        final int[] dwarfB = new int[]{11, 6, 5};
        final int[] dwarfC = new int[]{2, 1, 1};
        final int[] hero = new int[]{10, 4, 6};
        expect(generator.getRandomNumber(2)).andReturn(dwarfA);
        expect(generator.getRandomNumber(2)).andReturn(dwarfB);
        expect(generator.getRandomNumber(2)).andReturn(dwarfC);
        expect(generator.getRandomNumber(2)).andReturn(hero);
        expect(character.getGold()).andReturn(9);
        character.setGold(7);
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(renderer.render(6, dwarfA)).andReturn("Thrown value is 7.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfA"), aryEq(new Object[]{"Thrown value is 7.", 7}), eq(locale))).andReturn(
            "First Dwarf thrown 7.");
        expect(renderer.render(6, dwarfB)).andReturn("Thrown value is 11.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfB"), aryEq(new Object[]{"Thrown value is 11.", 11}), eq(locale))).andReturn(
            "Second Dwarf thrown 11.");
        expect(renderer.render(6, dwarfC)).andReturn("Thrown value is 2.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.dwarfC"), aryEq(new Object[]{"Thrown value is 2.", 2}), eq(locale))).andReturn(
            "Third Dwarf thrown 2.");
        expect(renderer.render(6, hero)).andReturn("Thrown value is 10.");
        expect(messageSource.getMessage(eq("page.ff5.whoThrowsHigher.hero"), aryEq(new Object[]{"Thrown value is 10.", 10}), eq(locale))).andReturn("We threw 10.");
        expect(messageSource.getMessage("page.ff5.whoThrowsHigher.lose", null, locale)).andReturn("We lost.");
        expect(data.getText()).andReturn("[p]Original text.[/p]");
        data.setText("[p]Original text.[/p][p]First Dwarf thrown 7.<br />Second Dwarf thrown 11.<br />Third Dwarf thrown 2.<br />We threw 10.<br />We lost.[/p]");
        mockControl.replay();
        // WHEN
        underTest.playGame(character, data);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
