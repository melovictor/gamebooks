package hu.zagor.gamebooks.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ContinuationData}.
 * @author Tamas_Szekeres
 */
@Test
public class ContinuationDataTest {
    private final ContinuationData underTest = new ContinuationData();
    private final BookInformations info = new BookInformations(996);

    public void testGetCurrentBookLastSectionIdShouldReturnSetValue() {
        // GIVEN
        underTest.setCurrentBookLastSectionId("35");
        // WHEN
        final String returned = underTest.getCurrentBookLastSectionId();
        // THEN
        Assert.assertEquals(returned, "35");
    }

    public void testGetNextBookIdShouldReturnSetValue() {
        // GIVEN
        underTest.setNextBookId(996);
        // WHEN
        final long returned = underTest.getNextBookId();
        // THEN
        Assert.assertEquals(returned, 996L);
    }

    public void testGetNextBookInfoShouldReturnSetValue() {
        // GIVEN
        underTest.setNextBookInfo(info);
        // WHEN
        final BookInformations returned = underTest.getNextBookInfo();
        // THEN
        Assert.assertEquals(returned, info);
    }

}
