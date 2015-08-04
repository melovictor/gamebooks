package hu.zagor.gamebooks.recording;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;

/**
 * Interface for recording the movement of a character throughout the adventure for later use in the automated tests.
 * @author Tamas_Szekeres
 */
public interface NavigationRecorder {

    /**
     * Method to record the navigation entries when necessary – when the system is in recording mode.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param sectionIdentifier the section identifier with what the recording has been requested
     * @param previousParagraph the previous paragraph object
     * @param paragraph the new paragraph object
     */
    void recordNavigation(HttpSessionWrapper wrapper, String sectionIdentifier, Paragraph previousParagraph, Paragraph paragraph);

}
