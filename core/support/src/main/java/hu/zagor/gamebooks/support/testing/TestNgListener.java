package hu.zagor.gamebooks.support.testing;

import hu.zagor.gamebooks.support.mock.EasyMockAnnotations;

import java.util.HashSet;
import java.util.Set;

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
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
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
                EasyMockAnnotations.initialize(instance);
                instances.add(instance);
            }
        }
    }

    @Override
    public void onFinish(final ITestContext context) {
    }

}
