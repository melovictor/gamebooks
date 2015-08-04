package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.CheckboxSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.RadioSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultSettingDefaultValueExtractor}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultSettingDefaultValueExtractorTest {

    private IMocksControl mockControl;
    private DefaultSettingDefaultValueExtractor underTest;

    private Map<String, String> defaultSettings;
    private SettingGroup existingGroup;
    private SettingGroup nonexistingGroup;
    private Logger logger;
    private SettingEntryDefaultValueExtractor extractor;

    private SettingEntry existingEntry;
    private SettingEntry nonexistingEntry;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new DefaultSettingDefaultValueExtractor();
        logger = mockControl.createMock(Logger.class);
        extractor = mockControl.createMock(SettingEntryDefaultValueExtractor.class);

        existingGroup = new SettingGroup();
        List<SettingEntry> settingEntries = new ArrayList<>();
        existingEntry = new RadioSettingEntry();
        existingEntry.setSettingNameKey("label.key.entry." + existingEntry.getSettingType());
        settingEntries.add(existingEntry);
        existingGroup.setSettingEntries(settingEntries);
        existingGroup.setGroupNameKey("label.key.group.existent");
        existingGroup.setAdminOnly(true);

        nonexistingGroup = new SettingGroup();
        settingEntries = new ArrayList<>();
        nonexistingEntry = new CheckboxSettingEntry();
        nonexistingEntry.setSettingNameKey(existingEntry.getSettingNameKey() + ".not");
        settingEntries.add(nonexistingEntry);
        nonexistingGroup.setSettingEntries(settingEntries);
        nonexistingGroup.setGroupNameKey(existingGroup.getGroupNameKey() + ".not");
        nonexistingGroup.setAdminOnly(!existingGroup.isAdminOnly());

        final Map<Class<? extends SettingEntry>, SettingEntryDefaultValueExtractor> extractors = new HashMap<>();
        extractors.put(RadioSettingEntry.class, extractor);

        Whitebox.setInternalState(underTest, "extractors", extractors);
        Whitebox.setInternalState(underTest, "logger", logger);

        defaultSettings = new HashMap<>();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExtractWhenGroupIsNullShouldThrowExceptino() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.extract(null, defaultSettings);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testExtractWhenDefaultSettingsIsNullShouldThrowExceptino() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.extract(existingGroup, null);
        // THEN throws exception
    }

    public void testExtractWhenGroupContainsEntryWithExistingExtractorShouldExtractDefaultValue() {
        // GIVEN
        extractor.extract(existingEntry, defaultSettings);
        mockControl.replay();
        // WHEN
        underTest.extract(existingGroup, defaultSettings);
        // THEN
    }

    public void testExtractWhenGroupContainsEntryWithNonexistingExtractorShouldReportProblem() {
        // GIVEN
        logger.error("Couldn't find logger for entry {}.", nonexistingEntry.getClass());
        mockControl.replay();
        // WHEN
        underTest.extract(nonexistingGroup, defaultSettings);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
