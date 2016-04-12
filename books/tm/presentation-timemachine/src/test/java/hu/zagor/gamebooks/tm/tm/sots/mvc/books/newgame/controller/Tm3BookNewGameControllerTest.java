package hu.zagor.gamebooks.tm.tm.sots.mvc.books.newgame.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.DefaultItemFactory;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Tm3BookNewGameController}.
 * @author Tamas_Szekeres
 */
@Test
public class Tm3BookNewGameControllerTest {
    @UnderTest private Tm3BookNewGameController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private HttpSessionWrapper wrapper;
    @Instance private CharacterHandler characterHandler;
    @Mock private Character character;
    private BookInformations info;
    @Mock private CharacterItemHandler itemHandler;
    @Inject private BeanFactory beanFactory;
    @Mock private DefaultItemFactory itemFactory;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1);
        Whitebox.setInternalState(underTest, "info", info);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
    }

    public void testSetUpCharacterHandlerShouldCallSuperAndAddStartingItems() {
        // GIVEN
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        itemHandler.setItemFactory(itemFactory);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, "1001", 1)).andReturn(1);
        expect(itemHandler.addItem(character, "1002", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.setUpCharacterHandler(wrapper, characterHandler);
        // THEN
    }
}
