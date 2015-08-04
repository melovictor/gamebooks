package hu.zagor.gamebooks.mvc.settings.controller;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.DefaultRandomNumberGenerator;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.SupportedLanguage;
import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.DefaultSettingsHandler;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserSettingsController}.
 * @author Tamas_Szekeres
 */
@Test
public class UserSettingsControllerTest {

    private UserSettingsController underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private UserSettingsHandler settingsHandler;
    private DefaultSettingsHandler defaultSettingsHandler;
    private HttpServletRequest request;
    private BeanFactory beanFactory;
    private Model model;
    private SupportedLanguage[] availableLanguages;
    private Cookie[] cookies;
    private Cookie cookie;
    private SortedSet<SettingGroup> settings;
    private Map<String, String> defaultSettings;
    private HttpSession session;
    private HttpSessionWrapper wrapper;
    private PlayerUser player;
    private EnvironmentDetector environmentDetector;
    private Map<String, String[]> parameterMap;
    private DefaultRandomNumberGenerator generator6;
    private DefaultRandomNumberGenerator generator10;
    private List<Integer> list6;
    private List<Integer> list10;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        settingsHandler = mockControl.createMock(UserSettingsHandler.class);
        defaultSettingsHandler = mockControl.createMock(DefaultSettingsHandler.class);
        request = mockControl.createMock(HttpServletRequest.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        model = mockControl.createMock(Model.class);
        availableLanguages = new SupportedLanguage[]{};
        cookie = mockControl.createMock(Cookie.class);
        cookies = new Cookie[]{cookie, cookie};
        settings = new TreeSet<>();
        defaultSettings = new HashMap<>();
        session = mockControl.createMock(HttpSession.class);
        wrapper = mockControl.createMock(HttpSessionWrapper.class);
        player = new PlayerUser(10, "FireFoX", true);
        parameterMap = new HashMap<>();
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        generator6 = mockControl.createMock(DefaultRandomNumberGenerator.class);
        generator10 = mockControl.createMock(DefaultRandomNumberGenerator.class);
        list6 = mockControl.createMock(List.class);
        list10 = mockControl.createMock(List.class);

        underTest = new UserSettingsController();
        underTest.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "userSettingsHandler", settingsHandler);
        Whitebox.setInternalState(underTest, "defaultSettingsHandler", defaultSettingsHandler);
        Whitebox.setInternalState(underTest, "availableLanguages", availableLanguages);
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
        Whitebox.setInternalState(underTest, "generator6", generator6);
        Whitebox.setInternalState(underTest, "generator10", generator10);

        defaultSettings.put("default.setting.1", "default.setting.1.value");
        defaultSettings.put("default.setting.2", "default.setting.2.value");
        defaultSettings.put("default.setting.3", "default.setting.3.value");
    }

    @BeforeMethod
    public void setUpMethod() {
        final PlayerSettings playerSettings = player.getSettings();

        playerSettings.clear();
        playerSettings.put("default.setting.1", "default.setting.1.playervalue");
        playerSettings.put("default.setting.2", "default.setting.2.value");

        settings.clear();

        mockControl.reset();
    }

    public void testDisplaySettingsScreenShouldInitializeModelFromDefaultSettingsOverridenByUserSettings() {
        // GIVEN
        expect(model.addAttribute("availableLanguages", availableLanguages)).andReturn(model);
        expect(request.getCookies()).andReturn(cookies);
        expect(cookie.getName()).andReturn("something");
        expect(cookie.getName()).andReturn("lang");
        expect(cookie.getValue()).andReturn("en");
        expect(model.addAttribute("selectedLanguage", "en")).andReturn(model);
        expect(defaultSettingsHandler.getOrderedSettings()).andReturn(settings);
        expect(model.addAttribute("settings", settings)).andReturn(model);
        expect(defaultSettingsHandler.getDefaultSettings()).andReturn(defaultSettings);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        final boolean recordState = false;
        final Capture<HashMap<String, String>> capturer = setUpModel(recordState);
        mockControl.replay();
        // WHEN
        final String returned = underTest.displaySettingsScreen(model, request);
        // THEN
        Assert.assertEquals(returned, "settings");
        final Map<String, String> capturedMap = capturer.getValue();
        Assert.assertEquals(capturedMap.size(), 3);
        Assert.assertEquals(capturedMap.get("default.setting.1"), "default.setting.1.playervalue");
        Assert.assertEquals(capturedMap.get("default.setting.2"), "default.setting.2.value");
        Assert.assertEquals(capturedMap.get("default.setting.3"), "default.setting.3.value");
    }

    public void testSaveSettingsShouldSaveSettingsAndInitializeModel() {
        // GIVEN
        expect(request.getParameterMap()).andReturn(parameterMap);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);
        parameterMap.put("default.setting.1", new String[]{"default.setting.1.playervalue"});
        parameterMap.put("default.setting.2", new String[]{"default.setting.2.playervalue"});
        parameterMap.put("default.setting.3", new String[]{"default.setting.3.playervalue"});
        settingsHandler.saveSettings(player);
        expect(model.addAttribute("availableLanguages", availableLanguages)).andReturn(model);
        expect(request.getCookies()).andReturn(cookies);
        expect(cookie.getName()).andReturn("something");
        expect(cookie.getName()).andReturn("lang");
        expect(cookie.getValue()).andReturn("en");
        expect(model.addAttribute("selectedLanguage", "en")).andReturn(model);
        expect(defaultSettingsHandler.getOrderedSettings()).andReturn(settings);
        expect(model.addAttribute("settings", settings)).andReturn(model);
        expect(defaultSettingsHandler.getDefaultSettings()).andReturn(defaultSettings);
        expect(request.getSession()).andReturn(session);
        expect(beanFactory.getBean("httpSessionWrapper", session)).andReturn(wrapper);
        wrapper.setRequest(request);
        expect(wrapper.getPlayer()).andReturn(player);

        final boolean recordState = true;
        final Capture<HashMap<String, String>> capturer = setUpModel(recordState);
        mockControl.replay();
        // WHEN
        final String returned = underTest.saveSettings(model, request);
        // THEN
        Assert.assertEquals(returned, "settings");
        final Map<String, String> capturedMap = capturer.getValue();
        Assert.assertEquals(capturedMap.size(), 3);
        Assert.assertEquals(capturedMap.get("default.setting.1"), "default.setting.1.playervalue");
        Assert.assertEquals(capturedMap.get("default.setting.2"), "default.setting.2.playervalue");
        Assert.assertEquals(capturedMap.get("default.setting.3"), "default.setting.3.playervalue");
    }

    private Capture<HashMap<String, String>> setUpModel(final boolean recordState) {
        final Capture<HashMap<String, String>> capturer = new Capture<>();
        expect(model.addAttribute(eq("userSettings"), capture(capturer))).andReturn(model);
        expect(model.addAttribute("player", player)).andReturn(model);
        expect(model.addAttribute("pageTitle", "page.title")).andReturn(model);
        expect(environmentDetector.isRecordState()).andReturn(recordState);
        expect(model.addAttribute("recordState", recordState)).andReturn(model);
        expect(generator6.getThrownResults()).andReturn(list6);
        expect(model.addAttribute("nums6", list6)).andReturn(model);
        expect(generator10.getThrownResults()).andReturn(list10);
        expect(model.addAttribute("nums10", list10)).andReturn(model);
        return capturer;
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
