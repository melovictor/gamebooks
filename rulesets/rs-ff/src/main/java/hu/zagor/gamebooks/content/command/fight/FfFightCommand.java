package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.domain.WeaponReplacementData;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing fight-related data.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class FfFightCommand extends ComplexFightCommand<FfEnemy> {

    private List<String> allies = new ArrayList<>();
    private String battleType;
    private int autoLoseRound;
    private int autoLoseStamina;
    private List<RoundEvent> roundEvents = new ArrayList<>();
    private FightRoundBoundingCommand afterBounding;
    private String resolver;
    private FightRoundBoundingCommand beforeBounding;

    private final List<FfAllyCharacter> resolvedAllies = new ArrayList<>();
    private Map<String, BattleStatistics> battleStatistics = new HashMap<>();

    private boolean luckOnHit;
    private boolean luckOnDefense;
    private boolean luckTestAllowed = true;

    private boolean recoverDamage;
    private int attackStrengthRolledDices = 2;
    private int attackStrengthUsedDices = 2;

    private boolean preFightAvailable = true;

    private final Map<String, Integer> attackStrengths = new HashMap<String, Integer>();

    private WeaponReplacementData replacementData;

    private List<ItemType> usableWeaponTypes;
    private boolean forceOrder;
    private int maxEnemiesToDisplay = Integer.MAX_VALUE;

    private Boolean luckOnDefenseResult;

    private boolean allyStaminaVisible;
    private List<FfSpecialAttack> specialAttacks = new ArrayList<>();

    @Override
    public FfFightCommand clone() throws CloneNotSupportedException {
        final FfFightCommand cloned = (FfFightCommand) super.clone();
        cloned.allies = new ArrayList<>(allies);
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
        cloned.specialAttacks = new ArrayList<>();
        for (final FfSpecialAttack attack : specialAttacks) {
            cloned.specialAttacks.add(attack.clone());
        }
        return cloned;
    }

    @Override
    public String getValidMove() {
        return "fight";
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
        if (isOngoing()) {
            hideChoices(model);
        }

        return new CommandView(rulesetPrefix + "Fight" + battleType, model);
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

    public List<RoundEvent> getRoundEvents() {
        return roundEvents;
    }

    public FightRoundBoundingCommand getAfterBounding() {
        return afterBounding;
    }

    public void setAfterBounding(final FightRoundBoundingCommand bounding) {
        this.afterBounding = bounding;
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

    public Map<String, Integer> getAttackStrengths() {
        return attackStrengths;
    }

    public WeaponReplacementData getReplacementData() {
        return replacementData;
    }

    public void setReplacementData(final WeaponReplacementData replacementData) {
        this.replacementData = replacementData;
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

    public Boolean getLuckOnDefenseResult() {
        return luckOnDefenseResult;
    }

    public void setLuckOnDefenseResult(final Boolean luckOnDefenseResult) {
        this.luckOnDefenseResult = luckOnDefenseResult;
    }

    public boolean isAllyStaminaVisible() {
        return allyStaminaVisible;
    }

    public void setAllyStaminaVisible(final boolean allyStaminaVisible) {
        this.allyStaminaVisible = allyStaminaVisible;
    }

    public List<FfSpecialAttack> getSpecialAttacks() {
        return specialAttacks;
    }

}
