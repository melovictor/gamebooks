package hu.zagor.gamebooks.character.handler.luck;

/**
 * Bean for storing the damage-modifiers for a luck test during battle.
 * @author Tamas_Szekeres
 */
public class BattleLuckTestParameters {

    private final int luckyAttackAddition;
    private final int unluckyAttackDeduction;
    private final int luckyDefenseDeduction;
    private final int unluckyDefenseAddition;

    /**
     * Basic constructor specifying the values of the different additions/reductions a luck test during a
     * battle results.
     * @param luckyAttackAddition the extra damage we cause in case of a successful luck test during attack
     * @param unluckyAttackDeduction the amount by which the damage we caused is reduced by in case of an
     * unsuccessful luck test during attack
     * @param luckyDefenseDeduction the amount by which the damage we suffer is reduced in case of a
     * successful luck test during defense
     * @param unluckyDefenseAddition the extra damage we suffer in case of an unsuccessful luck test during
     * defense
     */
    public BattleLuckTestParameters(final int luckyAttackAddition, final int unluckyAttackDeduction, final int luckyDefenseDeduction,
            final int unluckyDefenseAddition) {
        this.luckyAttackAddition = luckyAttackAddition;
        this.unluckyAttackDeduction = unluckyAttackDeduction;
        this.luckyDefenseDeduction = luckyDefenseDeduction;
        this.unluckyDefenseAddition = unluckyDefenseAddition;
    }

    public int getLuckyAttackAddition() {
        return luckyAttackAddition;
    }

    public int getUnluckyAttackDeduction() {
        return unluckyAttackDeduction;
    }

    public int getLuckyDefenseDeduction() {
        return luckyDefenseDeduction;
    }

    public int getUnluckyDefenseAddition() {
        return unluckyDefenseAddition;
    }

}
