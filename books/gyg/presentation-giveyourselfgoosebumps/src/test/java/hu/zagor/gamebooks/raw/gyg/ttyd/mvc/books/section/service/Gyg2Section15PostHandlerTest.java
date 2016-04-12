package hu.zagor.gamebooks.raw.gyg.ttyd.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.joda.time.DateTime;
import org.springframework.beans.factory.BeanFactory;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Gyg2Section15PostHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Gyg2Section15PostHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Gyg2Section15PostHandler underTest;
    @Inject private BeanFactory beanFactory;
    @Mock private HttpSessionWrapper wrapper;
    private final DateTime evenDayDateTime = new DateTime(2000, 1, 10, 0, 0);
    private final DateTime oddDayDateTime = new DateTime(2000, 1, 13, 0, 0);
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private ChoiceSet choices;
    @Mock private Choice choice;

    public void testHandleWhenEvenDayShouldRemoveZeroPosChoice() {
        // GIVEN
        expect(beanFactory.getBean("gyg2CurrentDateTime", DateTime.class)).andReturn(evenDayDateTime);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.removeByPosition(0)).andReturn(choice);
        mockControl.replay();
        // WHEN
        underTest.handle(null, wrapper, null, true);
        // THEN
    }

    public void testHandleWhenOddDayShouldRemoveOnePosChoice() {
        // GIVEN
        expect(beanFactory.getBean("gyg2CurrentDateTime", DateTime.class)).andReturn(oddDayDateTime);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.removeByPosition(1)).andReturn(choice);
        mockControl.replay();
        // WHEN
        underTest.handle(null, wrapper, null, true);
        // THEN
    }

}
