package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.CheckboxSettingEntry;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CheckboxSettingEntryDefaultValueExtractor}.
 * @author Tamas_Szekeres
 */
@Test
public class CheckboxSettingEntryDefaultValueExtractorTest {

    private CheckboxSettingEntryDefaultValueExtractor underTest;
    private Map<String, String> defaultSettings;
    private CheckboxSettingEntry defaultEntryObject;
    private CheckboxSettingEntry nonDefaultEntryObject;

    @BeforeClass
    public void setUpClass() {
        underTest = new CheckboxSettingEntryDefaultValueExtractor();
        defaultSettings = new HashMap<>();

        defaultEntryObject = new CheckboxSettingEntry();
        defaultEntryObject.setDefaultState(true);
        defaultEntryObject.setSettingKey("option.true");

        nonDefaultEntryObject = new CheckboxSettingEntry();
        nonDefaultEntryObject.setDefaultState(false);
        nonDefaultEntryObject.setSettingKey("option.false");
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
        underTest.extract(defaultEntryObject, null);
        // THEN throws exception
    }

    public void testExtractWhenDefaultEntryObjectInputShouldCollectTrueDefaultValue() {
        // GIVEN
        // WHEN
        underTest.extract(defaultEntryObject, defaultSettings);
        // THEN
        Assert.assertEquals(defaultSettings.size(), 1);
        Assert.assertEquals(defaultSettings.get("option.true"), "true");
    }

    public void testExtractWhenNonDefaultEntryObjectInputShouldCollectFalseDefaultValue() {
        // GIVEN
        // WHEN
        underTest.extract(nonDefaultEntryObject, defaultSettings);
        // THEN
        Assert.assertEquals(defaultSettings.size(), 1);
        Assert.assertEquals(defaultSettings.get("option.false"), "false");
    }

}
