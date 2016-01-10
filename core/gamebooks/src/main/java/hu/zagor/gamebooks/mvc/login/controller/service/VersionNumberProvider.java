package hu.zagor.gamebooks.mvc.login.controller.service;

/**
 * Interface for acquiring the current version number.
 * @author Tamas_Szekeres
 */
public interface VersionNumberProvider {

    /**
     * Fetches the current version number.
     * @return the version number
     */
    String getVersion();

}
