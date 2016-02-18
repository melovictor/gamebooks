package hu.zagor.gamebooks.books.saving;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.File;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Abstract class containing some convenience methods for loading and saving the state of the game.
 * @author Tamas_Szekeres
 */
public abstract class AbstractGameStateHandler implements GameStateHandler, BeanFactoryAware {

    private static final String CONTAINER_NOT_NULL = "The parameter 'container' cannot be null!";

    @LogInject private Logger logger;

    private BeanFactory beanFactory;
    @Autowired private DirectoryProvider directoryProvider;

    @Override
    public void loadGame(final SavedGameContainer container) {
        Assert.notNull(container, CONTAINER_NOT_NULL);
        logger.info("Started loading game for user {} for book {}.", container.getUserId(), container.getBookId());

        final File saveFileLocation = getSaveFileLocation(container);
        if (saveFileLocation.canRead()) {
            loadGameFromFile(container, saveFileLocation);
        } else {
            logger.warn("Cannot open saved file '{}' for reading!", saveFileLocation.getAbsolutePath());
        }
    }

    /**
     * Handles the actual loading of the game from file.
     * @param container the container into which the game must be loaded
     * @param saveFileLocation the location of the save file from which to load the data
     */
    protected abstract void loadGameFromFile(final SavedGameContainer container, final File saveFileLocation);

    @Override
    public void saveGame(final SavedGameContainer container) {
        Assert.notNull(container, CONTAINER_NOT_NULL);
        logger.info("Started saving game for user {} for book {}.", container.getUserId(), container.getBookId());

        final File saveFileLocation = getSaveFileLocation(container);
        saveGameToFile(container, saveFileLocation);
    }

    /**
     * Handles the actual saving into file.
     * @param container the container from which the game must be saved
     * @param saveFileLocation the location of the save file into which to save the data
     */
    protected abstract void saveGameToFile(final SavedGameContainer container, final File saveFileLocation);

    @Override
    public boolean checkSavedGame(final int playerId, final long bookId) {
        return checkSavedGame(generateSavedGameContainer(playerId, bookId));
    }

    @Override
    public boolean checkSavedGame(final int playerId, final ContinuationData continuationData) {
        final SavedGameContainer container = generateSavedGameContainer(playerId, continuationData.getPreviousBookId());
        final File saveFile = getSaveFileLocation(container);
        boolean savedGameExists = false;
        if (saveFile.exists()) {
            savedGameExists = saveGamePositionIsCorrect(container, continuationData.getPreviousBookLastSectionId());
        }
        return savedGameExists;
    }

    private boolean saveGamePositionIsCorrect(final SavedGameContainer container, final String previousBookLastSectionId) {
        loadGame(container);
        final Paragraph paragraph = (Paragraph) container.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY);
        return paragraph != null && previousBookLastSectionId.equals(paragraph.getId());
    }

    @Override
    public SavedGameContainer generateSavedGameContainer(final int playerId, final long bookId) {
        return (SavedGameContainer) beanFactory.getBean("savedGameContainer", playerId, bookId);
    }

    /**
     * Returns the {@link File} object pointing to the location where the game's saved file should be located.
     * @param container the {@link SavedGameContainer} object
     * @return the {@link File} pointing to the save file
     */
    protected File getSaveFileLocation(final SavedGameContainer container) {
        final File saveFile;
        final File saveFileDirectory = (File) beanFactory.getBean("file", getSavegameRoot(), container.getUserId().toString());
        if (!saveFileDirectory.exists() && !saveFileDirectory.mkdirs()) {
            logger.error("Failed to create save game directory '{}', using temp directory instead!", saveFileDirectory.getAbsolutePath());
            saveFile = (File) beanFactory.getBean("file", System.getProperty("java.io.tmpdir"), container.getBookId().toString());
        } else {
            saveFile = (File) beanFactory.getBean("file", saveFileDirectory, container.getBookId().toString());
        }
        return saveFile;
    }

    @Override
    public boolean checkSavedGame(final SavedGameContainer container) {
        Assert.notNull(container, CONTAINER_NOT_NULL);
        return getSaveFileLocation(container).exists();
    }

    /**
     * Reports the failure of loading the data from the saved game.
     * @param container the container which should have been populated
     * @param saveFileLocation the location of the file
     * @param exception the exception that caused the problem
     */
    protected void reportLoadFailure(final SavedGameContainer container, final File saveFileLocation, final Exception exception) {
        logger.warn("Failed to load game for book '{}' for user '{}' from location '{}'!",
            new Object[]{container.getBookId(), container.getUserId(), saveFileLocation.getAbsolutePath()}, exception);
    }

    /**
     * Reports the failure of saving the data into the saved game.
     * @param container the container which should have been saved
     * @param saveFileLocation the location of the file
     * @param exception the exception that caused the problem
     */
    protected void reportSaveFailure(final SavedGameContainer container, final File saveFileLocation, final Exception exception) {
        logger.warn("Failed to save game for book '{}' for user '{}' to location '{}'!",
            new Object[]{container.getBookId(), container.getUserId(), saveFileLocation.getAbsolutePath()}, exception);
    }

    /**
     * Saves loaded data into the container and reports it through the logger.
     * @param container the container into which the data must be stored
     * @param savedElements the loaded data
     */
    protected void saveLoadedData(final SavedGameContainer container, final Map<String, Object> savedElements) {
        container.addElements(savedElements);
        logger.info("Successfully finished loading game for user {} for book {}.", container.getUserId(), container.getBookId());
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected Logger getLogger() {
        return logger;
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private String getSavegameRoot() {
        return directoryProvider.getSaveGameDirectory();
    }
}
