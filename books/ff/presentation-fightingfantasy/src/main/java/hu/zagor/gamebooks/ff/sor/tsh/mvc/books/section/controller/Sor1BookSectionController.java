package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.SorBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Set;
import javax.annotation.Resource;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookSectionController extends SorBookSectionController {
    private static final int ASSASSIN_STAMINA_FLEE_LIMIT = 6;
    private static final String ASSASSIN_ID = "17";
    private static final int TROLL_CLUMSINESS_OVER = 5;
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
    protected void handleBeforeFight(final HttpSessionWrapper wrapper, final String enemyId) {
        setUpRagnarsBracelet(wrapper);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        preventKillerFromGivingUp(wrapper, enemyId);
        handleTrollPickingUpHisWeapon(wrapper, enemyId);
    }

    private void preventKillerFromGivingUp(final HttpSessionWrapper wrapper, final String enemyId) {
        if (ASSASSIN_ID.equals(enemyId)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final FfAttributeHandler attributeHandler = getInfo().getCharacterHandler().getAttributeHandler();
            final int stamina = attributeHandler.resolveValue(character, "stamina");
            if (stamina < ASSASSIN_STAMINA_FLEE_LIMIT) {
                final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get(ASSASSIN_ID);
                enemy.setFleeAtStamina(0);
            }
        }
    }

    private void handleTrollPickingUpHisWeapon(final HttpSessionWrapper wrapper, final String enemyId) {
        if (TROLL_ID.equals(enemyId)) {
            final Paragraph paragraph = wrapper.getParagraph();
            final CommandList commands = paragraph.getData().getCommands();
            if (!commands.isEmpty()) {
                final FightCommand fightCommand = (FightCommand) commands.get(0);
                if (pickedUpHalberd((FfEnemy) wrapper.getEnemies().get(TROLL_ID))) {
                    stopPickUpAttempt(fightCommand);
                } else if (roundFive(fightCommand)) {
                    alwaysTryToPickUpHalberd(fightCommand);
                }
            }
        }
    }

    private boolean pickedUpHalberd(final FfEnemy enemy) {
        return enemy.getSkill() == INITIAL_TROLL_SKILL;
    }

    private void stopPickUpAttempt(final FightCommand fightCommand) {
        fightCommand.setBeforeBounding(null);
    }

    private void alwaysTryToPickUpHalberd(final FightCommand fightCommand) {
        final FightRoundBoundingCommand beforeBounding = fightCommand.getBeforeBounding();
        beforeBounding.setNth(1);
    }

    private boolean roundFive(final FightCommand command) {
        return command.getRoundNumber() == TROLL_CLUMSINESS_OVER;
    }

    private void setUpRagnarsBracelet(final HttpSessionWrapper wrapper) {
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, "3020");

        if (item != null && item.getEquipInfo().isEquipped()) {
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

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        if (itemHandler.hasItem(character, "4006", 1)) {
            itemHandler.removeItem(character, "4006", 1);
            character.setStamina(character.getStamina() / 2);
        }
    }
}
