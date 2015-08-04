package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.content.ParagraphData;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CommandResolveResult}.
 * @author Tamas_Szekeres
 */
@Test
public class CommandResolveResultTest {

    private CommandResolveResult underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new CommandResolveResult();
    }

    public void testIsFinishedWhenNothingIsSetShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsFinishedWhenResolveListIsNullShouldReturnFalse() {
        // GIVEN
        underTest.setResolveList(null);
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsFinishedWhenParagraphDataIsSetShouldReturnTrue() {
        // GIVEN
        underTest.add(new ParagraphData());
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsFinishedWhenFinishedSetToTrueShouldReturnTrue() {
        // GIVEN
        underTest.setFinished(true);
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsFinishedWhenFinishedSetToFalseShouldReturnFalse() {
        // GIVEN
        underTest.setFinished(false);
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsFinishedWhenFinishedSetToFalseAndHasParagraphsAddedShouldReturnFalse() {
        // GIVEN
        final List<ParagraphData> dataList = new ArrayList<>();
        dataList.add(new ParagraphData());
        underTest.add(dataList);
        underTest.setFinished(false);
        // WHEN
        final boolean returned = underTest.isFinished();
        // THEN
        Assert.assertFalse(returned);
    }

}
