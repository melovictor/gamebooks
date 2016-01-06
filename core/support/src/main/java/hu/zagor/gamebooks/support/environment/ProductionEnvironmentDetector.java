package hu.zagor.gamebooks.support.environment;

/**
 * Production implementation of the {@link EnvironmentDetector} interface.
 * @author Tamas_Szekeres
 */
public class ProductionEnvironmentDetector implements EnvironmentDetector {

    @Override
    public boolean isDevelopment() {
        return false;
    }

    @Override
    public boolean isRecordState() {
        return false;
    }

    @Override
    public void setRecordState(final boolean recordState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSeleniumTesting() {
        return false;
    }

    @Override
    public void setSeleniumTesting(final boolean seleniumTesting) {
    }

}
