package hu.zagor.gamebooks.ff.wm.tdcoa.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Warlock;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Warlock.THE_DARK_CHRONICLES_OF_ANAKENDIS)
public class Wm6BookSectionController extends FfBookSectionController {

    private static final int STAMINA_LOSS_RATIO = 4;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Wm6BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        if ("95".equals(paragraph.getId())) {
            final Map<String, Enemy> enemies = wrapper.getEnemies();
            resetEnemy(enemies, "1");
            resetEnemy(enemies, "4");
        } else if ("175".equals(paragraph.getId())) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
            final int stamina = attributeHandler.resolveValue(character, "stamina") / STAMINA_LOSS_RATIO;
            attributeHandler.handleModification(character, "stamina", -stamina);
        }

    }

    private void resetEnemy(final Map<String, Enemy> enemies, final String enemyId) {
        final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
        enemy.setStamina(enemy.getInitialStamina());
    }
}
