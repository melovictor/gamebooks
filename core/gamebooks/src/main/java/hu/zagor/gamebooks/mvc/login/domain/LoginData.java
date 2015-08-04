package hu.zagor.gamebooks.mvc.login.domain;

import hu.zagor.gamebooks.mvc.login.controller.LoginController;

/**
 * Bean for transferring user login data from the login page back to the {@link LoginController}.
 * @author Tamas_Szekeres
 */
public class LoginData {

    private String username;
    private String password;
    private String guid;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

}
