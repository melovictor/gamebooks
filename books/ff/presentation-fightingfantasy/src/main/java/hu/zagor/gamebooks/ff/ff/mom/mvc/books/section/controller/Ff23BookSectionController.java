package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.enemy.Enemy;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.MASKS_OF_MAYHEM)
public class Ff23BookSectionController extends FfBookSectionController {

    @Autowired private HuntService huntService;
    @Autowired private EnemyStatusEvaluator enemyStatusEvaluator;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff23BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        if ("240".equals(paragraph.getId())) {
            paragraph.clearValidMoves();
        }
    }

    /**
     * Handler method for a single round of the Tiger hunting.
     * @param request the {@link HttpServletRequest} object
     * @return the result of the round
     */
    @RequestMapping(value = "hunt", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public HuntRoundResult handleHuntRound(final HttpServletRequest request) {
        return huntService.playRound(getWrapper(request), getInfo());
    }

    @Override
    protected void handleBeforeFight(final HttpSessionWrapper wrapper, final String enemyId) {
        if ("27".equals(enemyId) || "28".equals(enemyId)) {
            final FightCommand fightCommand = (FightCommand) wrapper.getParagraph().getData().getCommands().get(0);
            final List<String> enemies = fightCommand.getEnemies();
            enemies.add(enemies.remove(0));
        }
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        if ("44".equals(enemyId) || "45".equals(enemyId)) {
            handleTribesmanSwitching(wrapper, enemyId);
        }
    }

    private void handleTribesmanSwitching(final HttpSessionWrapper wrapper, final String enemyId) {
        final CommandList commands = wrapper.getParagraph().getData().getCommands();
        final FightCommand fightCommand = (FightCommand) commands.get(0);
        final List<String> enemies = fightCommand.getEnemies();
        final List<FfEnemy> resolvedEnemies = fightCommand.getResolvedEnemies();
        resetEnemies(wrapper, enemies, resolvedEnemies);
        if (enemies.size() == 2) {
            final BattleStatistics battleStatistics = fightCommand.getBattleStatistics(enemyId);
            if (battleStatistics.getSubsequentWin() > 0) {
                enemies.remove(enemyId);
                final Iterator<FfEnemy> enemiesIterator = resolvedEnemies.iterator();
                while (enemiesIterator.hasNext()) {
                    final FfEnemy enemy = enemiesIterator.next();
                    if (enemyId.equals(enemy.getId())) {
                        enemiesIterator.remove();
                    }
                }
            }
        }
        if (commands.size() == 1) {
            final FightCommand singleFightCommand = (FightCommand) commands.get(0);
            final int totalRoundsPassed = fightCommand.getRoundNumber();
            for (int i = 0; i < totalRoundsPassed; i++) {
                singleFightCommand.increaseBattleRound();
            }
        }
    }

    private void resetEnemies(final HttpSessionWrapper wrapper, final List<String> enemies, final List<FfEnemy> resolvedEnemies) {
        enemies.clear();
        resolvedEnemies.clear();
        final Map<String, Enemy> enemyMap = wrapper.getEnemies();
        addEnemy(enemyMap, enemies, resolvedEnemies, "44");
        addEnemy(enemyMap, enemies, resolvedEnemies, "45");
    }

    private void addEnemy(final Map<String, Enemy> enemyMap, final List<String> enemies, final List<FfEnemy> resolvedEnemies, final String enemyId) {
        final FfEnemy enemy = (FfEnemy) enemyMap.get(enemyId);
        if (enemyStatusEvaluator.isAlive(enemy, 2)) {
            enemies.add(enemyId);
            resolvedEnemies.add(enemy);
        }
    }

}
