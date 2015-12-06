package hu.zagor.gamebooks.mvc.login.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root element of the response from the server.
 * @author Tamas_Szekeres
 */
@XmlRootElement
public class Login {
    private LoginStatus status;
    private User user;

    @XmlElement
    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(final LoginStatus status) {
        this.status = status;
    }

    @XmlElement(required = false)
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

}
