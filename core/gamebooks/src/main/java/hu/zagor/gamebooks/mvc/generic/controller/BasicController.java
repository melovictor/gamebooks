package hu.zagor.gamebooks.mvc.generic.controller;

import hu.zagor.gamebooks.PageAddresses;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the welcome and random pages.
 * @author Tamas_Szekeres
 *
 */
@Controller
public class BasicController {

    /**
     * Redirects the user from the welcome screen to login.
     * @return redirection to login
     */
    @RequestMapping(value = "")
    public String welcome() {
        return "redirect:" + PageAddresses.LOGIN;
    }

    /**
     * Redirects the user from all unhandled screens to login.
     * @return redirection to login
     */
    @RequestMapping(value = "*")
    public String random() {
        return "redirect:" + PageAddresses.LOGIN;
    }

}
