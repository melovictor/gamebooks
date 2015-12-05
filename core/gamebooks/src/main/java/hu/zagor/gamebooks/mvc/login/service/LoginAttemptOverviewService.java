package hu.zagor.gamebooks.mvc.login.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Service to prevent the user being logged in in case of the detection of a brute force login attempt.
 * @author Tamas_Szekeres
 */
@Service
public class LoginAttemptOverviewService {

    private static final int EXPIRATION_TIME = 10;
    private static final int MAX_ATTEMPT = 5;
    private final LoadingCache<String, Integer> attemptsCache;

    /**
     * Default constructor setting up the cache to use for storing the potential attacker addresses.
     * @param builder the {@link CacheBuilder} object to use to build up the cache
     * @param cacheLoader the {@link CacheLoader} implementation to initialize the newly created caches
     */
    @Autowired
    public LoginAttemptOverviewService(final CacheBuilder<Object, Object> builder, final ZeroInitializedCacheLoader cacheLoader) {
        attemptsCache = builder.expireAfterWrite(EXPIRATION_TIME, TimeUnit.MINUTES).build(cacheLoader);
    }

    /**
     * Mark that the login has succeeded from the specified IP address. Clears the error counter for this specific IP address
     * @param ipAddress the IP address of the visitor
     */
    public void loginSucceeded(final String ipAddress) {
        attemptsCache.invalidate(ipAddress);
    }

    /**
     * Mark a failed login attempt from a specific IP address. Increases the error counter for the address to detect a potential brute force attack.
     * @param ipAddress the IP address of the visitor
     */
    public void loginFailed(final String ipAddress) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(ipAddress);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ipAddress, attempts);
    }

    /**
     * Check whether a specific IP address has been blocked or not.
     * @param ipAddress the address of the visitor
     * @return true if the IP address is currently blocked, false otherwise
     */
    public boolean isBlocked(final String ipAddress) {
        return attemptsCache.getUnchecked(ipAddress) >= MAX_ATTEMPT;
    }
}
