package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.ui.Model;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff14Section377PostHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff14Section377PostHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff14Section377PostHandler underTest;
    @Mock private Model model;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private FfBookInformations info;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private ChoiceSet choices;
    @Mock private Choice choice;

    public void testHandleWhenThereIsOnlyOneChoiceShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.size()).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.handle(model, wrapper, info, false);
        // THEN
    }

    public void testHandleWhenThereAreTwoChoicesShouldRemoveSecond() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.size()).andReturn(2);
        expect(choices.removeByPosition(1)).andReturn(choice);
        mockControl.replay();
        // WHEN
        underTest.handle(model, wrapper, info, false);
        // THEN
    }

}
