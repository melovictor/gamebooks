package hu.zagor.gamebooks.player;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link PlayerUser}.
 * @author Tamas_Szekeres
 */
@Test
public class PlayerUserTest {

    private static final int ID = 10;
    private static final boolean ADMIN = false;
    private static final String NAME = "username";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenUsernameIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(ID, null, ADMIN).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNegativeShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(-1, NAME, ADMIN).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsZeroShouldThrowException() {
        // GIVEN
        // WHEN
        new PlayerUser(0, NAME, ADMIN).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenParametersAreGoodShouldCreateBean() {
        // GIVEN
        // WHEN
        final PlayerUser returned = new PlayerUser(ID, NAME, ADMIN);
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getUsername(), NAME);
        Assert.assertEquals(returned.isAdmin(), ADMIN);
    }

}
