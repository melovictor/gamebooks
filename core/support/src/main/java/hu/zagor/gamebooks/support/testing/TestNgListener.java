package hu.zagor.gamebooks.support.testing;

import hu.zagor.gamebooks.support.mock.MockAnnotationInitializer;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * Initializer listener.
 * @author Tamas_Szekeres
 */
public class TestNgListener implements ITestListener {

    @Override
    public void onTestStart(final ITestResult result) {
        final IMocksControl mockControl = getMockControl(result.getInstance());
        if (mockControl != null) {
            mockControl.reset();
        }
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        final IMocksControl mockControl = getMockControl(result.getInstance());
        if (mockControl != null) {
            mockControl.verify();
        }
    }

    private IMocksControl getMockControl(final Object instance) {
        IMocksControl control = null;

        final Set<Field> controlFields = Whitebox.getFieldsOfType(instance, IMocksControl.class);

        if (!controlFields.isEmpty()) {
            final Field field = controlFields.iterator().next();
            if (field.isAnnotationPresent(MockControl.class)) {
                control = Whitebox.getInternalState(instance, field.getName());
            }
        }
        return control;
    }

    @Override
    public void onTestFailure(final ITestResult result) {
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
    }

    @Override
    public void onStart(final ITestContext context) {
        final Set<Object> instances = new HashSet<>();
        for (final ITestNGMethod method : context.getAllTestMethods()) {
            final Object instance = method.getInstance();
            if (!instances.contains(instance)) {
                MockAnnotationInitializer.initialize(instance);
                instances.add(instance);
            }
        }
    }

    @Override
    public void onFinish(final ITestContext context) {
    }

}
