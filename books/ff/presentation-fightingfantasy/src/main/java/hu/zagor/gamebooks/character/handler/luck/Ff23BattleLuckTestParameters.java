package hu.zagor.gamebooks.character.handler.luck;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Battle luck test parameter provider for FF23.
 * @author Tamas_Szekeres
 */
@Component("ff23BattleLuckTestParameters")
public class Ff23BattleLuckTestParameters extends BattleLuckTestParameters {

    @Autowired
    private HttpServletRequest request;

    /**
     * Default constructor for this book.
     */
    public Ff23BattleLuckTestParameters() {
        super(2, 1, 1, 1);
    }

    @Override
    public int getLuckyAttackAddition() {
        int add;
        if (isMorganaAttacking()) {
            add = 1;
        } else {
            add = super.getLuckyAttackAddition();
        }
        return add;
    }

    private boolean isMorganaAttacking() {
        final String enemyId = request.getParameter("id");
        return "42".equals(enemyId);
    }
}
