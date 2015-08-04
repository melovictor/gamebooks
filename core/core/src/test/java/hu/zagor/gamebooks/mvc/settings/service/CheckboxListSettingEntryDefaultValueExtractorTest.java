package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.CheckboxListSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntrySub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CheckboxListSettingEntryDefaultValueExtractor}.
 * @author Tamas_Szekeres
 */
@Test
public class CheckboxListSettingEntryDefaultValueExtractorTest {

    private CheckboxListSettingEntryDefaultValueExtractor underTest;
    private Map<String, String> defaultSettings;
    private CheckboxListSettingEntry entryObject;
    private SettingEntrySub entryA;
    private SettingEntrySub entryB;

    @BeforeClass
    public void setUpClass() {
        underTest = new CheckboxListSettingEntryDefaultValueExtractor();
        defaultSettings = new HashMap<>();

        entryObject = new CheckboxListSettingEntry();

        entryA = new SettingEntrySub();
        entryB = new SettingEntrySub();

        entryA.setDefaultEntry(true);
        entryA.setOptionKey("option.true");
        entryA.setTitleKey("setting.title.A");
        entryB.setDefaultEntry(false);
        entryB.setOptionKey("option.false");
        entryB.setTitleKey(entryA.getTitleKey() + "2");

        final List<SettingEntrySub> subEntries = new ArrayList<>();
        subEntries.add(entryA);
        subEntries.add(entryB);
        entryObject.setSubEntries(subEntries);
    }

    @BeforeMethod
    public void setUpMethod() {
        defaultSettings.clear();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExtractWhenEntryObjectIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.extract(null, defaultSettings);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExtractWhenDefaultSettingsIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.extract(entryObject, null);
        // THEN throws exception
    }

    public void testExtractWhenInputIsCorrectShouldCollectAllDefaultValues() {
        // GIVEN
        // WHEN
        underTest.extract(entryObject, defaultSettings);
        // THEN
        Assert.assertEquals(defaultSettings.size(), 2);
        Assert.assertEquals(defaultSettings.get("option.true"), "true");
        Assert.assertEquals(defaultSettings.get("option.false"), "false");
    }

}
