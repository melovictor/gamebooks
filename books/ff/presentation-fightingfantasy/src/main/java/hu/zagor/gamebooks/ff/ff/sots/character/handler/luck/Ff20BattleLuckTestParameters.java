package hu.zagor.gamebooks.ff.ff.sots.character.handler.luck;

import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Battle luck test parameter provider for FF23.
 * @author Tamas_Szekeres
 */
@Component("ff20BattleLuckTestParameters")
public class Ff20BattleLuckTestParameters extends BattleLuckTestParameters {

    @Autowired private HttpServletRequest request;

    /**
     * Default constructor.
     */
    public Ff20BattleLuckTestParameters() {
        super(2, 1, 1, 1);
    }

    @Override
    public int getLuckyAttackAddition() {
        int add;
        if (isMantisAttacking()) {
            add = 1;
        } else {
            add = super.getLuckyAttackAddition();
        }
        return add;
    }

    private boolean isMantisAttacking() {
        final String enemyId = request.getParameter("id");
        return "52".equals(enemyId);
    }

}
