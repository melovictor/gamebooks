package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.service.FfCustomPrePostSectionHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Post section handler for ff11, section 175.
 * @author Tamas_Szekeres
 */
@Component
public class Ff11Section175PostHandler extends FfCustomPrePostSectionHandler {

    private static final int RE_ADD_LOST_SKILL = 20;

    @Override
    public void handle(final Model model, final HttpSessionWrapper wrapper, final FfBookInformations info, final boolean changedSection) {
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final int recoverableSkillPoints = info.getCharacterHandler().getItemHandler().removeItem(character, "4003", RE_ADD_LOST_SKILL).size();
        character.changeSkill(recoverableSkillPoints);
    }

}
