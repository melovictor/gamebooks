package hu.zagor.gamebooks.raw.mvc.book.section.controller;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfBookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class RawBookSectionControllerNegativeTest {

    private static final String DEFAULT_TEXT = "<p>This is some sample text.</p><p>This is text. <alt>This is to be replaced.</alt> End of text.</p>";

    private RawBookSectionController underTest;
    private IMocksControl mockControl;
    private SectionHandlingService sectionHandlingService;
    private Model model;
    private HttpServletRequest request;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private Paragraph oldParagraph;
    private BeanFactory beanFactory;
    private Logger logger;
    private ParagraphData data;
    private ChoiceSet choices;
    private Choice choice;
    private ChoiceSet choicesMixed;
    private ChoiceSet choicesExtra;
    private Choice choiceWithExtra;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        sectionHandlingService = mockControl.createMock(SectionHandlingService.class);
        model = mockControl.createMock(Model.class);
        request = mockControl.createMock(HttpServletRequest.class);
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        oldParagraph = new Paragraph("9", null, Integer.MAX_VALUE);
        beanFactory = mockControl.createMock(BeanFactory.class);
        logger = mockControl.createMock(Logger.class);
        data = new ParagraphData();
        oldParagraph.setData(data);

        underTest = new RawBookSectionController(sectionHandlingService);
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @BeforeMethod
    public void setUpMethod() {
        data.setText(DEFAULT_TEXT);
        choiceWithExtra = new Choice("10", null, 0, "Extra text.");
        choice = new Choice("9", null, 1, null);
        choicesMixed = new DefaultChoiceSet(new ChoicePositionComparator());
        choicesMixed.add(choice);
        choicesMixed.add(choiceWithExtra);
        choicesExtra = new DefaultChoiceSet(new ChoicePositionComparator());
        choicesExtra.add(choiceWithExtra);
        choices = new DefaultChoiceSet(new ChoicePositionComparator());
        choices.add(choice);
        data.setChoices(choices);
        mockControl.reset();
    }

    @Test(expectedExceptions = InvalidStepChoiceException.class)
    public void testHandleSectionWhenSectionIsWithChoicePositionAndPositionIsInvalidShouldThrowException() {
        // GIVEN
        logger.debug("Handling choice request '{}' for book.", "7");
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getParagraph()).andReturn(oldParagraph);
        logger.debug("Player tried to navigate to illegal position {}.", 7);
        mockControl.replay();
        // WHEN
        underTest.handleSection(model, request, "7");
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
