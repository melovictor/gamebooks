package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.ui.Model;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff11Section175PostHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff11Section175PostHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff11Section175PostHandler underTest;
    @Mock private Model model;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private FfBookInformations info;
    @Mock private FfCharacter character;
    @Mock private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private List<Item> removedItemsList;

    public void testHandleShouldRemoveSkilLDeductionMarkersAndIncreaseSkillWithSameAmount() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(itemHandler.removeItem(character, "4003", 20)).andReturn(removedItemsList);
        expect(removedItemsList.size()).andReturn(4);
        character.changeSkill(4);
        mockControl.replay();
        // WHEN
        underTest.handle(model, wrapper, info, false);
        // THEN
    }

}
