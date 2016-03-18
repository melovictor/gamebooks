package hu.zagor.gamebooks.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link PlayerUser}.
 * @author Tamas_Szekeres
 */
@Test
public class PlayerUserTest {

    private static final int ID = 10;
    private static final String NAME = "username";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenUsernameIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(ID, null, false).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNegativeShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(-1, NAME, false).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsZeroShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(0, NAME, false).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenParametersAreNonAdminGoodShouldCreateBean() {
        // GIVEN
        // WHEN
        final PlayerUser returned = new PlayerUser(ID, NAME, false);
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getPrincipal(), NAME);
        Assert.assertEquals(returned.isAdmin(), false);
        Assert.assertEquals(returned.isTester(), false);
    }

    public void testConstructorWhenParametersAreAdminGoodShouldCreateBean() {
        // GIVEN
        // WHEN
        final PlayerUser returned = new PlayerUser(ID, NAME, true);
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getPrincipal(), NAME);
        Assert.assertEquals(returned.isAdmin(), true);
        Assert.assertEquals(returned.isTester(), true);
    }

    public void testConstructorWhenCreatedWithAuthoritiesWithoutRewardsShouldCreateBean() {
        // GIVEN
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        // WHEN
        final PlayerUser returned = new PlayerUser(ID, NAME, authorities);
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getPrincipal(), NAME);
        Assert.assertEquals(returned.isAdmin(), true);
        Assert.assertEquals(returned.isTester(), false);
    }

    public void testConstructorWhenCreatedWithAuthoritiesWithRewardsShouldCreateBean() {
        // GIVEN
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("TEST"));
        final Collection<Reward> rewards = new HashSet<>();
        rewards.add(getReward(1L, "sword"));
        rewards.add(getReward(1L, "shield"));
        rewards.add(getReward(2L, "vacuumcleaner"));
        // WHEN
        final PlayerUser returned = new PlayerUser(ID, NAME, authorities, rewards);
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getPrincipal(), NAME);
        Assert.assertEquals(returned.isAdmin(), false);
        Assert.assertEquals(returned.isTester(), true);
        Assert.assertEquals(returned.getRewards().size(), 2);
        Assert.assertEquals(returned.getRewards().get(1L), new HashSet<>(Arrays.asList("sword", "shield")));
        Assert.assertEquals(returned.getRewards().get(2L), new HashSet<>(Arrays.asList("vacuumcleaner")));
    }

    public void testAddRewardWhenAddingNewRewardItShouldAppearInRewardList() {
        // GIVEN
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("TEST"));
        final Collection<Reward> rewards = new HashSet<>();
        rewards.add(getReward(1L, "sword"));
        rewards.add(getReward(1L, "shield"));
        rewards.add(getReward(2L, "vacuumcleaner"));
        final PlayerUser underTest = new PlayerUser(ID, NAME, authorities, rewards);
        // WHEN
        underTest.addReward(2L, "blaster");
        // THEN
        Assert.assertEquals(underTest.getRewards().size(), 2);
        Assert.assertEquals(underTest.getRewards().get(1L), new HashSet<>(Arrays.asList("sword", "shield")));
        Assert.assertEquals(underTest.getRewards().get(2L), new HashSet<>(Arrays.asList("vacuumcleaner", "blaster")));
    }

    public void testGetSettingsShouldReturnSettingsObject() {
        // GIVEN
        final PlayerUser underTest = new PlayerUser(ID, NAME, false);
        // WHEN
        final PlayerSettings returned = underTest.getSettings();
        // THEN
        Assert.assertNotNull(returned);
    }

    private Reward getReward(final long bookId, final String code) {
        final Reward reward = new Reward();
        reward.setBookId(bookId);
        reward.setCode(code);
        return reward;
    }

}
