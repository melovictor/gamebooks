package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing fight-related data.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class FightCommand extends Command {

    public static final String FLEEING = "fleeing";
    public static final String ATTACKING = "attacking";

    private List<String> enemies = new ArrayList<>();
    private List<String> allies = new ArrayList<>();
    private String battleType;
    private List<FightOutcome> win = new ArrayList<>();
    private FfParagraphData flee;
    private FightFleeData fleeData;
    private int autoLoseRound;
    private int autoLoseStamina;
    private FfParagraphData lose;
    private List<RoundEvent> roundEvents = new ArrayList<>();
    private FightRoundBoundingCommand afterBounding;
    private String resolver;
    private FightRoundBoundingCommand beforeBounding;

    private boolean keepOpen;
    private final List<FfEnemy> resolvedEnemies = new ArrayList<>();
    private final List<FfAllyCharacter> resolvedAllies = new ArrayList<>();
    private Map<String, BattleStatistics> battleStatistics = new HashMap<>();

    private boolean ongoing;
    private boolean luckOnHit;
    private boolean luckOnDefense;
    private boolean luckTestAllowed = true;

    private int roundNumber;
    private boolean fleeAllowed;
    private boolean recoverDamage;
    private int attackStrengthRolledDices = 2;
    private int attackStrengthUsedDices = 2;

    private boolean preFightAvailable = true;

    private final Map<String, Integer> attackStrengths = new HashMap<String, Integer>();

    private WeaponReplacementData replacementData;

    private List<ItemType> usableWeaponTypes;
    private boolean forceOrder;
    private int maxEnemiesToDisplay = Integer.MAX_VALUE;

    @Autowired
    private FightCommandMessageList messages;

    @Override
    public FightCommand clone() throws CloneNotSupportedException {
        final FightCommand cloned = (FightCommand) super.clone();
        cloned.enemies = new ArrayList<>(enemies);
        cloned.allies = new ArrayList<>(allies);
        cloned.win = new ArrayList<>();
        for (final FightOutcome outcome : win) {
            cloned.win.add(outcome.clone());
        }
        cloned.flee = cloneObject(flee);
        cloned.fleeData = cloneObject(fleeData);
        cloned.lose = cloneObject(lose);
        cloned.roundEvents = new ArrayList<>();
        for (final RoundEvent roundEvent : roundEvents) {
            cloned.roundEvents.add(roundEvent.clone());
        }
        cloned.battleStatistics = new HashMap<>();
        for (final Entry<String, BattleStatistics> entry : battleStatistics.entrySet()) {
            cloned.battleStatistics.put(entry.getKey(), entry.getValue().clone());
        }
        cloned.afterBounding = cloneObject(afterBounding);
        cloned.beforeBounding = cloneObject(beforeBounding);
        cloned.usableWeaponTypes = new ArrayList<ItemType>(usableWeaponTypes);
        return cloned;
    }

    @Override
    public String getValidMove() {
        return "fight";
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public FfParagraphData getLose() {
        return lose;
    }

    public void setLose(final FfParagraphData lose) {
        this.lose = lose;
    }

    public String getBattleType() {
        return battleType;
    }

    public void setBattleType(final String battleType) {
        this.battleType = battleType;
    }

    @Override
    public CommandView getCommandView(final String rulesetPrefix) {
        final Map<String, Object> model = new HashMap<>();
        model.put("fightCommand", this);
        if (ongoing) {
            hideChoices(model);
        }
        if (fleeAllowed) {
            model.put("ffFleeAllowed", true);
        }

        return new CommandView(rulesetPrefix + "Fight" + battleType, model);
    }

    public List<FfEnemy> getResolvedEnemies() {
        return resolvedEnemies;
    }

    public boolean isKeepOpen() {
        return keepOpen;
    }

    public void setKeepOpen(final boolean keepOpen) {
        this.keepOpen = keepOpen;
    }

    /**
     * Get the battle statistics for a specific enemy.
     * @param id the id of the enemy
     * @return the statistics object
     */
    public BattleStatistics getBattleStatistics(final String id) {
        if (!battleStatistics.containsKey(id)) {
            battleStatistics.put(id, new BattleStatistics());
        }
        return battleStatistics.get(id);
    }

    public void setOngoing(final boolean ongoing) {
        this.ongoing = ongoing;
    }

    public List<RoundEvent> getRoundEvents() {
        return roundEvents;
    }

    public FightRoundBoundingCommand getAfterBounding() {
        return afterBounding;
    }

    public void setAfterBounding(final FightRoundBoundingCommand bounding) {
        this.afterBounding = bounding;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public boolean isLuckOnHit() {
        return luckOnHit;
    }

    public void setLuckOnHit(final boolean luckOnHit) {
        this.luckOnHit = luckOnHit;
    }

    public boolean isLuckOnDefense() {
        return luckOnDefense;
    }

    public void setLuckOnDefense(final boolean luckOnDefense) {
        this.luckOnDefense = luckOnDefense;
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

    public FightRoundBoundingCommand getBeforeBounding() {
        return beforeBounding;
    }

    public void setBeforeBounding(final FightRoundBoundingCommand beforeBounding) {
        this.beforeBounding = beforeBounding;
    }

    public int getAutoLoseRound() {
        return autoLoseRound;
    }

    public void setAutoLoseRound(final int autoLoseRound) {
        this.autoLoseRound = autoLoseRound;
    }

    public FfParagraphData getFlee() {
        return flee;
    }

    public void setFlee(final FfParagraphData flee) {
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

    public List<String> getAllies() {
        return allies;
    }

    public List<FfAllyCharacter> getResolvedAllies() {
        return resolvedAllies;
    }

    public FfAllyCharacter getFirstAlly() {
        return resolvedAllies.get(0);
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(final String resolver) {
        this.resolver = resolver;
    }

    public List<FightOutcome> getWin() {
        return win;
    }

    public void setWin(final List<FightOutcome> win) {
        this.win = win;
    }

    public Map<String, Integer> getAttackStrengths() {
        return attackStrengths;
    }

    public WeaponReplacementData getReplacementData() {
        return replacementData;
    }

    public void setReplacementData(final WeaponReplacementData replacementData) {
        this.replacementData = replacementData;
    }

    public FightCommandMessageList getMessages() {
        return messages;
    }

    public boolean isLuckTestAllowed() {
        return luckTestAllowed;
    }

    public void setLuckTestAllowed(final boolean luckTestAllowed) {
        this.luckTestAllowed = luckTestAllowed;
    }

    public boolean isRecoverDamage() {
        return recoverDamage;
    }

    public void setRecoverDamage(final boolean recoverDamage) {
        this.recoverDamage = recoverDamage;
    }

    public List<ItemType> getUsableWeaponTypes() {
        return usableWeaponTypes;
    }

    public void setUsableWeaponTypes(final List<ItemType> usableWeaponTypes) {
        this.usableWeaponTypes = usableWeaponTypes;
    }

    public int getAutoLoseStamina() {
        return autoLoseStamina;
    }

    public void setAutoLoseStamina(final int autoLoseStamina) {
        this.autoLoseStamina = autoLoseStamina;
    }

    public int getAttackStrengthRolledDices() {
        return attackStrengthRolledDices;
    }

    public void setAttackStrengthRolledDices(final int attackStrengthRolledDices) {
        this.attackStrengthRolledDices = attackStrengthRolledDices;
    }

    public int getAttackStrengthUsedDices() {
        return attackStrengthUsedDices;
    }

    public void setAttackStrengthUsedDices(final int attackStrengthUsedDices) {
        this.attackStrengthUsedDices = attackStrengthUsedDices;
    }

    public boolean isForceOrder() {
        return forceOrder;
    }

    public void setForceOrder(final boolean forceOrder) {
        this.forceOrder = forceOrder;
    }

    public int getMaxEnemiesToDisplay() {
        return maxEnemiesToDisplay;
    }

    public void setMaxEnemiesToDisplay(final int maxEnemiesToDisplay) {
        this.maxEnemiesToDisplay = maxEnemiesToDisplay;
    }

    public boolean isPreFightAvailable() {
        return preFightAvailable;
    }

    public void setPreFightAvailable(final boolean preFightAvailable) {
        this.preFightAvailable = preFightAvailable;
    }

}
