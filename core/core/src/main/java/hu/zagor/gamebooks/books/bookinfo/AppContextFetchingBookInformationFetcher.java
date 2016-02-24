package hu.zagor.gamebooks.books.bookinfo;

import hu.zagor.gamebooks.domain.BookInformations;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Simplest implementation of the {@link BookInformationFetcher} interface that works by fetching all available {@link BookInformations} object from the {@link ApplicationContext}
 * and looks for the one with the matching ID.
 * @author Tamas_Szekeres
 */
@Component
public class AppContextFetchingBookInformationFetcher implements BookInformationFetcher, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public BookInformations getInfoById(final String bookId) {
        return getInfoById(Long.parseLong(bookId));
    }

    @Override
    public BookInformations getInfoById(final long bookId) {
        BookInformations found = null;

        for (final BookInformations info : applicationContext.getBeansOfType(BookInformations.class).values()) {
            if (info.getId() == bookId) {
                found = info;
                break;
            }
        }

        return found;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
