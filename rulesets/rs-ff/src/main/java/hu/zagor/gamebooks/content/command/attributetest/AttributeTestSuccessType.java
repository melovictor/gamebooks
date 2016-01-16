package hu.zagor.gamebooks.content.command.attributetest;

/**
 * Enum specifying the success condition for attribute tests.
 * @author Tamas_Szekeres
 */
public enum AttributeTestSuccessType {
    /**
     * The thrown value must be lower than the value being tested against.
     */
    lower,
    /**
     * The thrown value must be lower than or equal to the value being tested against.
     */
    lowerEquals,
    /**
     * The thrown value must be higher than the value being tested against.
     */
    higher

}
