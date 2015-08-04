package hu.zagor.gamebooks.player;

import org.springframework.util.Assert;

/**
 * Bean for storing data about the player itself.
 * @author Tamas_Szekeres
 */
public class PlayerUser {

    private final int id;
    private final String username;
    private final boolean admin;
    private final PlayerSettings settings = new PlayerSettings();

    /**
     * Basic constructor that stores the name of the user and it's admin status.
     * @param id the id of the user
     * @param username the name of the user, cannot be null
     * @param admin true if the user is an admin and is allowed to cheat, false otherwise
     */
    public PlayerUser(final int id, final String username, final boolean admin) {
        Assert.notNull(username, "The parameter 'username' cannot be null!");
        Assert.isTrue(id > 0, "The parameter 'id' must be positive!");
        this.id = id;
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public int getId() {
        return id;
    }

    public PlayerSettings getSettings() {
        return settings;
    }

}
