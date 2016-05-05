package hu.zagor.gamebooks.character.handler.luck;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Battle luck test parameters for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class Ff60BattleLuckTestParameters extends BattleLuckTestParameters {
    @Autowired private HttpServletRequest request;

    /**
     * Default constructor for this book.
     */
    public Ff60BattleLuckTestParameters() {
        super(2, 1, 1, 1);
    }

    @Override
    public int getLuckyAttackAddition() {
        int luckyAttackAddition;

        if (isGiantMantisAttacking()) {
            luckyAttackAddition = 1;
        } else {
            luckyAttackAddition = super.getLuckyAttackAddition();
        }

        return luckyAttackAddition;
    }

    private boolean isGiantMantisAttacking() {
        final String enemyId = request.getParameter("id");
        return "53".equals(enemyId);
    }
}
