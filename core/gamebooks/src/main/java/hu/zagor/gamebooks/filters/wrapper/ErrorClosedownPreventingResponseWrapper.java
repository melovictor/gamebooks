package hu.zagor.gamebooks.filters.wrapper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrapper around the response so we can still hijack it after an error has been set to it.
 * @author Tamas_Szekeres
 */
public class ErrorClosedownPreventingResponseWrapper extends HttpServletResponseWrapper {

    private Integer statusCode;
    private String message;

    /**
     * Basic constructor.
     * @param response the {@link HttpServletResponse} to wrap up
     */
    public ErrorClosedownPreventingResponseWrapper(final HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(final int statusCode, final String message) throws IOException {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public void sendError(final int statusCode) throws IOException {
        this.statusCode = statusCode;
    }

    /**
     * Approves the error code that has been set to this message.
     * @throws IOException if an input or output exception occurs
     */
    public void approveSendError() throws IOException {
        if (statusCode != null) {
            if (message == null) {
                super.sendError(statusCode);
            } else {
                super.sendError(statusCode, message);
            }
        }
    }

    /**
     * Checks if the current status code is the same as a specific status code.
     * @param statusCode the code against we check
     * @return true if the current status code is the same as the provided one, false otherwise
     */
    public boolean isStatusCode(final int statusCode) {
        return this.statusCode != null && this.statusCode == statusCode;
    }
}
