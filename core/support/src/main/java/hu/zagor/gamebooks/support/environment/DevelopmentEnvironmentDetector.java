package hu.zagor.gamebooks.support.environment;

/**
 * Development implementation of the {@link EnvironmentDetector} interface.
 * @author Tamas_Szekeres
 */
public class DevelopmentEnvironmentDetector implements EnvironmentDetector {

    private boolean seleniumTesting;
    private boolean recordState;

    @Override
    public boolean isDevelopment() {
        return !seleniumTesting;
    }

    @Override
    public boolean isRecordState() {
        return recordState;
    }

    @Override
    public void setRecordState(final boolean recordState) {
        this.recordState = recordState;
    }

    @Override
    public boolean isSeleniumTesting() {
        return seleniumTesting;
    }

    @Override
    public void setSeleniumTesting(final boolean seleniumTesting) {
        this.seleniumTesting = seleniumTesting;
    }

}
