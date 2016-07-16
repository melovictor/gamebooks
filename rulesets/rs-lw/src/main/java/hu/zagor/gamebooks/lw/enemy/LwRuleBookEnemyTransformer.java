package hu.zagor.gamebooks.lw.enemy;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.enemy.BookEnemyTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.enemy.Enemy;
import java.util.Map;
import org.w3c.dom.Document;

public class LwRuleBookEnemyTransformer extends AbstractTransformer implements BookEnemyTransformer {

    @Override
    public Map<String, Enemy> transformEnemies(final Document document) throws XmlTransformationException {
        // TODO Auto-generated method stub
        return null;
    }

}
