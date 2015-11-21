package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.HuntService;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.MASKS_OF_MAYHEM)
public class Ff23BookSectionController extends FfBookSectionController {

    @Autowired
    private HuntService huntService;
    @Autowired
    private EnemyStatusEvaluator enemyStatusEvaluator;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff23BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        if ("240".equals(paragraph.getId())) {
            wrapper.getParagraph().clearValidMoves();
        }
    }

    /**
     * Handler method for a single round of the Tiger hunting.
     * @param request the {@link HttpServletRequest} object
     * @return the result of the round
     */
    @RequestMapping(value = "hunt", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public HuntRoundResult handleHuntRound(final HttpServletRequest request) {
        return huntService.playRound(getWrapper(request), getInfo());
    }

    @Override
    public String handleFight(final Model model, final HttpServletRequest request, @RequestParam("id") final String enemyId,
        @RequestParam("hit") final Boolean luckOnHit, @RequestParam("def") final Boolean luckOnDefense, @RequestParam("oth") final Boolean luckOnOther) {

        final FightCommand fightCommand = (FightCommand) getWrapper(request).getParagraph().getData().getCommands().get(0);
        if ("27".equals(enemyId) || "28".equals(enemyId)) {
            final List<String> enemies = fightCommand.getEnemies();
            enemies.add(enemies.remove(0));
        }
        final String handleFight = super.handleFight(model, request, enemyId, luckOnHit, luckOnDefense, luckOnOther);

        if ("44".equals(enemyId) || "45".equals(enemyId)) {
            handleTribesmanSwitching(fightCommand, request, enemyId);
        }

        return handleFight;
    }

    private void handleTribesmanSwitching(final FightCommand fightCommand, final HttpServletRequest request, final String enemyId) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final List<String> enemies = fightCommand.getEnemies();
        final List<FfEnemy> resolvedEnemies = fightCommand.getResolvedEnemies();
        resetEnemies(wrapper, enemies, resolvedEnemies);
        if (enemies.size() == 2) {
            final BattleStatistics battleStatistics = fightCommand.getBattleStatistics(enemyId);
            if (battleStatistics.getSubsequentWin() > 0) {
                // This one won't fight the next round
                enemies.remove(enemyId);
                for (final FfEnemy enemy : resolvedEnemies) {
                    if (enemyId.equals(enemy.getId())) {
                        resolvedEnemies.remove(enemy);
                        break;
                    }
                }
            }
        }
        final CommandList commands = wrapper.getParagraph().getData().getCommands();
        if (commands.size() == 1) {
            final FightCommand singleFightCommand = (FightCommand) commands.get(0);
            for (int i = 0; i < fightCommand.getRoundNumber(); i++) {
                singleFightCommand.increaseBattleRound();
            }
        }
    }

    private void resetEnemies(final HttpSessionWrapper wrapper, final List<String> enemies, final List<FfEnemy> resolvedEnemies) {
        enemies.clear();
        resolvedEnemies.clear();
        addEnemy(wrapper, enemies, resolvedEnemies, "44");
        addEnemy(wrapper, enemies, resolvedEnemies, "45");
    }

    private void addEnemy(final HttpSessionWrapper wrapper, final List<String> enemies, final List<FfEnemy> resolvedEnemies, final String enemyId) {
        final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get(enemyId);
        if (enemyStatusEvaluator.isAlive(enemy)) {
            enemies.add(enemyId);
            resolvedEnemies.add(enemy);
        }
    }

}
