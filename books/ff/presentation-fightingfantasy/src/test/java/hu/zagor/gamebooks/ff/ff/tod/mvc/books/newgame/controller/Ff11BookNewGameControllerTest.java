package hu.zagor.gamebooks.ff.ff.tod.mvc.books.newgame.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.newgame.domain.FfPotionSelection;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff11BookNewGameController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff11BookNewGameControllerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff11BookNewGameController underTest;
    @Mock private FfCharacter character;
    @Inject private FfBookInformations info;
    @Mock private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Instance private FfPotionSelection potionSelection;

    public void testInitializeItemsShouldAddPotionMarker() {
        // GIVEN
        potionSelection.setPotion("2001");
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(itemHandler.addItem(character, "2001", 1)).andReturn(1);
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(itemHandler.addItem(character, "4101", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.initializeItems(potionSelection, character);
        // THEN
    }
}
