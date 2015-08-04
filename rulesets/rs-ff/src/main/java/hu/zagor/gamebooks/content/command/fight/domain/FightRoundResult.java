package hu.zagor.gamebooks.content.command.fight.domain;

/**
 * Enum with the possible outcomes of a single round of fight.
 * @author Tamas_Szekeres
 */
public enum FightRoundResult {
    WIN,
    LOSE,
    NOT_WIN,
    TIE,
    IDLE,
    NOT_IDLE,
    ALL
}
