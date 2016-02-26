package hu.zagor.gamebooks.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private Map<Long, Set<String>> rewards;

    /**
     * Creates a new {@link PlayerUser} that also acts as the {@link Authentication} object.
     * @param principal the login name of the user
     * @param authorities the granted authorities for the user
     * @param id the id of the user
     * @param rewards the rewards the player has at the time of logging in
     */
    public PlayerUser(final int id, final Object principal, final Collection<? extends GrantedAuthority> authorities, final Collection<Reward> rewards) {
        super(principal, null, authorities);
        this.id = id;
        parseRewards(rewards);
    }

    /**
     * Creates a new {@link PlayerUser} for testing purposes. Should not be used in live code.
     * @param id the id of the user
     * @param principal the login name of the user
     * @param isAdmin true if the user should be admin, false otherwise
     */
    public PlayerUser(final int id, final String principal, final boolean isAdmin) {
        this(id, principal, isAdmin ? Arrays.asList(USER, TEST, ADMIN) : Arrays.asList(USER), new ArrayList<Reward>());
        Assert.notNull(principal, "The parameter 'principal' cannot be null!");
        Assert.isTrue(id > 0, "The parameter 'id' must be positive!");
    }

    /**
     * Creates a new {@link PlayerUser} that also acts as the {@link Authentication} object.
     * @param principal the login name of the user
     * @param authorities the granted authorities for the user
     * @param id the id of the user
     */
    public PlayerUser(final Integer id, final Object principal, final List<GrantedAuthority> authorities) {
        this(id, principal, authorities, new ArrayList<Reward>());
    }

    private void parseRewards(final Collection<Reward> rewards) {
        for (final Reward reward : rewards) {
            provideSet(reward.getBookId()).add(reward.getCode());
        }
    }

    private Set<String> provideSet(final long bookId) {
        if (!rewards.containsKey(bookId)) {
            rewards.put(bookId, new HashSet<String>());
        }
        return rewards.get(bookId);
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
