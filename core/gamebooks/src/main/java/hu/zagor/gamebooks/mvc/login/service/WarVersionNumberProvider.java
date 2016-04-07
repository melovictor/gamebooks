package hu.zagor.gamebooks.mvc.login.service;

import java.io.File;

/**
 * Prod implementation that reads the deployment date of the war file and assembles a dynamic version number out of this information.
 * @author Tamas_Szekeres
 */
public class WarVersionNumberProvider extends FileBasedVersionNumberProvider {

    @Override
    protected File getFile() {
        return new File("/var/lib/tomcat7/webapps/gamebooks.war");
    }

}
