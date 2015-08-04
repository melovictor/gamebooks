package hu.zagor.gamebooks.player.settings;

import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link UserSettingsHandler} interface.
 * @author Tamas_Szekeres
 */
public class DefaultUserSettingsHandler implements UserSettingsHandler, BeanFactoryAware {

    private static final String UTF_8 = "UTF-8";
    private static final String SETTINGS_PROPERTIES = "settings.properties";
    private static final String PLAYER_NOT_NULL = "The parameter 'player' cannot be null!";
    @LogInject
    private Logger logger;
    private BeanFactory beanFactory;
    @Autowired
    private DirectoryProvider directoryProvider;

    @Override
    public void loadSettings(final PlayerUser player) {
        Assert.notNull(player, PLAYER_NOT_NULL);
        final PlayerSettings settings = player.getSettings();
        final File file = (File) beanFactory.getBean("file", getSaveDirectory(), player.getId() + "/" + SETTINGS_PROPERTIES);
        try {
            final Scanner scanner = (Scanner) beanFactory.getBean("scanner", file, UTF_8);

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                final String[] property = line.split("=");
                settings.put(property[0], property[1]);
            }

            scanner.close();
        } catch (final BeansException exception) {
            final Throwable rootCause = exception.getMostSpecificCause();
            if (rootCause instanceof FileNotFoundException) {
                logger.info("User '{}' doesn't have settings file at the moment.", player.getUsername());
            } else {
                logger.error("Loading settings file for user '{}' failed.", player.getUsername(), exception);
            }
        }
    }

    @Override
    public void saveSettings(final PlayerUser player) {
        Assert.notNull(player, PLAYER_NOT_NULL);
        final PlayerSettings settings = player.getSettings();
        final File userSaveDirFile = (File) beanFactory.getBean("file", getSaveDirectory() + "/" + player.getId());
        if (!userSaveDirFile.exists()) {
            userSaveDirFile.mkdirs();
        }
        final File file = (File) beanFactory.getBean("file", userSaveDirFile, SETTINGS_PROPERTIES);
        final Writer writer = (Writer) beanFactory.getBean("writer", file, UTF_8);

        try {
            for (final Entry<String, String> entry : settings.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (final IOException exception) {
            logger.error("Failed to save settings for user {}!", player.getUsername(), exception);
        }

        writer.close();
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getSaveDirectory() {
        return directoryProvider.getSaveGameDirectory();
    }

}
