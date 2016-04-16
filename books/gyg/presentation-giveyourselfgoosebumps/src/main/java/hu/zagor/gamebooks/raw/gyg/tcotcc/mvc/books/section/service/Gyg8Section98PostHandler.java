package hu.zagor.gamebooks.raw.gyg.tcotcc.mvc.books.section.service;

import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Custom section post handler for GYG8, section 98.
 * @author Tamas_Szekeres
 */
@Component
public class Gyg8Section98PostHandler implements CustomPrePostSectionHandler, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final DateTime dateTime = beanFactory.getBean("gygCurrentDateTime", DateTime.class);
        final int dayOfWeek = dateTime.getDayOfWeek();
        final ChoiceSet choices = wrapper.getParagraph().getData().getChoices();
        choices.removeByPosition(dayOfWeek == DateTimeConstants.FRIDAY ? 2 : 1);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
