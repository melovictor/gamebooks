package hu.zagor.gamebooks.support.mock;

import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.springframework.util.ReflectionUtils;

/**
 * Automatically prepares unit tests with the underTest, mock control, mocks and injections using annotations on the fields and/or methods.
 * @author Tamas_Szekeres
 */
public final class EasyMockAnnotations {

    private Object testInstance;
    private Object underTest;
    private IMocksControl mockControl;

    private EasyMockAnnotations() {
    }

    /**
     * Initializes a specific test instance.
     * @param testInstance the test instance to initialize
     */
    public static void initialize(final Object testInstance) {
        try {
            new EasyMockAnnotations().doInitialize(testInstance);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to initialize the test instance.", e);
        }
    }

    private void doInitialize(final Object testInstance) throws ReflectiveOperationException, IllegalArgumentException {
        this.testInstance = testInstance;
        if (initializeMockControl()) {
            initializeBaseMocks();
            initializeUnderTest();
            initializeInjectMocks();
        }
    }

    private boolean initializeMockControl() {
        final Field mockControlField = ReflectionUtils.findField(testInstance.getClass(), "mockControl");
        final boolean hasControl = mockControlField != null && mockControlField.isAnnotationPresent(MockControl.class);
        if (hasControl) {
            mockControl = EasyMock.createStrictControl();
            Whitebox.setInternalState(testInstance, "mockControl", mockControl);
        }
        return hasControl;
    }

    private void initializeUnderTest() throws ReflectiveOperationException, IllegalArgumentException {
        final Field underTestField = ReflectionUtils.findField(testInstance.getClass(), "underTest");
        if (underTestField != null) {
            if (underTestField.isAnnotationPresent(UnderTest.class)) {
                underTest = underTestField.getType().newInstance();
            } else {
                for (final Method method : ReflectionUtils.getAllDeclaredMethods(testInstance.getClass())) {
                    if (method.isAnnotationPresent(UnderTest.class)) {
                        underTest = method.invoke(testInstance);
                        break;
                    }
                }
            }
            Whitebox.setInternalState(testInstance, "underTest", underTest);
        }
    }

    private void initializeBaseMocks() {
        ReflectionUtils.doWithFields(testInstance.getClass(), new InitializingMockFieldCallback(mockControl, testInstance));
    }

    private void initializeInjectMocks() {
        ReflectionUtils.doWithFields(testInstance.getClass(), new InitializingMockFieldCallback(mockControl, testInstance, underTest));
    }
}
