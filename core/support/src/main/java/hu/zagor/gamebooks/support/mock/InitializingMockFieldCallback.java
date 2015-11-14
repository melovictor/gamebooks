package hu.zagor.gamebooks.support.mock;

import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * Callback to initialize the mocks in a specific test instance and underTest.
 * @author Tamas_Szekeres
 */
public class InitializingMockFieldCallback implements FieldCallback {

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
            }
        } else {
            if (field.isAnnotationPresent(Inject.class)) {
                final Object mock = injectOnTest(field);
                if (!setOnUnderTest(field, mock)) {
                    Whitebox.setInternalState(underTest, field.getName(), mock);
                }
            }
        }
    }

    private void instantiateOnTest(final Field field) throws IllegalAccessException {
        try {
            final Object instance = field.getType().newInstance();
            Whitebox.setInternalState(testInstance, field.getName(), instance);
        } catch (final InstantiationException e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }

    private Object injectOnTest(final Field field) {
        final Object mock = mockControl.createMock(field.getType());
        Whitebox.setInternalState(testInstance, field.getName(), mock);
        return mock;
    }

    private boolean setOnUnderTest(final Field field, final Object mock) {
        final String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method method = ReflectionUtils.findMethod(underTest.getClass(), setterName, field.getType());
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
