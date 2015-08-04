package hu.zagor.gamebooks.player.settings;

import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;
import hu.zagor.gamebooks.mvc.settings.service.SettingDefaultValueExtractor;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link DefaultSettingsHandler} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultDefaultSettingsHandler implements DefaultSettingsHandler {

    @Autowired
    private SettingGroup[] settingGroups;
    @Resource(name = "orderedSettingSet")
    private SortedSet<SettingGroup> orderedSettingGroup;
    @Autowired
    private SettingDefaultValueExtractor defaultValueExtractor;

    private final Map<String, String> defaultSettings = new HashMap<>();

    @Override
    public SortedSet<SettingGroup> getOrderedSettings() {
        if (orderedSettingGroup.isEmpty()) {
            initializeFields();
        }

        return orderedSettingGroup;
    }

    @Override
    public Map<String, String> getDefaultSettings() {
        if (orderedSettingGroup.isEmpty()) {
            initializeFields();
        }

        return defaultSettings;
    }

    private void initializeFields() {
        for (final SettingGroup group : settingGroups) {
            orderedSettingGroup.add(group);
            defaultValueExtractor.extract(group, defaultSettings);
        }
    }
}
