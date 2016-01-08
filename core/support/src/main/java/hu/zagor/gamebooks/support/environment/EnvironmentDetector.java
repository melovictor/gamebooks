package hu.zagor.gamebooks.support.environment;

/**
 * Interface for detecting what environment we're running on.
 * @author Tamas_Szekeres
 */
public interface EnvironmentDetector {

    /**
     * Checks whether we're running in development mode.
     * @return true if currently we're in development mode, false otherwise
     */
    boolean isDevelopment();

    /**
     * Check whether we're running in testing mode.
     * @return true if we're in testing mode, false otherwise
     */
    boolean isSeleniumTesting();

    /**
     * Sets selenium testing state.
     * @param seleniumTesting should be true when selenium testing starts and false when it ends
     */
    void setSeleniumTesting(boolean seleniumTesting);

    /**
     * Checks if we're in recording state or not.
     * @return true if we're in recording state, false otherwise
     */
    boolean isRecordState();

    /**
     * Sets the record state.
     * @param recordState the new record state
     */
    void setRecordState(boolean recordState);

}
