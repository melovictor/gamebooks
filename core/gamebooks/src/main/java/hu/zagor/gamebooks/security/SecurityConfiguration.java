package hu.zagor.gamebooks.security;

import hu.zagor.gamebooks.mvc.login.service.LoginFailureHandler;
import hu.zagor.gamebooks.mvc.logout.handler.ResettingLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Class for setting up the spring security.
 * @author Tamas_Szekeres
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired @Qualifier("activeLoginFacade") private AuthenticationProvider authenticationProvider;
    @Autowired private ResettingLogoutHandler logoutHandler;
    @Autowired private LoginFailureHandler loginResultHandler;
    @Autowired @Qualifier("csrfAccessDeniedHandler") private AccessDeniedHandler accessDeniedHandler;

    /**
     * Configures the global spring security.
     * @param auth the {@link AuthenticationManagerBuilder} object
     * @throws Exception when error occurs with the authentication
     */
    @Autowired
    public void configureGlobalSecurity(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final HttpSecurity basic = http.authorizeRequests().antMatchers("/resources/**").permitAll().anyRequest().authenticated().and();
        final HttpSecurity login = basic.formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccessful", true).failureHandler(loginResultHandler).permitAll().and();
        final HttpSecurity logout = login.logout().addLogoutHandler(logoutHandler).logoutSuccessUrl("/login").permitAll().and();
        logout.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

}
