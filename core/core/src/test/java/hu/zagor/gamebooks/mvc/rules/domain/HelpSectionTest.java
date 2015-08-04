package hu.zagor.gamebooks.mvc.rules.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class HelpSection.
 * @author Tamas_Szekeres
 */
@Test
public class HelpSectionTest {

    private final HelpSection underTest = new HelpSection();

    public void testGetJspShouldReturnJsp() {
        // GIVEN
        underTest.setJsp("intro");
        // WHEN
        final String returned = underTest.getJsp();
        // THEN
        Assert.assertEquals(returned, "intro");
    }

    public void testGetLocationShouldReturnLocation() {
        // GIVEN
        underTest.setLocation("ff");
        // WHEN
        final String returned = underTest.getLocation();
        // THEN
        Assert.assertEquals(returned, "ff");
    }
}
