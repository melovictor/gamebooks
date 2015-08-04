package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class for extracting the default values from the setting entries.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultSettingDefaultValueExtractor implements SettingDefaultValueExtractor {

    @LogInject
    private Logger logger;
    @Resource(name = "settingEntryExtractors")
    private Map<Class<? extends SettingEntry>, SettingEntryDefaultValueExtractor> extractors;

    @Override
    public void extract(final SettingGroup group, final Map<String, String> defaultSettings) {
        Assert.notNull(group, "The parameter 'group' cannot be null!");
        Assert.notNull(defaultSettings, "The parameter 'defaultSettings' cannot be null!");

        for (final SettingEntry entry : group.getSettingEntries()) {
            extract(entry, defaultSettings);
        }
    }

    private void extract(final SettingEntry entry, final Map<String, String> defaultSettings) {
        final SettingEntryDefaultValueExtractor extractor = extractors.get(entry.getClass());
        if (extractor == null) {
            logger.error("Couldn't find logger for entry {}.", entry.getClass());
        } else {
            extractor.extract(entry, defaultSettings);
        }
    }

}
