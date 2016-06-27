package hu.zagor.gamebooks.raw.gyg.eftcoh.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableDateTime;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Gyg1Section109PostHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Gyg1Section109PostHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Gyg1Section109PostHandler underTest;
    @Inject private BeanFactory beanFactory;
    @Mock private BookInformations info;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Model model;
    @Mock private ReadableDateTime dateTime;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private ChoiceSet choices;
    @Mock private Choice choice;

    @Test(dataProvider = "dayPositionProvider")
    public void testHandleRemoveChoiceBasedOnDay(final int dayOfWeek, final int removePos) {
        // GIVEN
        expect(beanFactory.getBean("gygCurrentDateTime", ReadableDateTime.class)).andReturn(dateTime);
        expect(dateTime.getDayOfWeek()).andReturn(dayOfWeek);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getChoices()).andReturn(choices);
        expect(choices.removeByPosition(removePos)).andReturn(choice);
        mockControl.replay();
        // WHEN
        underTest.handle(model, wrapper, info, true);
        // THEN
    }

    @DataProvider
    public Object[][] dayPositionProvider() {
        return new Object[][]{{DateTimeConstants.MONDAY, 2}, {DateTimeConstants.TUESDAY, 1}, {DateTimeConstants.WEDNESDAY, 2}, {DateTimeConstants.THURSDAY, 1},
            {DateTimeConstants.FRIDAY, 2}, {DateTimeConstants.SATURDAY, 2}, {DateTimeConstants.SUNDAY, 1}};
    }

}
