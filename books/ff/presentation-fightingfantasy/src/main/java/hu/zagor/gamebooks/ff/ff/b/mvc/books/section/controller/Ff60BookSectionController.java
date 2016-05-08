package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.BLOODBONES)
public class Ff60BookSectionController extends FfBookSectionController {
    private static final int LUCK_BONUS = 3;
    private static final int SPELL_CAST_COST = -2;
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
    protected void handleBeforeFight(final HttpSessionWrapper wrapper, final String enemyId) {
        final Ff60Character character = (Ff60Character) wrapper.getCharacter();
        character.setDamageInLastFight(character.getDamageInLastFight() + character.getStamina());
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        final Ff60Character character = (Ff60Character) wrapper.getCharacter();
        character.setDamageInLastFight(character.getDamageInLastFight() - character.getStamina());
        if (character.getCommandView() == null) {
            getInfo().getCharacterHandler().getItemHandler().removeItem(character, FIGHT_END_DISAPPEARING_ATTACK_STRENGTH_BONUS, ATTACK_STRENGTH_BONUS_AMOUNT);
        }
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        super.handleCustomSectionsPre(model, wrapper, changedSection);
        if (changedSection) {
            final Ff60Character character = (Ff60Character) wrapper.getCharacter();
            character.setDamageInLastFight(0);
        }
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        super.handleCustomSectionsPost(model, wrapper, changedSection);

        final Paragraph paragraph = wrapper.getParagraph();
        if ("346".equals(paragraph.getDisplayId())) {
            final String templateText = paragraph.getData().getText();
            final Ff60Character character = (Ff60Character) wrapper.getCharacter();
            final String resolvedText = String.format(templateText, character.getArrowRound(), character.getArrowScore());
            paragraph.getData().setText(resolvedText);
        }
    }

    /**
     * Handler for the skill spell.
     * @param request the {@link HttpServletRequest}
     */
    @RequestMapping("skillSpell")
    @ResponseBody
    public void handleSkillSpell(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "4008")) {
            character.changeStamina(SPELL_CAST_COST);
            if (!itemHandler.hasItem(character, "4016")) {
                itemHandler.addItem(character, "4016", 1);
            }
        }
    }

    /**
     * Handler for the luck spell.
     * @param request the {@link HttpServletRequest}
     */
    @RequestMapping("luckSpell")
    @ResponseBody
    public void handleLuckSpell(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (itemHandler.hasItem(character, "4007")) {
            character.changeStamina(SPELL_CAST_COST);
            character.changeLuck(LUCK_BONUS);
        }
    }
}
