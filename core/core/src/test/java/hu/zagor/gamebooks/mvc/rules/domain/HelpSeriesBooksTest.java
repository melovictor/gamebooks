package hu.zagor.gamebooks.mvc.rules.domain;

import hu.zagor.gamebooks.domain.BookInformations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link HelpSeriesBooks}.
 * @author Tamas_Szekeres
 */
@Test
public class HelpSeriesBooksTest {

    private HelpSeriesBooks underTest;
    private BookInformations info;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new HelpSeriesBooks();
        info = new BookInformations(1L);
        info.setPosition(1.0);
        info.setTitle("Book title");
    }

    public void testAddShouldAddEntry() {
        // GIVEN
        // WHEN
        underTest.add(info);
        // THEN
        Assert.assertTrue(underTest.getEntries().contains(info));
    }

    public void testGetSeriesTitleShouldReturnSeriesTitle() {
        // GIVEN
        underTest.setSeriesTitle("Series title");
        // WHEN
        final String returned = underTest.getSeriesTitle();
        // THEN
        Assert.assertEquals(returned, "Series title");
    }

    public void testGetLocaleShouldReturnLocale() {
        // GIVEN
        underTest.setLocale("en");
        // WHEN
        final String returned = underTest.getLocale();
        // THEN
        Assert.assertEquals(returned, "en");
    }

    public void testCompareToShouldCompareByTitle() {
        // GIVEN
        underTest.setSeriesTitle("Book 1");
        final HelpSeriesBooks other = new HelpSeriesBooks();
        other.setSeriesTitle("Book 2");
        // WHEN
        final int returned = underTest.compareTo(other);
        // THEN
        Assert.assertTrue(returned < 0);
    }
}
