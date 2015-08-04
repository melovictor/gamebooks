package hu.zagor.gamebooks.player.settings;

import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;
import hu.zagor.gamebooks.mvc.settings.domain.SettingGroupComparator;
import hu.zagor.gamebooks.mvc.settings.service.SettingDefaultValueExtractor;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultDefaultSettingsHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultDefaultSettingsHandlerTest {

    private IMocksControl mockControl;
    private DefaultDefaultSettingsHandler underTest;
    private SettingDefaultValueExtractor valueExtractor;
    private final SettingGroup settingGroupA = new SettingGroup();
    private final SettingGroup settingGroupB = new SettingGroup();
    private final SettingGroup[] settingGroups = new SettingGroup[]{settingGroupA, settingGroupB};

    @BeforeClass
    public void setUpClass() {
        settingGroupA.setGroupPositionIndex(10);
        settingGroupB.setGroupPositionIndex(5);
        mockControl = EasyMock.createStrictControl();
        valueExtractor = mockControl.createMock(SettingDefaultValueExtractor.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultDefaultSettingsHandler();
        Whitebox.setInternalState(underTest, "defaultValueExtractor", valueExtractor);
        Whitebox.setInternalState(underTest, "settingGroups", settingGroups);
        Whitebox.setInternalState(underTest, "orderedSettingGroup", new TreeSet<>(new SettingGroupComparator()));

        mockControl.reset();
    }

    public void testGetOrderedSettingsWhenCalledTheFirstTimeShouldInitializeInnerFieldsAndReturnOrderedSettings() {
        // GIVEN
        @SuppressWarnings("unchecked")
        final Map<String, String> defaultSettings = (Map<String, String>) Whitebox.getInternalState(underTest, "defaultSettings");
        valueExtractor.extract(settingGroupA, defaultSettings);
        valueExtractor.extract(settingGroupB, defaultSettings);
        mockControl.replay();
        // WHEN
        final SortedSet<SettingGroup> returned = underTest.getOrderedSettings();
        // THEN
        Assert.assertEquals(returned.size(), 2);
        Assert.assertTrue(returned.contains(settingGroupA));
        Assert.assertTrue(returned.contains(settingGroupB));
    }

    public void testGetOrderedSettingsWhenCalledThesecondTimeShouldNotReinitializeInnerFieldsJustReturnOrderedSettings() {
        // GIVEN
        @SuppressWarnings("unchecked")
        final Map<String, String> defaultSettings = (Map<String, String>) Whitebox.getInternalState(underTest, "defaultSettings");
        valueExtractor.extract(settingGroupA, defaultSettings);
        valueExtractor.extract(settingGroupB, defaultSettings);
        mockControl.replay();
        // WHEN
        underTest.getOrderedSettings();
        final SortedSet<SettingGroup> returned = underTest.getOrderedSettings();
        // THEN
        Assert.assertEquals(returned.size(), 2);
        Assert.assertTrue(returned.contains(settingGroupA));
        Assert.assertTrue(returned.contains(settingGroupB));
    }

    public void testGetDefaultSettingsWhenCalledTheFirstTimeShouldInitializeInnerFieldsAndReturnDefaultSettings() {
        // GIVEN
        @SuppressWarnings("unchecked")
        final Map<String, String> defaultSettings = (Map<String, String>) Whitebox.getInternalState(underTest, "defaultSettings");
        valueExtractor.extract(settingGroupA, defaultSettings);
        valueExtractor.extract(settingGroupB, defaultSettings);
        mockControl.replay();
        // WHEN
        final Map<String, String> returned = underTest.getDefaultSettings();
        // THEN
        Assert.assertSame(returned, defaultSettings);
    }

    public void testGetDefaultSettingsWhenCalledThesecondTimeShouldNotReinitializeInnerFieldsJustReturnDefaultSettings() {
        // GIVEN
        @SuppressWarnings("unchecked")
        final Map<String, String> defaultSettings = (Map<String, String>) Whitebox.getInternalState(underTest, "defaultSettings");
        valueExtractor.extract(settingGroupA, defaultSettings);
        valueExtractor.extract(settingGroupB, defaultSettings);
        mockControl.replay();
        // WHEN
        underTest.getOrderedSettings();
        final Map<String, String> returned = underTest.getDefaultSettings();
        // THEN
        Assert.assertSame(returned, defaultSettings);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
