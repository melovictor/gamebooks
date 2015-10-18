package hu.zagor.gamebooks.support.mock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

public class EasyMockAnnotations {

    public static void initialize(final Object testClass) {
        try {
            new EasyMockAnnotationsInitializer().initialize(testClass);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize the test object.", e);
        }
    }

    private EasyMockAnnotations() {
    }

    private static class EasyMockAnnotationsInitializer {

        private Object testClass;
        private Object underTest;
        private IMocksControl mockControl;

        private void initialize(final Object testClass) throws InstantiationException, IllegalAccessException {
            this.testClass = testClass;
            initializeMockControls();
            initializeUnderTest();
            initializeMocks();
        }

        private void initializeMockControls() {
            mockControl = EasyMock.createStrictControl();
            Whitebox.setInternalState(testClass, "mockControl", mockControl);
        }

        private void initializeUnderTest() throws InstantiationException, IllegalAccessException {
            final Field underTestField = ReflectionUtils.findField(testClass.getClass(), "underTest");
            if (underTestField.isAnnotationPresent(UnderTest.class)) {
                underTest = underTestField.getType().newInstance();
                Whitebox.setInternalState(testClass, "underTest", underTest);
            } else {
                underTest = Whitebox.getInternalState(testClass, "underTest");
            }
        }

        private void initializeMocks() {
            ReflectionUtils.doWithFields(testClass.getClass(), new FieldCallback() {

                @Override
                public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                    if (field.isAnnotationPresent(Mock.class)) {
                        final Object mock = mockControl.createMock(field.getType());
                        Whitebox.setInternalState(testClass, field.getName(), mock);
                    } else if (field.isAnnotationPresent(Inject.class)) {
                        final Object mock = mockControl.createMock(field.getType());
                        Whitebox.setInternalState(testClass, field.getName(), mock);
                        if (!setField(field, mock)) {
                            Whitebox.setInternalState(underTest, field.getName(), mock);
                        }
                    }
                }

                private boolean setField(final Field field, final Object mock) {
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
            });
        }
    }
}
