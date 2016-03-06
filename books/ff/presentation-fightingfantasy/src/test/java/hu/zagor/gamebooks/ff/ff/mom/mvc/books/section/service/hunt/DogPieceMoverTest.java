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
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DogPieceMover}.
 * @author Tamas_Szekeres
 */
@Test
public class DogPieceMoverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DogPieceMover underTest;
    @Mock private FfCharacter character;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private HuntRoundResult result;
    @Inject private RandomNumberGenerator generator;
    @Inject private HierarchicalMessageSource messageSource;
    @Inject private LocaleProvider localeProvider;

    public void testGetPositionWhenNoPositionIsSetShouldReturnDefault() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "dogPosition")).andReturn(null);
        mockControl.replay();
        // WHEN
        final String returned = underTest.getPosition(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, "E12");
    }

    public void testGetPositionWhenPositionIsSetShouldReturnIt() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "dogPosition")).andReturn("G5");
        mockControl.replay();
        // WHEN
        final String returned = underTest.getPosition(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, "G5");
    }

    public void testMovePieceWhenRolledOneAndTigerIsAboveDogShouldMoveNorth() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E7");
        expect(result.getTigerPosition()).andReturn("E2");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        result.setDogPosition("E6");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.1", null, Locale.ENGLISH)).andReturn("Dog rolled 1.");
        result.setRoundMessage("Dog rolled 1.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledOneAndTigerIsSouthWestOfDogShouldMoveSouth() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E7");
        expect(result.getTigerPosition()).andReturn("B10");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        result.setDogPosition("E8");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.1", null, Locale.ENGLISH)).andReturn("Dog rolled 1.");
        result.setRoundMessage("Dog rolled 1.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledTwoShouldMoveEast() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E3");
        expect(result.getTigerPosition()).andReturn("B6");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{2, 2});
        result.setDogPosition("F3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.2", null, Locale.ENGLISH)).andReturn("Dog rolled 2.");
        result.setRoundMessage("Dog rolled 2.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledThreeShouldMoveNorthTwice() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E3");
        expect(result.getTigerPosition()).andReturn("B6");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{3, 3});
        result.setDogPosition("E1");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.3", null, Locale.ENGLISH)).andReturn("Dog rolled 3.");
        result.setRoundMessage("Dog rolled 3.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledFourAndTigerIsEastOfDogShouldMoveEast() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("B7");
        expect(result.getTigerPosition()).andReturn("F7");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});
        result.setDogPosition("D7");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.4", null, Locale.ENGLISH)).andReturn("Dog rolled 4.");
        result.setRoundMessage("Dog rolled 4.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledFiveShouldMoveWest() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E3");
        expect(result.getTigerPosition()).andReturn("B6");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{5, 5});
        result.setDogPosition("D3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.5", null, Locale.ENGLISH)).andReturn("Dog rolled 5.");
        result.setRoundMessage("Dog rolled 5.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

    public void testMovePieceWhenRolledSixShouldNotMove() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("E3");
        expect(result.getTigerPosition()).andReturn("B6");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{6, 6});
        result.setDogPosition("E3");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.6", null, Locale.ENGLISH)).andReturn("Dog rolled 6.");
        result.setRoundMessage("Dog rolled 6.<br />");
        mockControl.replay();
        // WHEN
        underTest.movePiece(result);
        // THEN
    }

}
