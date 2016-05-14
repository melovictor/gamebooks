package hu.zagor.gamebooks.books.bookinfo;

import hu.zagor.gamebooks.domain.BookInformations;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simplest implementation of the {@link BookInformationFetcher} interface that works by fetching the {@link BookInformations} object using the info-* alias.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultBookInformationFetcher implements BookInformationFetcher, BeanFactoryAware {
    private static final int BOOK_ID_PART = 3;

    private BeanFactory beanFactory;
    @Autowired private HttpServletRequest request;

    @Override
    public BookInformations getInfoById(final String bookId) {
        BookInformations info;
        try {
            info = beanFactory.getBean("info-" + bookId, BookInformations.class);
        } catch (final BeansException ex) {
            info = null;
        }
        return info;
    }

    @Override
    public BookInformations getInfoById(final long bookId) {
        return getInfoById(String.valueOf(bookId));
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public BookInformations getInfoByRequest() {
        final String bookId = request.getRequestURI().split("/")[BOOK_ID_PART];
        return getInfoById(bookId);
    }

}
