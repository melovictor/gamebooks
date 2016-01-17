package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Sor2BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Sor2BookSectionControllerTest {
    private Sor2BookSectionController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private SectionHandlingService sectionHandler;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private SorCharacter character;
    @Mock private CommandView commandView;
    @Inject private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);
    }

    @UnderTest
    public Sor2BookSectionController underTest() {
        return new Sor2BookSectionController(sectionHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Sor2BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testHandleAfterFightWhenFightIsOngoingShouldDoNothing() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(commandView);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "10");
        // THEN
    }

    public void testHandleAfterFightWhenFightIsOverShouldRemoveSingleBattleSkillBonusesAndCursedChainmailGloves() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        itemHandler.removeItem(character, "3042", 1);

        expect(wrapper.getCharacter()).andReturn(character);
        expect(character.getCommandView()).andReturn(null);
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        itemHandler.removeItem(character, "4015", 1);
        itemHandler.removeItem(character, "4016", 1);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "10");
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
