package hu.zagor.gamebooks.player.settings;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.scanner.Scanner;
import hu.zagor.gamebooks.support.writer.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultUserSettingsHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultUserSettingsHandlerTest {

    private static final String LOCATION = "d:/prod/";
    private static final String PLAYER_USERNAME = "FireFoX";
    private IMocksControl mockControl;
    private DefaultUserSettingsHandler underTest;
    private Logger logger;
    private BeanFactory beanFactory;
    private PlayerUser player;
    private PlayerSettings settings;
    private Scanner scanner;
    private Writer writer;
    private DirectoryProvider directoryProvider;
    private File location;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new DefaultUserSettingsHandler();

        logger = mockControl.createMock(Logger.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        scanner = mockControl.createMock(Scanner.class);
        writer = mockControl.createMock(Writer.class);

        player = new PlayerUser(29, PLAYER_USERNAME, false);
        settings = player.getSettings();
        directoryProvider = mockControl.createMock(DirectoryProvider.class);
        location = mockControl.createMock(File.class);

        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "directoryProvider", directoryProvider);
        underTest.setBeanFactory(beanFactory);
    }

    @BeforeMethod
    public void setUpMethod() {
        settings.setChoiceSectionDisplayable(false);
        settings.setTopSectionDisplayable(true);
        settings.setImageTypeOrder("bwFirst");

        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLoadSettingsWhenPlayerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.loadSettings(null);
        // THEN throws exception
    }

    public void testLoadSettingsWhenPlayerIsSetAndHasConfigFileShouldLoadSettings() {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(LOCATION);
        expect(beanFactory.getBean("file", LOCATION, "29/settings.properties")).andReturn(location);
        expect(beanFactory.getBean("scanner", location, "UTF-8")).andReturn(scanner);
        expect(scanner.hasNextLine()).andReturn(true);
        expect(scanner.nextLine()).andReturn("global.sectionsVisible.top=false");
        expect(scanner.hasNextLine()).andReturn(true);
        expect(scanner.nextLine()).andReturn("global.sectionsVisible.choice=true");
        expect(scanner.hasNextLine()).andReturn(true);
        expect(scanner.nextLine()).andReturn("global.imageTypeOrder=colFirst");
        expect(scanner.hasNextLine()).andReturn(false);
        scanner.close();
        mockControl.replay();
        // WHEN
        underTest.loadSettings(player);
        // THEN
        Assert.assertTrue(settings.isChoiceSectionDisplayable());
        Assert.assertFalse(settings.isTopSectionDisplayable());
        Assert.assertEquals(settings.getImageTypeOrder(), "colFirst");
    }

    public void testLoadSettingsWhenPlayerIsSetAndHasNoConfigFileShouldLeaveSettingsAsIs() {
        // GIVEN
        final BeansException exception = new BeanCreationException("Couldn't create bean.", new FileNotFoundException("Couldn't find file."));
        expect(directoryProvider.getSaveGameDirectory()).andReturn(LOCATION);
        expect(beanFactory.getBean("file", LOCATION, "29/settings.properties")).andReturn(location);
        expect(beanFactory.getBean("scanner", location, "UTF-8")).andThrow(exception);
        logger.info("User '{}' doesn't have settings file at the moment.", PLAYER_USERNAME);
        mockControl.replay();
        // WHEN
        underTest.loadSettings(player);
        // THEN
        Assert.assertFalse(settings.isChoiceSectionDisplayable());
        Assert.assertTrue(settings.isTopSectionDisplayable());
        Assert.assertEquals(settings.getImageTypeOrder(), "bwFirst");
    }

    public void testLoadSettingsWhenPlayerIsSetAndErrorOccursDuringSettingsLoadingShouldReportError() {
        // GIVEN
        final BeansException exception = new BeanCreationException("Couldn't create bean.", new IOException("Some random IO issue."));
        expect(directoryProvider.getSaveGameDirectory()).andReturn(LOCATION);
        expect(beanFactory.getBean("file", LOCATION, "29/settings.properties")).andReturn(location);
        expect(beanFactory.getBean("scanner", location, "UTF-8")).andThrow(exception);
        logger.error("Loading settings file for user '{}' failed.", PLAYER_USERNAME, exception);
        mockControl.replay();
        // WHEN
        underTest.loadSettings(player);
        // THEN
        Assert.assertFalse(settings.isChoiceSectionDisplayable());
        Assert.assertTrue(settings.isTopSectionDisplayable());
        Assert.assertEquals(settings.getImageTypeOrder(), "bwFirst");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveSettingsWhenPlayerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.saveSettings(null);
        // THEN throws exception
    }

    public void testSaveSettingsWhenPlayerIsSetShouldWriteSettingsOut() throws IOException {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(LOCATION);
        expect(beanFactory.getBean("file", LOCATION + "/29")).andReturn(location);
        expect(location.exists()).andReturn(true);
        expect(beanFactory.getBean("file", location, "settings.properties")).andReturn(location);
        expect(beanFactory.getBean("writer", location, "UTF-8")).andReturn(writer);
        final Capture<String> savedElement = new Capture<String>(CaptureType.ALL);
        writer.write(capture(savedElement));
        writer.write(capture(savedElement));
        writer.write(capture(savedElement));
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveSettings(player);
        // THEN
        final Set<String> savedElements = new HashSet<>();
        savedElements.add("global.sectionsVisible.top=true\n");
        savedElements.add("global.sectionsVisible.choice=false\n");
        savedElements.add("global.imageTypeOrder=bwFirst\n");
        for (final String capturedElement : savedElement.getValues()) {
            Assert.assertTrue(savedElements.contains(capturedElement));
        }
    }

    public void testSaveSettingsWhenSavingTriggersAnErrorShouldReportError() throws IOException {
        // GIVEN
        expect(directoryProvider.getSaveGameDirectory()).andReturn(LOCATION);
        expect(beanFactory.getBean("file", LOCATION + "/29")).andReturn(location);
        expect(location.exists()).andReturn(false);
        expect(location.mkdirs()).andReturn(true);
        expect(beanFactory.getBean("file", location, "settings.properties")).andReturn(location);
        expect(beanFactory.getBean("writer", location, "UTF-8")).andReturn(writer);
        writer.write(anyObject(String.class));
        final IOException exception = new IOException();
        expectLastCall().andThrow(exception);
        logger.error("Failed to save settings for user {}!", PLAYER_USERNAME, exception);
        writer.close();
        mockControl.replay();
        // WHEN
        underTest.saveSettings(player);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
