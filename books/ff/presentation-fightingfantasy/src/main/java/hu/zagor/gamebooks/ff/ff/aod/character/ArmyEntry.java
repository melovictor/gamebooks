package hu.zagor.gamebooks.ff.ff.aod.character;

/**
 * Bean containing the name and amount of a specific branch of the army.
 * @author Tamas_Szekeres
 */
public class ArmyEntry {
    private final String name;
    private final int amount;

    /**
     * Basic constructor.
     * @param name the name of the squadron
     * @param amount the amount of creatures in it
     */
    public ArmyEntry(final String name, final int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

}
