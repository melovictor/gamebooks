package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;

import java.util.Locale;

import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.HierarchicalMessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultPositionManipulator}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultPositionManipulatorTest {

    @MockControl
    private IMocksControl mockControl;
    @UnderTest
    private DefaultPositionManipulator underTest;
    @Inject
    private HierarchicalMessageSource messageSource;
    @Inject
    private LocaleProvider localeProvider;
    @Mock
    private Character character;
    @Mock
    private FfUserInteractionHandler interactionHandler;
    @Mock
    private HuntRoundResult result;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(dataProvider = "cssConvertData")
    public void testCssClassFromPositionWhenRangeProvidedShouldConvertProperly(final String in, final String out) {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.cssClassFromPosition(in);
        // THEN
        Assert.assertEquals(returned, out);
    }

    @DataProvider(name = "cssConvertData")
    public Object[][] getCssConvertData() {
        return new Object[][]{{"E6", "gE g6"}, {"I5", "gX"}, {"B0", "gX"}, {"A13", "gX"}, {"@12", "gX"}};
    }

    public void testSaveDataWhenCalledShouldSaveToInteractionHandler() {
        // GIVEN
        interactionHandler.setInteractionState(character, "huntRound", "8");
        expect(result.getTigerPosition()).andReturn("H11");
        interactionHandler.setInteractionState(character, "tigerPosition", "H11");
        expect(result.getDogPosition()).andReturn("B6");
        interactionHandler.setInteractionState(character, "dogPosition", "B6");
        mockControl.replay();
        // WHEN
        underTest.saveData(character, interactionHandler, 7, result);
        // THEN
    }

    public void testGetRoundWhenDataNotAvailableShouldReturnWithOne() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "huntRound")).andReturn(null);
        mockControl.replay();
        // WHEN
        final int returned = underTest.getRound(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testGetRoundWhenDataIsAvailableShouldReturnIt() {
        // GIVEN
        expect(interactionHandler.getInteractionState(character, "huntRound")).andReturn("3");
        mockControl.replay();
        // WHEN
        final int returned = underTest.getRound(character, interactionHandler);
        // THEN
        Assert.assertEquals(returned, 3);
    }

    public void testVerifyPositionWhenAtC9ShouldFinishHunt() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("C9");
        result.setHuntFinished(true);
        result.setNextSectionId("262");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 4);
        // THEN
    }

    @Test(dataProvider = "tigerCaughtUpWithData")
    public void testVerifyPositionWhenOnOrAroundTigerShouldFinishHunt(final String dogPosition) {
        // GIVEN
        expect(result.getDogPosition()).andReturn(dogPosition).times(2);
        expect(result.getTigerPosition()).andReturn("B11");
        result.setHuntFinished(true);
        result.setNextSectionId("371");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 4);
        // THEN
    }

    @DataProvider(name = "tigerCaughtUpWithData")
    public Object[][] getTigerCaughtData() {
        return new Object[][]{{"B11"}, {"B12"}, {"B10"}, {"A11"}, {"C11"}, {"A10"}, {"A12"}, {"C10"}, {"C12"}};
    }

    @Test(dataProvider = "outOfAreaData")
    public void testVerifyPositionWhenTigerOutOfMapShouldFinishHunt(final String tigerPosition) {
        // GIVEN
        expect(result.getDogPosition()).andReturn("D6").times(2);
        expect(result.getTigerPosition()).andReturn(tigerPosition).times(2);
        result.setHuntFinished(true);
        result.setNextSectionId("177");
        expect(result.getRoundMessage()).andReturn("Tiger rolled 3.<br />");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.fled", null, Locale.ENGLISH)).andReturn("Tiger fled.");
        result.setRoundMessage("Tiger rolled 3.<br />Tiger fled.<br />");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 4);
        // THEN
    }

    @Test(dataProvider = "outOfAreaData")
    public void testVerifyPositionWhenDogOutOfMapShouldFinishHunt(final String dogPosition) {
        // GIVEN
        expect(result.getDogPosition()).andReturn(dogPosition).times(2);
        expect(result.getTigerPosition()).andReturn("D6").times(2);
        expect(result.getDogPosition()).andReturn(dogPosition);
        result.setHuntFinished(true);
        result.setNextSectionId("177");
        expect(result.getRoundMessage()).andReturn("Dog rolled 3.<br />");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.dog.lost", null, Locale.ENGLISH)).andReturn("Dog lost.");
        result.setRoundMessage("Dog rolled 3.<br />Dog lost.<br />");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 4);
        // THEN
    }

    @DataProvider(name = "outOfAreaData")
    public Object[][] getOutOfAreaData() {
        return new Object[][]{{"D0"}, {"D13"}, {"I6"}, {"@6"}};
    }

    public void testVerifyPositionWhenOutOfTimeShouldFinishRound() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("B12").times(2);
        expect(result.getTigerPosition()).andReturn("D6").times(2);
        expect(result.getDogPosition()).andReturn("B12");
        result.setHuntFinished(true);
        result.setNextSectionId("177");
        expect(result.getRoundMessage()).andReturn("Dog rolled 3.<br />");
        expect(localeProvider.getLocale()).andReturn(Locale.ENGLISH);
        expect(messageSource.getMessage("page.ff23.hunt.tiger.timeout", null, Locale.ENGLISH)).andReturn("Tiger survived.");
        result.setRoundMessage("Dog rolled 3.<br />Tiger survived.<br />");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 16);
        // THEN
    }

    public void testVerifyPositionWhenNothingUntowardsHappensShouldDoNothing() {
        // GIVEN
        expect(result.getDogPosition()).andReturn("B12").times(2);
        expect(result.getTigerPosition()).andReturn("D6").times(2);
        expect(result.getDogPosition()).andReturn("B12");
        mockControl.replay();
        // WHEN
        underTest.verifyPositions(result, 9);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
