package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.RadioSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntrySub;

import java.util.Map;

import org.springframework.util.Assert;

/**
 * Extractor for {@link RadioSettingEntry} objects.
 * @author Tamas_Szekeres
 */
public class RadioSettingEntryDefaultValueExtractor implements SettingEntryDefaultValueExtractor {

    @Override
    public void extract(final SettingEntry entryObject, final Map<String, String> defaultSettings) {
        Assert.notNull(entryObject, "The parameter 'entryObject' cannot be null!");
        Assert.notNull(defaultSettings, "The parameter 'defaultSettings' cannot be null!");

        final RadioSettingEntry entry = (RadioSettingEntry) entryObject;
        final String key = entry.getSettingKey();
        String value = null;
        for (final SettingEntrySub sub : entry.getSubEntries()) {
            if (sub.isDefaultEntry()) {
                value = sub.getOptionKey();
            }
        }
        defaultSettings.put(key, value);
    }

}
