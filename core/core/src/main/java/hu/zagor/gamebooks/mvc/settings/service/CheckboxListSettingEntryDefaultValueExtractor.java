package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.CheckboxListSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntrySub;

import java.util.Map;

import org.springframework.util.Assert;

/**
 * Extractor for {@link CheckboxListSettingEntry} objects.
 * @author Tamas_Szekeres
 */
public class CheckboxListSettingEntryDefaultValueExtractor implements SettingEntryDefaultValueExtractor {

    @Override
    public void extract(final SettingEntry entryObject, final Map<String, String> defaultSettings) {
        Assert.notNull(entryObject, "The parameter 'entryObject' cannot be null!");
        Assert.notNull(defaultSettings, "The parameter 'defaultSettings' cannot be null!");

        final CheckboxListSettingEntry entry = (CheckboxListSettingEntry) entryObject;

        for (final SettingEntrySub sub : entry.getSubEntries()) {
            final String key = sub.getOptionKey();
            final String value = String.valueOf(sub.isDefaultEntry());
            defaultSettings.put(key, value);
        }

    }

}
