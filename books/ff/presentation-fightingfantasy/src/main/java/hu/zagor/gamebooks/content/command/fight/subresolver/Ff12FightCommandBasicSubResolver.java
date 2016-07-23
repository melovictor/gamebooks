package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * FF12 setup fo the basic sub resolver.
 * @author Tamas_Szekeres
 */
public class Ff12FightCommandBasicSubResolver implements FightCommandSubResolver {
    @Autowired @Qualifier("fightCommandBasicSubResolver") private FightCommandSubResolver superResolver;
    @Autowired private HttpServletRequest request;

    @Override
    public List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        command.setLuckTestAllowed(false);
        request.setAttribute("characterRecord", "ff12characterRecord.jsp");
        return superResolver.doResolve(command, resolvationData);
    }
}
