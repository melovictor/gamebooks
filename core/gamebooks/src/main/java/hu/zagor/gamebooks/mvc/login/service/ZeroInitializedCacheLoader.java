package hu.zagor.gamebooks.mvc.login.service;

import org.springframework.stereotype.Component;
import com.google.common.cache.CacheLoader;

/**
 * Cache loader that initializes the newly loaded cache with 0.
 * @author Tamas_Szekeres
 */
@Component
public class ZeroInitializedCacheLoader extends CacheLoader<String, Integer> {
    @Override
    public Integer load(final String key) {
        return 0;
    }
}
