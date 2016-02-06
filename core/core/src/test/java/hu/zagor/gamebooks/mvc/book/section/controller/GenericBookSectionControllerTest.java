package hu.zagor.gamebooks.mvc.book.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
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
    @Inject private BookContentInitializer contentInitializer;
    @MockControl private IMocksControl mockControl;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private HttpServletRequest request;
    @Mock private Paragraph oldParagraph;
    private PlayerUser player;
    private BookInformations info;
    @Mock private Paragraph newParagraph;
    @Inject private Logger logger;
    @Inject private GameStateHandler gameStateHandler;
    @Inject private ApplicationContext applicationContext;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        player = new PlayerUser(9, "FireFoX", false);
    }

    @UnderTest
    public GenericBookSectionController underTest() {
        return new Testing99BookSectionController();
    }

    private GenericBookSectionController initController(final GenericBookSectionController controller) {
        controller.setBeanFactory(beanFactory);
        controller.setApplicationContext(applicationContext);
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
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
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
        expect(applicationContext.containsBean("testing99PrePostSectionHandler")).andReturn(false);
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
        expect(applicationContext.containsBean("testing99p3PrePostSectionHandler")).andReturn(false);
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
        expect(applicationContext.containsBean("testing99PrePostSectionHandler")).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.init();
        // THEN
        Assert.assertNull(Whitebox.getInternalState(underTest, "info"));
    }

    public void testInitWhenBookIdCannotBeDeterminedShouldSkipInfoInitialization() {
        // GIVEN
        expect(applicationContext.containsBean("nullPrePostSectionHandler")).andReturn(false);
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
    }

    private class Testing99p3BookSectionController extends GenericBookSectionController {
    }

    private class _testingBookSectionController extends GenericBookSectionController {
    }

}
