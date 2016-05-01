package hu.zagor.gamebooks.mvc.acknowledgement.controller;

import hu.zagor.gamebooks.PageAddresses;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for displaying the acknowledgement page.
 * @author Tamas_Szekeres
 */
@Controller
public class AcknowledgementController {

    @Resource(name = "acknowledgementPeople") private Map<String, List<String>> acknowledgementPeople;

    /**
     * Displays the acknowledgement page.
     * @param model the {@link Model} object
     * @return the name of the page
     */
    @RequestMapping(PageAddresses.ACKNOWLEDGEMENT)
    public String display(final Model model) {
        model.addAttribute("users", acknowledgementPeople);
        model.addAttribute("pageTitle", "page.title");
        return "acknowledgement";
    }

}
