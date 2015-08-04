package hu.zagor.gamebooks.controller.domain;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ImageLocation}.
 * @author Tamas_Szekeres
 */
@Test
public class ImageLocationTest {

    private static final String HU = "hu";
    private static final String FILE = "fileName";
    private static final String DIR = "dirName";
    private final Locale locale = new Locale(HU);

    public void testConstructorWhenParametersAreProperlySetShouldCreateProperImageLocation() {
        // GIVEN
        // WHEN
        final ImageLocation returned = new ImageLocation(DIR, FILE, locale);
        // THEN
        Assert.assertEquals(returned.getDir(), DIR);
        Assert.assertEquals(returned.getFile(), FILE);
        Assert.assertEquals(returned.getLocale(), locale);
        Assert.assertEquals(returned.getDirLocale(), DIR + HU);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenDirIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new ImageLocation(null, FILE, locale).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenFileIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new ImageLocation(null, FILE, locale).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenLocaleIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new ImageLocation(DIR, null, null).getClass();
        // THEN throws exception
    }

}
