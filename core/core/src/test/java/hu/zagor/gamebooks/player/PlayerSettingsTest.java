package hu.zagor.gamebooks.player;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link PlayerSettings}.
 * @author Tamas_Szekeres
 */
@Test
public class PlayerSettingsTest {

    public void testSetDefaultLanguageShouldSetDefaultLanguage() {
        // GIVEN
        final PlayerSettings underTest = new PlayerSettings();
        // WHEN
        underTest.setDefaultLanguage("en");
        // THEN
        Assert.assertEquals(underTest.getDefaultLanguage(), "en");
    }

}
