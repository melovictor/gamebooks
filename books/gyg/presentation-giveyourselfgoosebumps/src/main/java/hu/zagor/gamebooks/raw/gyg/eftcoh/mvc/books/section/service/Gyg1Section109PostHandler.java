package hu.zagor.gamebooks.raw.gyg.eftcoh.mvc.books.section.service;

import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableDateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Custom section post handler for GYG1, section 109.
 * @author Tamas_Szekeres
 */
@Component
public class Gyg1Section109PostHandler implements CustomPrePostSectionHandler, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        final ReadableDateTime dateTime = beanFactory.getBean("gygCurrentDateTime", ReadableDateTime.class);
        final int dayOfWeek = dateTime.getDayOfWeek();
        final ChoiceSet choices = wrapper.getParagraph().getData().getChoices();
        choices.removeByPosition(dayOfWeek == DateTimeConstants.MONDAY || dayOfWeek == DateTimeConstants.WEDNESDAY || dayOfWeek == DateTimeConstants.FRIDAY
            || dayOfWeek == DateTimeConstants.SATURDAY ? 2 : 1);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
