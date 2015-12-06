package hu.zagor.gamebooks.mvc.login.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * The user element in the response.
 * @author Tamas_Szekeres
 */
@XmlType
public class User {
    private int id;
    private String roles;

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @XmlAttribute
    public String getRoles() {
        return roles;
    }

    public void setRoles(final String roles) {
        this.roles = roles;
    }
}
