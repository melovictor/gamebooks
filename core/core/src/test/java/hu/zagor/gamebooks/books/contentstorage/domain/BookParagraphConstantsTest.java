package hu.zagor.gamebooks.books.contentstorage.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BookParagraphConstants}.
 * @author Tamas_Szekeres
 */
@Test
public class BookParagraphConstantsTest {

    public void testValueOfWhenBackgroundRequestedReturnsBackground() {
        // GIVEN
        // WHEN
        final BookParagraphConstants returned = BookParagraphConstants.valueOf("BACKGROUND");
        // THEN
        Assert.assertSame(returned, BookParagraphConstants.BACKGROUND);
        Assert.assertEquals(returned.getValue(), "background");
    }

    public void testValueOfWhenBackCoverRequestedReturnsBackCover() {
        // GIVEN
        // WHEN
        final BookParagraphConstants returned = BookParagraphConstants.valueOf("BACK_COVER");
        // THEN
        Assert.assertSame(returned, BookParagraphConstants.BACK_COVER);
        Assert.assertEquals(returned.getValue(), "back_cover");
    }
}
