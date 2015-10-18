package hu.zagor.gamebooks.mdc;

import javax.servlet.http.HttpSession;

/**
 * Interface for handling MDC-related operations.
 * @author Tamas_Szekeres
 */
public interface MdcHandler {

    /**
     * Sets a user id both for the MDC and for the {@link HttpSession}.
     * @param value the user id to set
     * @param session the {@link HttpSession} object
     */
    void setUserId(String value, HttpSession session);

    /**
     * Provides a user id for the MDC from the {@link HttpSession}.
     * @param session the {@link HttpSession} object
     * @param bookId the ID of the book, or null if not working with a book
     */
    void provideUserId(HttpSession session, String bookId);

    /**
     * Clears the currently set user id both from MDC and from the {@link HttpSession}.
     * @param session the {@link HttpSession}
     */
    void cleanUserId(HttpSession session);

}
