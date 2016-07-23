package hu.zagor.gamebooks.content.command.fight;

/**
 * Utility class containing the widely-used fight commands.
 * @author Tamas_Szekeres
 */
public final class LastFightCommand {
    public static final String ENEMY_ID = "enemyId";
    public static final String LUCK_ON_HIT = "luckOnHit";
    public static final String LUCK_ON_DEFENSE = "luckOnDefense";
    public static final String LUCK_ON_OTHER = "luckOnOther";
    public static final String SPECIAL = "special";

    private LastFightCommand() {
        throw new UnsupportedOperationException();
    }

}
