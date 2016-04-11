package hu.zagor.gamebooks.ff.mvc.book.newgame.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the new game requests to the given book.
 * @author Tamas_Szekeres
 */
public class SorBookNewGameController extends FfBookNewGameController {
    @Override
    protected Map<String, Object> doGenerateCharacter(final HttpServletRequest request) {
        final Map<String, Object> results = super.doGenerateCharacter(request);
        final SorCharacter character = (SorCharacter) getWrapper(request).getCharacter();
        if (character.isWizard()) {
            getInfo().getCharacterHandler().getItemHandler().addItem(character, "4102", 1);
        }
        return results;
    }

    /**
     * Method to handle the initialization of a completely new book.
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @param model the {@link Model} object
     * @param sectionId the id to jump to
     * @throws IOException if an input or output exception occurs
     */
    @RequestMapping("spellJump-{section}")
    public final void handleSpellJumpThroughBooks(final HttpServletRequest request, final HttpServletResponse response, final Model model,
        @PathVariable("section") final int sectionId) throws IOException {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Character character = wrapper.getCharacter();
        this.handleNew(request, model, null);
        wrapper.setCharacter(character);
        final Paragraph paragraph = wrapper.getParagraph();
        final ChoiceSet choices = paragraph.getData().getChoices();
        choices.clear();
        choices.add(new Choice(String.valueOf(sectionId), null, 0, null));
        paragraph.calculateValidEvents();
        response.sendRedirect("s-" + sectionId);
    }
}
