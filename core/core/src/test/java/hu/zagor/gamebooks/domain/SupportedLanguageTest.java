package hu.zagor.gamebooks.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SupportedLanguage}.
 * @author Tamas_Szekeres
 */
@Test
public class SupportedLanguageTest {

    public void testGetLocaleCodeShouldReturnLocaleCode() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("en", "English");
        // WHEN
        final String returned = underTest.getLocaleCode();
        // THEN
        Assert.assertEquals(returned, "en");
    }

    public void testGetSelfNameShouldReturnSelfName() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("en", "English");
        // WHEN
        final String returned = underTest.getSelfName();
        // THEN
        Assert.assertEquals(returned, "English");
    }
}
