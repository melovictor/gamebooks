package hu.zagor.gamebooks.exception;

/**
 * Exception for marking the fact that the player did an invalid move.
 * @author Tamas_Szekeres
 */
public class InvalidStepChoiceException extends GamebookError {

    private static final String INVALID_STEP_ERROR = "User tried to move from paragraph '";

    /**
     * Basic constructor that contains information about which step has failed.
     * @param previousParagraphId the id of the originating paragraph
     * @param paragraphId the id of the next paragraph
     */
    public InvalidStepChoiceException(final String previousParagraphId, final String paragraphId) {
        super(INVALID_STEP_ERROR + previousParagraphId + "' to paragraph '" + paragraphId + "'");
    }

    /**
     * Basic constructor that contains information about which step has failed.
     * @param previousParagraphId the id of the originating paragraph
     * @param position the claimed position of the illegal choice
     */
    public InvalidStepChoiceException(final String previousParagraphId, final int position) {
        super(INVALID_STEP_ERROR + previousParagraphId + "' to choice at position '" + position + "'");
    }

}
