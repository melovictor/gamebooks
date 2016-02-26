package hu.zagor.gamebooks.mvc.login.domain;

import hu.zagor.gamebooks.player.Reward;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The user element in the response.
 * @author Tamas_Szekeres
 */
@XmlType
public class User {
    private int id;
    private String roles;
    private final List<Reward> reward = new ArrayList<>();

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

    @XmlElement(type = Reward.class)
    public List<Reward> getReward() {
        return reward;
    }

}
