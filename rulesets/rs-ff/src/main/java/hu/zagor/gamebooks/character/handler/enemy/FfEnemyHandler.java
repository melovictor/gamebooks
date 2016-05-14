package hu.zagor.gamebooks.character.handler.enemy;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for returning data about the enemies.
 * @author Tamas_Szekeres
 */
public class FfEnemyHandler implements BeanFactoryAware {
    @Autowired private HttpServletRequest request;
    private BeanFactory beanFactory;

    /**
     * Checks whether a specific enemy is alive at the moment or not.
     * @param enemyId the id of the enemy to check
     * @return true if the enemy is alive, false otherwise
     */
    public boolean isEnemyAlive(final String enemyId) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final FfEnemy enemy = (FfEnemy) wrapper.getEnemies().get(enemyId);
        return enemy.getStamina() > enemy.getFleeAtStamina();
    }

    private HttpSessionWrapper getWrapper(final HttpServletRequest request) {
        return (HttpSessionWrapper) beanFactory.getBean("httpSessionWrapper", request);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
