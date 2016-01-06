package hu.zagor.gamebooks.mvc.settings.controller;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.books.random.DefaultRandomNumberGenerator;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.SupportedLanguage;
import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.DefaultSettingsHandler;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private UserSettingsController underTest;
    @MockControl private IMocksControl mockControl;
    @Inject private Logger logger;
    @Inject private UserSettingsHandler userSettingsHandler;
    @Inject private DefaultSettingsHandler defaultSettingsHandler;
    @Mock private HttpServletRequest request;
    @Inject private BeanFactory beanFactory;
    @Mock private Model model;
    private SupportedLanguage[] availableLanguages;
    private Cookie[] cookies;
    @Mock private Cookie cookie;
    private SortedSet<SettingGroup> settings;
    private Map<String, String> defaultSettings;
    @Mock private HttpSession session;
    @Mock private HttpSessionWrapper wrapper;
    private PlayerUser player;
    @Inject private EnvironmentDetector environmentDetector;
    private Map<String, String[]> parameterMap;
    @Inject private DefaultRandomNumberGenerator generator6;
    @Inject private DefaultRandomNumberGenerator generator10;
    private List<Integer> list6;
    private List<Integer> list10;

    @BeforeClass
    public void setUpClass() {
        availableLanguages = new SupportedLanguage[]{};
        cookies = new Cookie[]{cookie, cookie};
        settings = new TreeSet<>();
        defaultSettings = new HashMap<>();
        player = new PlayerUser(10, "FireFoX", true);
        parameterMap = new HashMap<>();

        Whitebox.setInternalState(underTest, "availableLanguages", availableLanguages);

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
        final Capture<HashMap<String, String>> capturer = setUpModel();
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
        userSettingsHandler.saveSettings(player);
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

        final Capture<HashMap<String, String>> capturer = setUpModel();
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

    private Capture<HashMap<String, String>> setUpModel() {
        final Capture<HashMap<String, String>> capturer = newCapture();
        expect(model.addAttribute(eq("userSettings"), capture(capturer))).andReturn(model);
        expect(model.addAttribute("player", player)).andReturn(model);
        expect(model.addAttribute("pageTitle", "page.title")).andReturn(model);
        expect(model.addAttribute("environment", environmentDetector)).andReturn(model);
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
