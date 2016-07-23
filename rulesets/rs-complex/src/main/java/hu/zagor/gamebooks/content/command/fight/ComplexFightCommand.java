package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.ComplexParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bean for storing fight-related data.
 * @author Tamas_Szekeres
 * @param <E> the actual {@link Enemy} for the rulesystem
 */
public class ComplexFightCommand<E extends Enemy> extends Command {

    public static final String FLEEING = "fleeing";
    public static final String ATTACKING = "attacking";

    private List<String> enemies = new ArrayList<>();
    private List<FightOutcome> win = new ArrayList<>();
    private ComplexParagraphData flee;
    private FightFleeData fleeData;
    private boolean keepOpen;
    private ComplexParagraphData lose;
    private boolean ongoing = true;
    private int roundNumber;
    private boolean fleeAllowed;
    private final List<E> resolvedEnemies = new ArrayList<>();

    @Autowired private FightCommandMessageList messages;

    @Override
    public ComplexFightCommand<E> clone() throws CloneNotSupportedException {
        @SuppressWarnings("unchecked")
        final ComplexFightCommand<E> cloned = (ComplexFightCommand<E>) super.clone();
        cloned.enemies = new ArrayList<>(enemies);
        cloned.win = new ArrayList<>();
        for (final FightOutcome outcome : win) {
            cloned.win.add(outcome.clone());
        }
        cloned.flee = cloneObject(flee);
        cloned.fleeData = cloneObject(fleeData);
        cloned.lose = cloneObject(lose);
        return cloned;
    }

    public List<E> getResolvedEnemies() {
        return resolvedEnemies;
    }

    @Override
    public String getValidMove() {
        return "fight";
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public ComplexParagraphData getLose() {
        return lose;
    }

    public void setLose(final ComplexParagraphData lose) {
        this.lose = lose;
    }

    public boolean isKeepOpen() {
        return keepOpen;
    }

    public void setKeepOpen(final boolean keepOpen) {
        this.keepOpen = keepOpen;
    }

    public void setOngoing(final boolean ongoing) {
        this.ongoing = ongoing;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Increases the round number by 1.
     */
    public void increaseBattleRound() {
        roundNumber += 1;
    }

    public ComplexParagraphData getFlee() {
        return flee;
    }

    public void setFlee(final ComplexParagraphData flee) {
        this.flee = flee;
    }

    public FightFleeData getFleeData() {
        return fleeData;
    }

    public void setFleeData(final FightFleeData fleeData) {
        this.fleeData = fleeData;
    }

    public void setFleeAllowed(final boolean fleeAllowed) {
        this.fleeAllowed = fleeAllowed;
    }

    public List<FightOutcome> getWin() {
        return win;
    }

    public void setWin(final List<FightOutcome> win) {
        this.win = win;
    }

    public boolean isFleeAllowed() {
        return fleeAllowed;
    }

    public FightCommandMessageList getMessages() {
        return messages;
    }

}
