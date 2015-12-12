package hu.zagor.gamebooks.security;

import hu.zagor.gamebooks.mvc.login.service.LoginResultHandler;
import hu.zagor.gamebooks.mvc.logout.handler.ResettingLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Class for setting up the spring security.
 * @author Tamas_Szekeres
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired private AuthenticationProvider authenticationProvider;
    @Autowired private ResettingLogoutHandler logoutHandler;
    @Autowired private LoginResultHandler loginResultHandler;
    @Autowired private AccessDeniedHandler accessDeniedHandler;
    @Autowired private RequestMatcher requestMatcher;
    @Autowired private RememberMeServices rememberMeService;

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
        http.authorizeRequests().antMatchers("/resources/**").permitAll().anyRequest().authenticated();
        http.csrf().requireCsrfProtectionMatcher(requestMatcher);
        http.formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccessful", true).failureHandler(loginResultHandler).permitAll();
        http.logout().addLogoutHandler(logoutHandler).logoutSuccessUrl("/login").permitAll();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        http.rememberMe().rememberMeServices(rememberMeService).authenticationSuccessHandler(loginResultHandler);
    }
}
