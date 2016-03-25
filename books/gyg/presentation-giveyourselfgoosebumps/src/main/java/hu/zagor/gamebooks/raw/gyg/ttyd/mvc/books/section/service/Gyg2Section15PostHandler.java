package hu.zagor.gamebooks.raw.gyg.ttyd.mvc.books.section.service;

import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Custom section post handler for GYG2, section 15.
 * @author Tamas_Szekeres
 */
@Component
public class Gyg2Section15PostHandler implements CustomPrePostSectionHandler {

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final DateTime dateTime = new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Budapest")));
        final int dayOfMonth = dateTime.getDayOfMonth();
        final ChoiceSet choices = wrapper.getParagraph().getData().getChoices();
        choices.removeByPosition(dayOfMonth % 2);
    }

}
