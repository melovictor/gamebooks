package hu.zagor.gamebooks.mvc.book.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
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
    @Instance private Map<String, CustomPrePostSectionHandler> prePostHandlers;
    @Mock private CustomPrePostSectionHandler handlerA;
    @Mock private CustomPrePostSectionHandler handlerB;
    @Mock private CustomPrePostSectionHandler handlerC;
    @Mock private CustomPrePostSectionHandler handlerD;
    @Mock private Model model;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        player = new PlayerUser(9, "FireFoX", false);
        prePostHandlers.put("testing99Section11bPreHandler", handlerA);
        prePostHandlers.put("testing99p3Section99PreHandler", handlerB);
        prePostHandlers.put("testing99p3Section54aPostHandler", handlerC);
        prePostHandlers.put("testing99Section53PostHandler", handlerD);
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
        final Map<String, CustomPrePostSectionHandler> prePostHandlers = Whitebox.getInternalState(underTest, "prePostHandlers");
        prePostHandlers.clear();
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

    public void testInitWhenTest99BeanAvailableInSpringContextShouldInitializeInfoWithHandlers() {
        // GIVEN
        info.setResourceDir("testing99");
        expect(beanFactory.containsBean("testing99Info")).andReturn(true);
        expect(beanFactory.getBean("testing99Info", BookInformations.class)).andReturn(info);
        expect(applicationContext.getBeansOfType(CustomPrePostSectionHandler.class)).andReturn(prePostHandlers);
        mockControl.replay();
        // WHEN
        underTest.init();
        // THEN
        Assert.assertSame(underTest.getInfo(), info);
        final Map<String, CustomPrePostSectionHandler> prePostHandlers = Whitebox.getInternalState(underTest, "prePostHandlers");
        Assert.assertEquals(prePostHandlers.size(), 2);
        Assert.assertTrue(prePostHandlers.containsKey("11bPre"));
        Assert.assertTrue(prePostHandlers.containsKey("53Post"));
        Assert.assertFalse(prePostHandlers.containsKey("99Pre"));
        Assert.assertFalse(prePostHandlers.containsKey("54aPost"));
        Assert.assertSame(prePostHandlers.get("11bPre"), handlerA);
        Assert.assertSame(prePostHandlers.get("53Post"), handlerD);
    }

    public void testInitWhenTest99p3BeanAvailableInSpringContextShouldInitializeInfoWithHandlers() {
        // GIVEN
        info.setResourceDir("testing99p3");
        expect(beanFactory.containsBean("testing99p3Info")).andReturn(true);
        expect(beanFactory.getBean("testing99p3Info", BookInformations.class)).andReturn(info);
        expect(applicationContext.getBeansOfType(CustomPrePostSectionHandler.class)).andReturn(prePostHandlers);
        mockControl.replay();
        // WHEN
        final GenericBookSectionController controller = initController(new Testing99p3BookSectionController());
        controller.init();
        // THEN
        Assert.assertSame(controller.getInfo(), info);
        final Map<String, CustomPrePostSectionHandler> prePostHandlers = Whitebox.getInternalState(controller, "prePostHandlers");
        Assert.assertEquals(prePostHandlers.size(), 2);
        Assert.assertFalse(prePostHandlers.containsKey("11bPre"));
        Assert.assertFalse(prePostHandlers.containsKey("53Post"));
        Assert.assertTrue(prePostHandlers.containsKey("99Pre"));
        Assert.assertTrue(prePostHandlers.containsKey("54aPost"));
        Assert.assertSame(prePostHandlers.get("99Pre"), handlerB);
        Assert.assertSame(prePostHandlers.get("54aPost"), handlerC);
    }

    public void testInitWhenTest99p3BeanAvailableInSpringContextShouldInitializeInfoWithoutHandlers() {
        // GIVEN
        expect(beanFactory.containsBean("testing99p3Info")).andReturn(true);
        expect(beanFactory.getBean("testing99p3Info", BookInformations.class)).andReturn(info);
        expect(applicationContext.getBeansOfType(CustomPrePostSectionHandler.class)).andReturn(new HashMap<String, CustomPrePostSectionHandler>());
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
        expect(applicationContext.getBeansOfType(CustomPrePostSectionHandler.class)).andReturn(new HashMap<String, CustomPrePostSectionHandler>());
        mockControl.replay();
        // WHEN
        try {
            underTest.init();
            Assert.assertTrue(false);
        } catch (final IllegalStateException ex) {
        }
        // THEN
        Assert.assertNull(Whitebox.getInternalState(underTest, "info"));
    }

    public void testInitWhenBookIdCannotBeDeterminedShouldSkipInfoInitialization() {
        // GIVEN
        expect(applicationContext.getBeansOfType(CustomPrePostSectionHandler.class)).andReturn(new HashMap<String, CustomPrePostSectionHandler>());
        mockControl.replay();
        // WHEN
        try {
            initController(new _testingBookSectionController()).init();
            Assert.assertTrue(false);
        } catch (final IllegalStateException ex) {
        }
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

    public void testHandleCustomSectionsPreWhenNoPreHandlerExistsShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldParagraph.getId()).andReturn("15");
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, false);
        // THEN
    }

    public void testHandleCustomSectionsPostWhenNoPostHandlerExistsShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldParagraph.getId()).andReturn("15");
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPost(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsPreWhenPreHandlerExistsShouldCallPreHandler() {
        // GIVEN
        final Map<String, CustomPrePostSectionHandler> prePostHandlers = Whitebox.getInternalState(underTest, "prePostHandlers");
        Whitebox.setInternalState(underTest, "info", info);
        prePostHandlers.put("15Pre", handlerA);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldParagraph.getId()).andReturn("15");
        handlerA.handle(model, wrapper, info, true);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPre(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsPostWhenPostHandlerExistsShouldCallPreHandler() {
        // GIVEN
        final Map<String, CustomPrePostSectionHandler> prePostHandlers = Whitebox.getInternalState(underTest, "prePostHandlers");
        Whitebox.setInternalState(underTest, "info", info);
        prePostHandlers.put("15Post", handlerB);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        expect(oldParagraph.getId()).andReturn("15");
        handlerB.handle(model, wrapper, info, false);
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPost(model, wrapper, false);
        // THEN
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
