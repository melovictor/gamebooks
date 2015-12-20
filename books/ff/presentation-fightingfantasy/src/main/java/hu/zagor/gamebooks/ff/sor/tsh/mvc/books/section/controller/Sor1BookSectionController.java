package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookSectionController extends FfBookSectionController {
    private static final int TROLL_CLUMSINESS_OVER = 4;
    private static final String TROLL_ID = "16";
    private static final int INITIAL_TROLL_SKILL = 8;
    @Resource(name = "sorSwordItems") private Set<String> swordItemIds;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor1BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    public String handleFight(final Model model, final HttpServletRequest request, @RequestParam("id") final String enemyId, @RequestParam("hit") final Boolean luckOnHit,
        @RequestParam("def") final Boolean luckOnDefense, @RequestParam("oth") final Boolean luckOnOther) {
        setUpRagnarsBracelet(request);
        final String fightResult = super.handleFight(model, request, enemyId, luckOnHit, luckOnDefense, luckOnOther);
        stopTrollWeaponPickUpAttempt(request, enemyId);
        return fightResult;
    }

    private void stopTrollWeaponPickUpAttempt(final HttpServletRequest request, final String enemyId) {
        if (TROLL_ID.equals(enemyId)) {
            final HttpSessionWrapper wrapper = getWrapper(request);
            final Paragraph paragraph = wrapper.getParagraph();
            final CommandList commands = paragraph.getData().getCommands();
            if (!commands.isEmpty()) {
                final FightCommand fightCommand = (FightCommand) commands.get(0);
                if (roundFour(fightCommand)) {
                    alwaysTryToPickUpHalberd(fightCommand);
                } else if (pickedUpHalberd((FfEnemy) wrapper.getEnemies().get(TROLL_ID))) {
                    stopPickUpAttempt(fightCommand);
                }
            }
        }
    }

    private boolean pickedUpHalberd(final FfEnemy enemy) {
        return enemy.getSkill() == INITIAL_TROLL_SKILL;
    }

    private void stopPickUpAttempt(final FightCommand fightCommand) {
        fightCommand.setAfterBounding(null);
    }

    private void alwaysTryToPickUpHalberd(final FightCommand fightCommand) {
        final FightRoundBoundingCommand afterBounding = fightCommand.getAfterBounding();
        afterBounding.setNth(1);
    }

    private boolean roundFour(final FightCommand command) {
        return command.getRoundNumber() == TROLL_CLUMSINESS_OVER;
    }

    private void setUpRagnarsBracelet(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, "3020");

        if (item != null) {
            final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
            if (isSword(equippedWeapon)) {
                item.setAttackStrength(2);
            } else {
                item.setAttackStrength(0);
            }
        }
    }

    private boolean isSword(final FfItem equippedWeapon) {
        return swordItemIds.contains(equippedWeapon.getId());
    }
}
