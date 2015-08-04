package hu.zagor.gamebooks.mvc.login.domain;

/**
 * Bean for storing the result of the login attempt.
 * @author Tamas_Szekeres
 *
 */
public class LoginResult {
    private String message;
    private int id;
    private boolean successful;
    private boolean admin;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(final boolean successful) {
        this.successful = successful;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
