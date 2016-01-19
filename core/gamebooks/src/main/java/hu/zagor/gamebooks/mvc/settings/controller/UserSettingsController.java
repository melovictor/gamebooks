package hu.zagor.gamebooks.mvc.settings.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.random.ReplayingNumberGenerator;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.generic.controller.LanguageAwareController;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.DefaultSettingsHandler;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the settings page.
 * @author Tamas_Szekeres
 */
@Controller
public class UserSettingsController extends LanguageAwareController {

    @LogInject private Logger logger;

    @Autowired private UserSettingsHandler userSettingsHandler;
    @Autowired private DefaultSettingsHandler defaultSettingsHandler;
    @Autowired private EnvironmentDetector environmentDetector;
    @Autowired @Qualifier("d6") private ReplayingNumberGenerator generator6;
    @Autowired @Qualifier("d10") private ReplayingNumberGenerator generator10;

    /**
     * Method for displaying the settings screen.
     * @param model the model of the page
     * @param request the servlet request
     * @return the settings screen
     */
    @RequestMapping(value = PageAddresses.SETTINGS, method = RequestMethod.GET)
    public String displaySettingsScreen(final Model model, final HttpServletRequest request) {
        initializeLanguages(model, request);
        model.addAttribute("settings", defaultSettingsHandler.getOrderedSettings());

        final Map<String, String> userSettings = new HashMap<>(defaultSettingsHandler.getDefaultSettings());
        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        updateSettings(player, userSettings);
        model.addAttribute("userSettings", userSettings);
        model.addAttribute("player", player);
        model.addAttribute("pageTitle", "page.title");
        model.addAttribute("environment", environmentDetector);
        model.addAttribute("nums6", generator6.getThrownResults());
        model.addAttribute("nums10", generator10.getThrownResults());
        if (player.isAdmin() || environmentDetector.isDevelopment()) {
            model.addAttribute("memoryUsageList", ManagementFactory.getMemoryPoolMXBeans());
        }
        return "settings";
    }

    private void updateSettings(final PlayerUser player, final Map<String, String> userSettings) {
        final PlayerSettings settings = player.getSettings();
        for (final Entry<String, String> entry : settings.entrySet()) {
            userSettings.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Method for saving the new settings.
     * @param model the model of the page
     * @param request the servlet request
     * @return the settings screen
     */
    @RequestMapping(value = PageAddresses.SETTINGS, method = RequestMethod.POST)
    public String saveSettings(final Model model, final HttpServletRequest request) {

        final Map<String, String[]> parameterMap = request.getParameterMap();

        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        updatePlayerSettings(player, parameterMap);

        return displaySettingsScreen(model, request);
    }

    private void updatePlayerSettings(final PlayerUser player, final Map<String, String[]> parameterMap) {
        final PlayerSettings settings = player.getSettings();
        for (final Entry<String, String[]> entry : parameterMap.entrySet()) {
            settings.put(entry.getKey(), entry.getValue()[0]);
        }
        userSettingsHandler.saveSettings(player);
    }

    /**
     * Method for handling the recording state changes.
     * @param request the {@link HttpServletRequest} bean
     * @param model the {@link Model} bean
     * @param command the command to be executed
     * @return the new settings screen
     */
    @RequestMapping(value = PageAddresses.SETTINGS + "/recording/{command}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String setUpRecordingSettings(final HttpServletRequest request, final Model model, @PathVariable("command") final int command) {
        switch (command) {
        case 1:
            environmentDetector.setRecordState(true);
            generator6.getThrownResults().clear();
            generator10.getThrownResults().clear();
            break;
        case 0:
            environmentDetector.setRecordState(false);
            break;
        default:
        }
        return displaySettingsScreen(model, request);
    }

}
