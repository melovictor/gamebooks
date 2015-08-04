package hu.zagor.gamebooks.support.logging;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

/**
 * Class for injecting {@link org.slf4j.Logger} objects to the classes that require those. The injection can
 * be used in multi threaded environments. Upon successful injection the logger will write a trace level
 * message about it containing the name of the class which the logger was injected into. In case of an
 * exception during the injection an IllegalStateException will be raised containing the cause exception for
 * further processing.
 * @author Tamas_Szekeres
 * @author Gabor_Boroczki
 */
public class LoggerInjector implements BeanPostProcessor {

    private final Logger selfLogger = LoggerFactory.getLogger(LoggerInjector.class);

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) {
        final Field loggerField = ReflectionUtils.findField(bean.getClass(), null, Logger.class);
        if (isLogger(loggerField)) {
            setLoggerIntoBean(loggerField, bean);
        }
        return bean;
    }

    private boolean isLogger(final Field loggerField) {
        return loggerField != null;
    }

    private void setLoggerIntoBean(final Field field, final Object bean) {
        ReflectionUtils.makeAccessible(field);
        if (isAnnotated(field)) {
            try {
                final Logger logger = LoggerFactory.getLogger(bean.getClass());
                field.set(bean, logger);
                selfLogger.trace("Logger was successfully injected into the bean of class '{}'.", bean.getClass().getName());
            } catch (final Exception e) {
                throw new IllegalStateException("The logger could not been injected into the bean because of an exception.", e);
            }
        }
    }

    private boolean isAnnotated(final Field field) {
        return field.getAnnotation(LogInject.class) != null;
    }

}
