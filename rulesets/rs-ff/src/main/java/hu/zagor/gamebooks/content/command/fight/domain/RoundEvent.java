package hu.zagor.gamebooks.content.command.fight.domain;

import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.TrueCloneable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing an end-of-round event.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class RoundEvent implements TrueCloneable {

    public static final int NOT_SPECIFIED = -1;
    public static final int ALL_ROUNDS = -2;
    public static final int TOTAL_NOT_MEANINGFUL = -3;

    private int totalCount = NOT_SPECIFIED;
    private int subsequentCount = NOT_SPECIFIED;
    private String enemyId;
    private FfParagraphData paragraphData;
    private FightRoundResult roundResult;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }

    public FfParagraphData getParagraphData() {
        return paragraphData;
    }

    public void setParagraphData(final FfParagraphData paragraphData) {
        this.paragraphData = paragraphData;
    }

    @Override
    public RoundEvent clone() throws CloneNotSupportedException {
        final RoundEvent cloned = (RoundEvent) super.clone();

        cloned.paragraphData = paragraphData.clone();

        return cloned;
    }

    public String getEnemyId() {
        return enemyId;
    }

    public void setEnemyId(final String enemyId) {
        this.enemyId = enemyId;
    }

    public FightRoundResult getRoundResult() {
        return roundResult;
    }

    public void setRoundResult(final FightRoundResult roundResult) {
        this.roundResult = roundResult;
    }

    public int getSubsequentCount() {
        return subsequentCount;
    }

    public void setSubsequentCount(final int subsequentCount) {
        this.subsequentCount = subsequentCount;
    }

}
