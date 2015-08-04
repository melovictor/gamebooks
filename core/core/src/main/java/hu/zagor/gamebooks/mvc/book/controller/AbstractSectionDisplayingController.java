package hu.zagor.gamebooks.mvc.book.controller;

import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;

import org.springframework.ui.Model;

/**
 * Abstract controller for controllers that are supposed to fire up sections to the users.
 * @author Tamas_Szekeres
 */
public abstract class AbstractSectionDisplayingController extends AbstractRequestWrappingController {

    /**
     * Sets up some common section display options for the section pages.
     * @param paragraph the {@link Paragraph} to display
     * @param model the model to be populated
     * @param player the {@link PlayerUser} logged in
     */
    protected void setUpSectionDisplayOptions(final Paragraph paragraph, final Model model, final PlayerUser player) {
        final PlayerSettings settings = player.getSettings();
        if (!BookParagraphConstants.BACKGROUND.getValue().equals(paragraph.getId())) {
            model.addAttribute("hideTopSection", !settings.isTopSectionDisplayable());
        }
        model.addAttribute("hideChoiceSection", !settings.isChoiceSectionDisplayable());
        model.addAttribute("imgTypeOrder", settings.getImageTypeOrder());
        model.addAttribute("informativeSections", settings.isInformativeSectionsRequested());
    }

}
