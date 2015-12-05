package hu.zagor.gamebooks.books.saving.xml;

import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Abstract base class for making game state related operations (loading, saving).
 * @author Tamas_Szekeres
 */
public abstract class AbstractGameStateHandler implements BeanFactoryAware {
    @LogInject private Logger logger;
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Logger getLogger() {
        return logger;
    }
}
