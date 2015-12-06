package hu.zagor.gamebooks.player;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * Bean for storing data about the player itself.
 * @author Tamas_Szekeres
 */
public class PlayerUser extends UsernamePasswordAuthenticationToken {
    public static final GrantedAuthority USER = new SimpleGrantedAuthority("USER");
    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ADMIN");
    public static final GrantedAuthority TEST = new SimpleGrantedAuthority("TEST");

    private final int id;
    private final PlayerSettings settings = new PlayerSettings();

    /**
     * Creates a new {@link PlayerUser} that also acts as the {@link Authentication} object.
     * @param principal the login name of the user
     * @param authorities the granted authorities for the user
     * @param id the id of the user
     */
    public PlayerUser(final int id, final Object principal, final Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
        this.id = id;
    }

    /**
     * Creates a new {@link PlayerUser} for testing purposes. Should not be used in live code.
     * @param id the id of the user
     * @param principal the login name of the user
     * @param isAdmin true if the user should be admin, false otherwise
     */
    public PlayerUser(final int id, final String principal, final boolean isAdmin) {
        this(id, principal, isAdmin ? Arrays.asList(USER, TEST, ADMIN) : Arrays.asList(USER));
        Assert.notNull(principal, "The parameter 'principal' cannot be null!");
        Assert.isTrue(id > 0, "The parameter 'id' must be positive!");
    }

    public boolean isAdmin() {
        return getAuthorities().contains(ADMIN);
    }

    public boolean isTester() {
        return getAuthorities().contains(TEST);
    }

    public int getId() {
        return id;
    }

    public PlayerSettings getSettings() {
        return settings;
    }

}
