package hu.zagor.gamebooks.domain;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BookInformations}.
 * @author Tamas_Szekeres
 */
@Test
public class BookInformationsTest {

    private BookInformations underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new BookInformations(1L);
    }

    public void testSetPositionShouldSetPosition() {
        // GIVEN
        // WHEN
        underTest.setPosition(99D);
        // THEN
        Assert.assertEquals(underTest.getPosition().intValue(), 99);
    }

    public void testSetCoverPathShouldSetCoverPath() {
        // GIVEN
        // WHEN
        underTest.setCoverPath("resources/ff99/cover.jpg");
        // THEN
        Assert.assertEquals(underTest.getCoverPath(), "resources/ff99/cover.jpg");
    }

    public void testSetResourceDirShouldSetResourceDir() {
        // GIVEN
        // WHEN
        underTest.setResourceDir("resources/ff99");
        // THEN
        Assert.assertEquals(underTest.getResourceDir(), "resources/ff99");
    }

    public void testSetLocaleShouldSetLocale() {
        // GIVEN
        // WHEN
        underTest.setLocale(Locale.CHINESE);
        // THEN
        Assert.assertEquals(underTest.getLocale(), Locale.CHINESE);
    }

    public void testSetParagraphResolverShouldSetParagraphResolver() {
        // GIVEN
        final BookParagraphResolver paragraphResolver = EasyMock.createMock(BookParagraphResolver.class);
        // WHEN
        underTest.setParagraphResolver(paragraphResolver);
        // THEN
        Assert.assertSame(underTest.getParagraphResolver(), paragraphResolver);
    }

    public void testSetCommandResolversShouldSetCommandResolvers() {
        // GIVEN
        final Map<Class<? extends Command>, CommandResolver> commandResolvers = new HashMap<>();
        // WHEN
        underTest.setCommandResolvers(commandResolvers);
        // THEN
        Assert.assertSame(underTest.getCommandResolvers(), commandResolvers);
    }

    public void testSetDisabledShouldSetDisabled() {
        // GIVEN
        // WHEN
        underTest.setDisabled(true);
        // THEN
        Assert.assertTrue(underTest.isDisabled());
    }

    public void testSetHiddenShouldSetHidden() {
        // GIVEN
        // WHEN
        underTest.setHidden(true);
        // THEN
        Assert.assertTrue(underTest.isHidden());
    }

    public void testSetUnfinishedShouldSetUnfinished() {
        // GIVEN
        // WHEN
        underTest.setUnfinished(true);
        // THEN
        Assert.assertTrue(underTest.isUnfinished());
    }

    public void testGetHelpBeanIdShouldReturnHelpBeanId() {
        // GIVEN
        underTest.setHelpBeanId("ff1Help");
        // WHEN
        final String returned = underTest.getHelpBeanId();
        // THEN
        Assert.assertEquals(returned, "ff1Help");
    }

    public void testCompareToWhenPositionIsSmallerShouldCompareByPosition() {
        // GIVEN
        underTest.setPosition(1.0);
        underTest.setTitle("Book 1");
        final BookInformations info = new BookInformations(10L);
        info.setPosition(2.0);
        info.setTitle("Book 2");
        // WHEN
        final int returned = underTest.compareTo(info);
        // THEN
        Assert.assertTrue(returned < 0);
    }

    public void testCompareToWhenPositionIsGreaterShouldCompareByPosition() {
        // GIVEN
        underTest.setPosition(3.0);
        underTest.setTitle("Book 1");
        final BookInformations info = new BookInformations(10L);
        info.setPosition(2.0);
        info.setTitle("Book 2");
        // WHEN
        final int returned = underTest.compareTo(info);
        // THEN
        Assert.assertTrue(returned > 0);
    }

    public void testCompareToWhenPositionIsSameShouldCompareByTitle() {
        // GIVEN
        underTest.setPosition(2.0);
        underTest.setTitle("Book 1");
        final BookInformations info = new BookInformations(10L);
        info.setPosition(2.0);
        info.setTitle("Book 2");
        // WHEN
        final int returned = underTest.compareTo(info);
        // THEN
        Assert.assertTrue(returned < 0);
    }

    public void testCharacterBeanIdShouldSetCharacterBeanId() {
        // GIVEN
        // WHEN
        underTest.setCharacterBeanId("ffCharacter");
        // THEN
        Assert.assertEquals(underTest.getCharacterBeanId(), "ffCharacter");
    }

}
