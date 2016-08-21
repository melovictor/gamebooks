package hu.zagor.gamebooks.mvc.book.controller;

import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import java.util.Set;
import org.springframework.ui.Model;
import com.google.common.collect.Sets;

/**
 * Abstract controller for controllers that are supposed to fire up sections to the users.
 * @author Tamas_Szekeres
 */
public abstract class AbstractSectionDisplayingController extends AbstractRequestWrappingController {
    private static final Set<String> ALWAYS_SHOW_TOP = Sets.newHashSet(BookParagraphConstants.BACKGROUND.getValue(), BookParagraphConstants.GENERATE.getValue());

    /**
     * Sets up some common section display options for the section pages.
     * @param paragraph the {@link Paragraph} to display
     * @param model the model to be populated
     * @param player the {@link PlayerUser} logged in
     */
    protected void setUpSectionDisplayOptions(final Paragraph paragraph, final Model model, final PlayerUser player) {
        final PlayerSettings settings = player.getSettings();
        if (!ALWAYS_SHOW_TOP.contains(paragraph.getId())) {
            model.addAttribute("hideTopSection", !settings.isTopSectionDisplayable());
        }
        model.addAttribute("hideChoiceSection", !settings.isChoiceSectionDisplayable());
        model.addAttribute("imgTypeOrder", settings.getImageTypeOrder());
        model.addAttribute("informativeSections", settings.isInformativeSectionsRequested());
    }

    /**
     * Marks images inside the section so they will be displayable from the content provider server.
     * @param paragraph the current {@link Paragraph} object
     * @param imageType the type of the image to be displayed first
     */
    protected void markParagraphImages(final Paragraph paragraph, final String imageType) {
        final ParagraphData data = paragraph.getData();
        String text = data.getText();
        text = text.replaceAll("(<img[^>]*?src=\"[^\"?]*)\"", "$1?" + imageType + "\"");
        text = text.replaceAll("<p class=\"inlineImage\" data-img=\"",
            "<p class=\"inlineImage\" data-book=\"" + getInfo().getResourceDir() + "\" data-type=\"" + imageType.charAt(0) + "\" data-img=\"");
        data.setText(text);
    }

}
