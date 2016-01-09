package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Locale;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.HierarchicalMessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link TigerPieceMover}.
 * @author Tamas_Szekeres
 */
@Test
public class TigerPieceMoverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private TigerPieceMover underTest;
    @Mock private FfCharacter character;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private HuntRoundResult result;
    @Inject private RandomNumberGenerator generator;
    @Inject private HierarchicalMessageSource messageSource;
    @Inject private LocaleProvider localeProvider;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testGetPositionWhenNoPositionIsSetShouldReturnDefault() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "tigerPosition")).andReturn(null);
        mockControl.replay();
        // WHEN
        final String returned = underTest.getPosition(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, "E4");
    }

    public void testGetPositionWhenPositionIsSetShouldReturnIt() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "tigerPosition")).andReturn("A11");
        mockControl.replay();
        // WHEN
        final String returned = underTest.getPosition(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, "A11");
    }

    public void testMovePieceWhenRolledOneShouldMoveSouth() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        result.setTigerPosition("E4");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.1", null, Locale.ENGLISH)).andReturn("Tiger rolled 1.");
        result.setRoundMessage("Tiger rolled 1.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledTwoShouldMoveNorth() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{2, 2});
        result.setTigerPosition("E2");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.2", null, Locale.ENGLISH)).andReturn("Tiger rolled 2.");
        result.setRoundMessage("Tiger rolled 2.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledThreeShouldMoveWest() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{3, 3});
        result.setTigerPosition("D3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.3", null, Locale.ENGLISH)).andReturn("Tiger rolled 3.");
        result.setRoundMessage("Tiger rolled 3.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledFourShouldMoveEast() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});
        result.setTigerPosition("F3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.4", null, Locale.ENGLISH)).andReturn("Tiger rolled 4.");
        result.setRoundMessage("Tiger rolled 4.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledFiveShouldNotMove() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{5, 5});
        result.setTigerPosition("E3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.5", null, Locale.ENGLISH)).andReturn("Tiger rolled 5.");
        result.setRoundMessage("Tiger rolled 5.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledSixShouldMoveNorth() {
        // GIVEN
        expect(result.getTigerPosition()).andReturn("E3");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{6, 6});
        result.setTigerPosition("E2");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.6", null, Locale.ENGLISH)).andReturn("Tiger rolled 6.");
        result.setRoundMessage("Tiger rolled 6.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
