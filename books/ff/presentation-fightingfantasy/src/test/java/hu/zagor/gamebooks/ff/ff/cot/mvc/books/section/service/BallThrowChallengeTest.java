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
 * Unit test for class {@link BallThrowChallenge}.
 * @author Tamas_Szekeres
 */
@Test
public class BallThrowChallengeTest {

    private IMocksControl mockControl;
    private BallThrowChallenge underTest;

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
        underTest = new BallThrowChallenge();
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

    public void testPlayGameWhenThrowingFinishesInEvenRoundShouldWinPrize() {
        // GIVEN
        expect(data.getText()).andReturn("[p]Original text.[/p][p]||placeholder||[/p]");
        expect(localeProvider.getLocale()).andReturn(locale);
        int[] throwResult = new int[]{3, 3};

        expect(generator.getRandomNumber(1)).andReturn(throwResult);
        expect(renderer.render(6, throwResult)).andReturn("Thrown value is 3.");
        expect(messageSource.getMessage(eq("page.ff5.ballthrow.heroThrow"), aryEq(new Object[]{"Thrown value is 3."}), eq(locale))).andReturn("Hero's throw: 3.");

        throwResult = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(throwResult);
        expect(renderer.render(6, throwResult)).andReturn("Thrown value is 1.");
        expect(messageSource.getMessage(eq("page.ff5.ballthrow.opponentThrow"), aryEq(new Object[]{"Thrown value is 1."}), eq(locale))).andReturn("Opponent's throw: 1.");

        expect(character.getGold()).andReturn(9);
        character.setGold(14);

        expect(messageSource.getMessage("page.ff5.ballthrow.heroWin", null, locale)).andReturn("Hero won.");

        data.setText("[p]Original text.[/p][p]Hero's throw: 3.<br />Opponent's throw: 1.<br />Hero won.[/p]");

        mockControl.replay();
        // WHEN
        underTest.playGame(character, data);
        // THEN
    }

    public void testPlayGameWhenThrowingFinishesInOddRoundShouldLosePrize() {
        // GIVEN
        expect(data.getText()).andReturn("[p]Original text.[/p][p]||placeholder||[/p]");
        expect(localeProvider.getLocale()).andReturn(locale);
        final int[] throwResult = new int[]{1, 1};

        expect(generator.getRandomNumber(1)).andReturn(throwResult);
        expect(renderer.render(6, throwResult)).andReturn("Thrown value is 1.");
        expect(messageSource.getMessage(eq("page.ff5.ballthrow.heroThrow"), aryEq(new Object[]{"Thrown value is 1."}), eq(locale))).andReturn("Hero's throw: 1.");

        expect(character.getGold()).andReturn(9);
        character.setGold(4);

        expect(messageSource.getMessage("page.ff5.ballthrow.opponentWin", null, locale)).andReturn("Opponent won.");

        data.setText("[p]Original text.[/p][p]Hero's throw: 1.<br />Opponent won.[/p]");

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
