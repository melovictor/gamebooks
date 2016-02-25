package hu.zagor.gamebooks.mvc.book.load.controller;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
 * Unit test for class {@link GenericBookLoadController}.
 * @author Tamas_Szekeres
 */
@Test
public class GenericBookLoadControllerTest {

    protected static final String RETURNED = "111";
    private static final Integer PLAYER_ID = 7;
    private static final Long BOOK_ID = 56453413524564L;
    private GenericBookLoadController underTest;
    @MockControl private IMocksControl mockControl;
    private BookInformations info;
    @Mock private HttpServletRequest request;
    @Mock private Model model;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    @Inject private GameStateHandler gameStateHandler;
    @Mock private PlayerUser playerUser;
    @Mock private SavedGameContainer container;
    @Inject private Logger logger;
    @Mock private HttpServletResponse response;
    @Instance private ContinuationData continuationData;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private ChoiceSet choices;
    private Capture<Choice> choice;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(BOOK_ID);
        Whitebox.setInternalState(underTest, "info", info);
        continuationData.setContinuationPageName("s-background");
        continuationData.setPreviousBookId(BOOK_ID - 1);
        continuationData.setPreviousBookLastSectionId("456");
        choice = newCapture();
    }

    @UnderTest
    public GenericBookLoadController underTest() {
        return new GenericBookLoadController() {
            @Override
            protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
                logger.debug("called doLoad");
                return RETURNED;
            }

            @Override
            protected void doLoadPrevious(final HttpServletRequest request, final HttpServletResponse response, final SavedGameContainer savedGameContainer) {
                logger.debug("called doLoadPrevious");
            }

            @Override
            protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler) {
                logger.debug("called setUpCharacterHandler");
            }
        };
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testHandleLoadWhenCalledShouldCallAbstractMethod() {
        // GIVEN
        logger.debug("GenericBookLoadController.load");
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(playerUser);
        expect(playerUser.getId()).andReturn(PLAYER_ID);
        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID)).andReturn(container);
        gameStateHandler.loadGame(container);
        logger.debug("called doLoad");
        mockControl.replay();
        // WHEN
        final String returned = underTest.handleLoad(model, request);
        // THEN
        Assert.assertEquals(returned, RETURNED);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testHandleLoadPreviousWhenContinuationDataIsNullShouldThrowException() throws IOException {
        // GIVEN
        info.setContinuationData(null);
        logger.debug("GenericBookLoadController.loadPrevious");
        mockControl.replay();
        // WHEN
        underTest.handleLoadPrevious(request, response);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testHandleLoadPreviousWhenCharacterIsNotOnContinuationSectionShouldThrowException() throws IOException {
        // GIVEN
        info.setContinuationData(continuationData);
        logger.debug("GenericBookLoadController.loadPrevious");
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(playerUser);
        expect(playerUser.getId()).andReturn(PLAYER_ID);
        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID - 1)).andReturn(container);
        gameStateHandler.loadGame(container);
        expect(container.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY)).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("123");
        mockControl.replay();
        // WHEN
        underTest.handleLoadPrevious(request, response);
        // THEN throws exception
    }

    public void testHandleLoadPreviousWhenCharacterIsOnContinuationSectionShouldCallAbstractMethod() throws IOException {
        // GIVEN
        info.setContinuationData(continuationData);
        logger.debug("GenericBookLoadController.loadPrevious");
        expectWrapper();
        expect(wrapper.getPlayer()).andReturn(playerUser);
        expect(playerUser.getId()).andReturn(PLAYER_ID);
        expect(gameStateHandler.generateSavedGameContainer(PLAYER_ID, BOOK_ID - 1)).andReturn(container);
        gameStateHandler.loadGame(container);
        expect(container.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY)).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("456");
        logger.debug("called doLoadPrevious");
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.add(capture(choice))).andReturn(true);
        paragraph.calculateValidEvents();
        response.sendRedirect("s-background");
        mockControl.replay();
        // WHEN
        underTest.handleLoadPrevious(request, response);
        // THEN
        final Choice capturedChoice = choice.getValue();
        Assert.assertEquals(capturedChoice.getId(), "background");
        Assert.assertEquals(capturedChoice.getPosition(), -1);
        Assert.assertNull(capturedChoice.getText());
        Assert.assertNull(capturedChoice.getSingleChoiceText());
    }

    public void testGetInfoShouldReturnStoredInfo() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final BookInformations returned = underTest.getInfo();
        // THEN
        Assert.assertSame(returned, info);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
