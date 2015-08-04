package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.CheckboxSettingEntry;
import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;

import java.util.Map;

import org.springframework.util.Assert;

/**
 * Extractor for {@link CheckboxSettingEntry} objects.
 * @author Tamas_Szekeres
 */
public class CheckboxSettingEntryDefaultValueExtractor implements SettingEntryDefaultValueExtractor {

    @Override
    public void extract(final SettingEntry entryObject, final Map<String, String> defaultSettings) {
        Assert.notNull(entryObject, "The parameter 'entryObject' cannot be null!");
        Assert.notNull(defaultSettings, "The parameter 'defaultSettings' cannot be null!");

        final CheckboxSettingEntry entry = (CheckboxSettingEntry) entryObject;
        final String key = entry.getSettingKey();
        final String value = String.valueOf(entry.isDefaultState());
        defaultSettings.put(key, value);
    }

}
