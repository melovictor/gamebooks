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
     * @param session the session that has the user id attribute set
     */
    void provideUserId(HttpSession session);

    /**
     * Clears the currently set user id both from MDC and from the {@link HttpSession}.
     * @param session the {@link HttpSession}
     */
    void cleanUserId(HttpSession session);

}
