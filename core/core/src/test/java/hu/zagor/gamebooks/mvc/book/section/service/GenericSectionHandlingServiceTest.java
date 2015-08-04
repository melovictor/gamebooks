package hu.zagor.gamebooks.mvc.book.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericSectionHandlingService}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericSectionHandlingServiceTest {

    private static final int ID = 10;
    private static final String USERNAME = "username";
    private GenericSectionHandlingService underTest;
    private IMocksControl mockControl;
    private BookContentInitializer contentInitializer;
    private BookInformations info;
    private PlayerUser player;
    private Model model;
    private BookItemStorage itemStorage;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        itemStorage = mockControl.createMock(BookItemStorage.class);
        underTest = new TestSectionHandlingService(contentInitializer);

        info = new BookInformations(ID);
        player = new PlayerUser(ID, USERNAME, false);
        model = mockControl.createMock(Model.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenContentInitializerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new TestSectionHandlingService(null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenModelIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(null, player, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenPlayerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(model, null, info);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInitModelWhenInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.initModel(model, player, null);
        // THEN throws exception
    }

    public void testInitModelWhenParametersAreOkNullShouldInitializeModel() {
        // GIVEN
        contentInitializer.initModel(model, player, info);
        mockControl.replay();
        // WHEN
        underTest.initModel(model, player, info);
        // THEN
    }

    public void testResolveParagraphIdShouldForwardRequestToProperItemStoreAndReturnItsResponse() {
        // GIVEN
        expect(contentInitializer.getItemStorage(info)).andReturn(itemStorage);
        expect(itemStorage.resolveParagraphId("114e")).andReturn("114");
        mockControl.replay();
        // WHEN
        final String returned = underTest.resolveParagraphId(info, "114e");
        // THEN
        Assert.assertEquals(returned, "114");
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestSectionHandlingService extends GenericSectionHandlingService {

        public TestSectionHandlingService(final BookContentInitializer contentInitializer) {
            super(contentInitializer);
        }

        @Override
        public String handleSection(final Model model, final HttpSessionWrapper wrapper, final Paragraph paragraph, final BookInformations info) {
            return null;
        }

        @Override
        public String checkParagraph(final Model model, final Paragraph paragraph, final String tile, final BookInformations info) {
            return null;
        }

        @Override
        protected String getPageTileName(final BookInformations info) {
            return null;
        }

        @Override
        protected String getCommandView(final CommandView commandView, final BookInformations info) {
            return null;
        }
    }
}
