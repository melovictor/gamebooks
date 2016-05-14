package hu.zagor.gamebooks.spring;

import hu.zagor.gamebooks.domain.BookInformations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * A post processor that registers an alias for the {@link BookInformations} in the info-${BOOK_ID} format.
 * @author Tamas_Szekeres
 */
@Component
public class BookInformationPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof BookInformations) {
            final BookInformations info = (BookInformations) bean;
            final long bookId = info.getId();
            beanFactory.registerAlias(beanName, "info-" + bookId);
        }
        return bean;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
