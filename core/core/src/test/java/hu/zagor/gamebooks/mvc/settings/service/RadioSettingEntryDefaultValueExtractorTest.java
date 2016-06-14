package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.RadioSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntrySub;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link RadioSettingEntryDefaultValueExtractor}.
 * @author Tamas_Szekeres
 */
@Test
public class RadioSettingEntryDefaultValueExtractorTest {

    private RadioSettingEntryDefaultValueExtractor underTest;
    private Map<String, String> defaultSettings;
    private RadioSettingEntry entryObject;

    @BeforeClass
    private void setUpClass() {
        underTest = new RadioSettingEntryDefaultValueExtractor();
        defaultSettings = new HashMap<>();
        entryObject = new RadioSettingEntry();

        entryObject.setSettingKey("option.radio");
        final List<SettingEntrySub> subEntries = new ArrayList<>();

        final SettingEntrySub nonDefaultEntry = new SettingEntrySub();
        nonDefaultEntry.setDefaultEntry(false);
        nonDefaultEntry.setOptionKey("nonDefault");
        subEntries.add(nonDefaultEntry);

        final SettingEntrySub defaultEntry = new SettingEntrySub();
        nonDefaultEntry.setDefaultEntry(true);
        nonDefaultEntry.setOptionKey("default");
        subEntries.add(defaultEntry);

        entryObject.setSubEntries(subEntries);
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

    public void testExtractWhenInputIsCorrectShouldExtractProperDefaultValue() {
        // GIVEN
        // WHEN
        underTest.extract(entryObject, defaultSettings);
        // THEN
        Assert.assertEquals(defaultSettings.size(), 1);
        Assert.assertEquals(defaultSettings.get("option.radio"), "default");
    }
}
