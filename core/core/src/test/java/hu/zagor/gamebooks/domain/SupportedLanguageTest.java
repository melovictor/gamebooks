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

    public void testGetCountryCodeWhenNoCountryCodeWasSpecifiedShouldReturnNull() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "Português");
        // WHEN
        final String returned = underTest.getCountryCode();
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetCountryCodeWhenCountryCodeWasSpecifiedShouldReturnCountryCode() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "br", "Português");
        // WHEN
        final String returned = underTest.getCountryCode();
        // THEN
        Assert.assertEquals(returned, "br");
    }

    public void testFlagCodeWhenCountryCodeIsEmptyShouldReturnLocaleCodeOnly() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "Português");
        // WHEN
        final String returned = underTest.getFlagCode();
        // THEN
        Assert.assertEquals(returned, "pt");
    }

    public void testFlagCodeWhenCountryCodeIsNotEmptyShouldReturnLocaleAndCountryCode() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "br", "Português");
        // WHEN
        final String returned = underTest.getFlagCode();
        // THEN
        Assert.assertEquals(returned, "pt-br");
    }

    public void testLocaleFormatWhenCountryCodeIsEmptyShouldReturnLocaleCodeOnly() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "Português");
        // WHEN
        final String returned = underTest.getLocaleFormat();
        // THEN
        Assert.assertEquals(returned, "pt");
    }

    public void testLocaleFormatWhenCountryCodeIsNotEmptyShouldReturnLocaleAndCountryCode() {
        // GIVEN
        final SupportedLanguage underTest = new SupportedLanguage("pt", "br", "Português");
        // WHEN
        final String returned = underTest.getLocaleFormat();
        // THEN
        Assert.assertEquals(returned, "pt_br");
    }

}
