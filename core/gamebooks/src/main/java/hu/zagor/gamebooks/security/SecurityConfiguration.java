package hu.zagor.gamebooks.security;

import hu.zagor.gamebooks.mvc.logout.handler.ResettingLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * Class for setting up the spring security.
 * @author Tamas_Szekeres
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired @Qualifier("activeLoginFacade") private AuthenticationProvider authenticationProvider;
    @Autowired private ResettingLogoutHandler logoutHandler;

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
        final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry requestAuthorizator = http.authorizeRequests();
        final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry allowAccessToResources = requestAuthorizator.antMatchers("/resources/**")
            .permitAll();
        final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry disallowOtherAccess = allowAccessToResources.anyRequest().authenticated();
        disallowOtherAccess.and().formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccessful", true).permitAll().and().logout().addLogoutHandler(logoutHandler)
            .logoutSuccessUrl("/login").permitAll();
    }
}
