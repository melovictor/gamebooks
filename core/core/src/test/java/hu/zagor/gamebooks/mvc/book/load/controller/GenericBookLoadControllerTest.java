package hu.zagor.gamebooks.mvc.book.load.controller;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.Capturing;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    @Capturing private Capture<Choice> choice;
    @Mock private Character character;
    @Mock private List<String> paragraphList;
    @Mock private HttpSession session;

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
            protected void doLoadPrevious(final HttpSessionWrapper wrapper, final SavedGameContainer savedGameContainer) {
                logger.debug("called doLoadPrevious");
            }

            @Override
            protected void doContinuePrevious(final HttpSessionWrapper wrapper) {
                logger.debug("called doContinuePrevious");
            }

            @Override
            protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler) {
                logger.debug("called setUpCharacterHandler");
            }
        };
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
        expect(container.getElement(ControllerAddresses.CHARACTER_STORE_KEY)).andReturn(character);
        expect(character.getParagraphs()).andReturn(paragraphList);
        paragraphList.clear();
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

    @Test(expectedExceptions = IllegalStateException.class)
    public void testContinueWithPreviousWhenNoContinuationDataIsPreparedShouldThrowException() throws IOException {
        // GIVEN
        logger.debug("GenericBookLoadController.continueWithPrevious");

        info.setContinuationData(null);
        mockControl.replay();
        // WHEN
        underTest.continueWithPrevious(request, response);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testContinueWithPreviousWhenLastBookPositionIsIncorrectShouldThrowException() throws IOException {
        // GIVEN
        logger.debug("GenericBookLoadController.continueWithPrevious");
        expect(request.getSession()).andReturn(session);
        expect(session.getAttribute("paragraph56453413524563")).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("54");

        info.setContinuationData(continuationData);
        mockControl.replay();
        // WHEN
        underTest.continueWithPrevious(request, response);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testContinueWithPreviousWhenPreviousCharacterDoesNotExistsShouldThrowException() throws IOException {
        // GIVEN
        logger.debug("GenericBookLoadController.continueWithPrevious");
        expect(request.getSession()).andReturn(session);
        expect(session.getAttribute("paragraph56453413524563")).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("456");
        expect(session.getAttribute("char56453413524563")).andReturn(null);

        info.setContinuationData(continuationData);
        mockControl.replay();
        // WHEN
        underTest.continueWithPrevious(request, response);
        // THEN throws exception
    }

    public void testContinueWithPreviousWhenPreconditionsAreMetShouldContinueAtSpecifiedSection() throws IOException {
        // GIVEN
        logger.debug("GenericBookLoadController.continueWithPrevious");
        expect(request.getSession()).andReturn(session);
        expect(session.getAttribute("paragraph56453413524563")).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("456");
        expect(session.getAttribute("char56453413524563")).andReturn(character);
        expectWrapper();
        expect(wrapper.setCharacter(character)).andReturn(character);
        expect(character.getParagraphs()).andReturn(paragraphList);
        paragraphList.clear();

        logger.debug("called doContinuePrevious");
        expect(wrapper.getParagraph()).andReturn(null);
        expect(beanFactory.getBean(Paragraph.class)).andReturn(paragraph);
        expect(beanFactory.getBean("paragraphData", ParagraphData.class)).andReturn(data);
        paragraph.setData(data);
        expect(wrapper.setParagraph(paragraph)).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.add(capture(choice))).andReturn(true);
        paragraph.calculateValidEvents();

        response.sendRedirect("s-background");

        info.setContinuationData(continuationData);
        mockControl.replay();
        // WHEN
        underTest.continueWithPrevious(request, response);
        // THEN
        final Choice captured = choice.getValue();
        Assert.assertEquals(captured.getId(), "background");
        Assert.assertEquals(captured.getPosition(), -1);
        Assert.assertEquals(captured.getSingleChoiceText(), null);
        Assert.assertEquals(captured.getText(), null);
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }
}
