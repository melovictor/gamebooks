package hu.zagor.gamebooks.mvc.login.domain;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Enum for the login status.
 * @author Tamas_Szekeres
 */
@XmlEnum
public enum LoginStatus {
    ok,
    failure

}
