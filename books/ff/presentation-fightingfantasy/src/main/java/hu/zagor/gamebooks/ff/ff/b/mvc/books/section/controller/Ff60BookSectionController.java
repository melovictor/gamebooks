package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.BLOODBONES)
public class Ff60BookSectionController extends FfBookSectionController {
    private static final int ATTACK_STRENGTH_BONUS_AMOUNT = 10;
    private static final String FIGHT_END_DISAPPEARING_ATTACK_STRENGTH_BONUS = "4000";

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff60BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        final Character character = wrapper.getCharacter();
        if (character.getCommandView() == null) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, FIGHT_END_DISAPPEARING_ATTACK_STRENGTH_BONUS, ATTACK_STRENGTH_BONUS_AMOUNT);
        }
    }
}
