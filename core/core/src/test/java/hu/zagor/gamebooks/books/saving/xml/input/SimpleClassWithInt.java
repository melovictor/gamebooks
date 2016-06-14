package hu.zagor.gamebooks.books.saving.xml.input;

import hu.zagor.gamebooks.books.saving.xml.DefaultXmlGameStateSaver;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.Serializable;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Test class for testing {@link DefaultXmlGameStateSaver}.
 * @author Tamas_Szekeres
 */
public class SimpleClassWithInt implements Serializable, BeanFactoryAware {

    private static final int FIELD_VALUE = 1534;
    private int intField = FIELD_VALUE;
    @LogInject
    private Logger logger;
    private BeanFactory beanFactory;

    public int getIntField() {
        return intField;
    }

    void setIntField(final int intField) {
        this.intField = intField;
    }

    BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Logger getLogger() {
        return logger;
    }

}
