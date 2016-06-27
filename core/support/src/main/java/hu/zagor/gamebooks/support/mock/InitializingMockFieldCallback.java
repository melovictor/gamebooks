package hu.zagor.gamebooks.support.mock;

import hu.zagor.gamebooks.support.mock.annotation.Capturing;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.StringUtils;

/**
 * Callback to initialize the mocks in a specific test instance and underTest.
 * @author Tamas_Szekeres
 */
public class InitializingMockFieldCallback implements FieldCallback {

    private final List<Class<?>> collections = Arrays.asList((Class<?>) HashMap.class, TreeMap.class, ArrayList.class, HashSet.class, TreeSet.class);

    private Object underTest;
    private final Object testInstance;
    private final IMocksControl mockControl;

    public InitializingMockFieldCallback(final IMocksControl mockControl, final Object testInstance) {
        this.mockControl = mockControl;
        this.testInstance = testInstance;
    }

    public InitializingMockFieldCallback(final IMocksControl mockControl, final Object testInstance, final Object underTest) {
        this.mockControl = mockControl;
        this.testInstance = testInstance;
        this.underTest = underTest;
    }

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        if (underTest == null) {
            if (field.isAnnotationPresent(Mock.class)) {
                injectOnTest(field);
            } else if (field.isAnnotationPresent(Instance.class)) {
                instantiateOnTest(field);
            } else if (field.isAnnotationPresent(Capturing.class)) {
                injectCapture(field);
            }
        } else {
            if (field.isAnnotationPresent(Inject.class)) {
                final Object mock = injectOnTest(field);
                if (!setOnUnderTest(field, mock)) {
                    try {
                        Whitebox.setInternalState(underTest, field.getName(), mock);
                    } catch (final Exception ex) {
                        System.out.println("Couldn't inject value for field '" + field.getName() + "' in class '" + underTest.getClass()
                            + "'. It is extremely likely that this test will fail miserably.");
                    }
                }
            } else if (field.isAnnotationPresent(Instance.class)) {
                final Instance annotation = field.getAnnotation(Instance.class);
                if (annotation.inject()) {
                    Whitebox.setInternalState(underTest, field.getName(), Whitebox.getInternalState(testInstance, field.getName()));
                }
            }
        }
    }

    private void injectCapture(final Field field) {
        if (field.getType() != Capture.class) {
            throw new IllegalStateException("The @Capturing annotation can only be used on Capture<?> types.");
        }
        Whitebox.setInternalState(testInstance, field.getName(), EasyMock.newCapture());
    }

    private void instantiateOnTest(final Field field) throws IllegalAccessException {
        try {
            final Object instance;
            if (isCollection(field)) {
                instance = instantiateCollection(field);
            } else {
                final Instance annotation = field.getAnnotation(Instance.class);
                if (annotation.type() == Object.class) {
                    instance = field.getType().newInstance();
                } else {
                    instance = annotation.type().newInstance();
                }
            }
            if (instance == null) {
                throw new IllegalStateException("Couldn't inject any objects into the field " + field.getName() + " with type " + field.getType() + ".");
            }
            Whitebox.setInternalState(testInstance, field.getName(), instance);
        } catch (final InstantiationException e) {
            throw new IllegalAccessException(e.getClass() + ": " + e.getMessage());
        }
    }

    private Object instantiateCollection(final Field field) throws InstantiationException, IllegalAccessException {
        Object instance = null;
        final Instance annotation = field.getAnnotation(Instance.class);
        if (annotation.type() == Object.class) {
            for (final Class<?> clazz : collections) {
                if (isAssignable(field, clazz)) {
                    instance = clazz.newInstance();
                }
            }
        } else {
            instance = annotation.type().newInstance();
        }
        return instance;
    }

    private boolean isAssignable(final Field field, final Class<?> clazz) {
        return field.getType().isAssignableFrom(clazz);
    }

    private boolean isCollection(final Field field) {
        boolean isCollection = false;
        for (final Class<?> clazz : collections) {
            isCollection |= isAssignable(field, clazz);
        }

        return isCollection;
    }

    private Object injectOnTest(final Field field) {
        final Object mock = mockControl.createMock(field.getType());
        Whitebox.setInternalState(testInstance, field.getName(), mock);
        return mock;
    }

    private boolean setOnUnderTest(final Field field, final Object mock) {
        final String setterName = "set" + StringUtils.capitalize(field.getName());
        Method method = ReflectionUtils.findMethod(underTest.getClass(), setterName, (Class<?>[]) null);
        if (method != null) {
            try {
                method.invoke(underTest, mock);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                method = null;
            }
        }
        return method != null;
    }

}
