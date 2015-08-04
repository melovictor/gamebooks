package hu.zagor.gamebooks.mvc.book.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GenericBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookSectionControllerTest {

    private static final String PARAGRAPH_ID = "99";
    private GenericBookSectionController underTest;
    private BookContentInitializer contentInitializer;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private HttpSessionWrapper wrapper;
    private HttpServletRequest request;
    private Paragraph oldParagraph;
    private PlayerUser player;
    private BookInformations info;
    private Paragraph newParagraph;
    private Logger logger;
    private GameStateHandler gameStateHandler;
    private HttpSession session;

    @BeforeClass
    public void setUpClass() {
        underTest = new Testing99BookSectionController();
        mockControl = EasyMock.createStrictControl();
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        request = mockControl.createMock(HttpServletRequest.class);
        oldParagraph = mockControl.createMock(Paragraph.class);
        newParagraph = mockControl.createMock(Paragraph.class);
        session = mockControl.createMock(HttpSession.class);
        info = new BookInformations(1L);
        player = new PlayerUser(9, "FireFoX", false);
        logger = mockControl.createMock(Logger.class);
        gameStateHandler = mockControl.createMock(GameStateHandler.class);

        initController(underTest);
    }

    private GenericBookSectionController initController(final GenericBookSectionController controller) {
        controller.setBeanFactory(beanFactory);
        Whitebox.setInternalState(controller, "contentInitializer", contentInitializer);
        Whitebox.setInternalState(controller, "logger", logger);
        Whitebox.setInternalState(controller, "gameStateHandler", gameStateHandler);
        return controller;
    }

    @BeforeMethod
    private void setUpMethod() {
        Whitebox.setInternalState(underTest, "info", (Object) null);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSectionWhenParagraphIsNullShouldThrowExceptino() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSection(null, request);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSectionWhenRequestIsNullShouldThrowExceptino() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSection(PARAGRAPH_ID, null);
        // THEN throws exception
    }

    public void testLoadSectionWhenInputParametersAreCorrectShouldInitializeContentAndReturnParagraph() {
        // GIVEN
        Whitebox.setInternalState(underTest, "info", info);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(contentInitializer.loadSection(PARAGRAPH_ID, player, oldParagraph, info)).andReturn(newParagraph);
        expect(wrapper.setParagraph(newParagraph)).andReturn(newParagraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.loadSection(PARAGRAPH_ID, request);
        // THEN
        Assert.assertSame(returned, newParagraph);
    }

    public void testInitWhenTest99BeanAvailableInSpringContextShouldInitializeInfo() {
        // GIVEN
        expect(beanFactory.containsBean("testing99Info")).andReturn(true);
        expect(beanFactory.getBean("testing99Info", BookInformations.class)).andReturn(info);
        mockControl.replay();
        // WHEN
        underTest.init();
        // THEN
        Assert.assertSame(underTest.getInfo(), info);
    }

    public void testInitWhenTest99p3BeanAvailableInSpringContextShouldInitializeInfo() {
        // GIVEN
        expect(beanFactory.containsBean("testing99p3Info")).andReturn(true);
        expect(beanFactory.getBean("testing99p3Info", BookInformations.class)).andReturn(info);
        mockControl.replay();
        // WHEN
        final GenericBookSectionController controller = initController(new Testing99p3BookSectionController());
        controller.init();
        // THEN
        Assert.assertSame(controller.getInfo(), info);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetInfoWhenBeanHasNotBeenInitializedShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getInfo();
        // THEN throws exception
    }

    public void testInitWhenBeanFactoryDoesNotContainInfoBeanShouldLeaveInfoFieldEmpty() {
        // GIVEN
        expect(beanFactory.containsBean("testing99Info")).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.init();
        // THEN
        Assert.assertNull(Whitebox.getInternalState(underTest, "info"));
    }

    public void testInitWhenBookIdCannotBeDeterminedShouldSkipInfoInitialization() {
        // GIVEN
        mockControl.replay();
        // WHEN
        initController(new _testingBookSectionController()).init();
        // THEN
        Assert.assertNull(Whitebox.getInternalState(underTest, "info"));
    }

    public void testGetLoggerShouldReturnLogger() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Logger returned = initController(new _testingBookSectionController()).getLogger();
        // THEN
        Assert.assertSame(returned, logger);
    }

    public void testGetGameStateHandlerShouldReturnGameStateHandler() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final GameStateHandler returned = initController(new _testingBookSectionController()).getGameStateHandler();
        // THEN
        Assert.assertSame(returned, gameStateHandler);
    }

    @AfterMethod
    private void tearDownMethod() {
        mockControl.verify();
    }

    private class Testing99BookSectionController extends GenericBookSectionController {

        @Override
        protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        }

    }

    private class Testing99p3BookSectionController extends GenericBookSectionController {

        @Override
        protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        }

    }

    private class _testingBookSectionController extends GenericBookSectionController {

        @Override
        protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        }

    }

}
