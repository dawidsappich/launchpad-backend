package de.cdiag.launchpadbackend.configuration;

import de.cdiag.launchpadbackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomAuthProvider customAuthProvider;

    public WebSecurityConfiguration(CustomAuthProvider customAuthProvider) {
        this.customAuthProvider = customAuthProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // use http basic for authentication
        http.httpBasic();

        // allow h2 to be loaded in an iframe
        http.headers().frameOptions().disable();

        // disable in favor of h2
        http.csrf().disable();

        // all request must be authenticated
        http.authorizeRequests()
                // allow any request to h2-console
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/signup").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider);
    }
}

@Component
class CustomAuthProvider implements AuthenticationProvider {

    private final UserService userService;

    CustomAuthProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // does provided password match with the password in storage?
        if (userService.matches(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.createAuthorityList("USER"));
        }
        throw new BadCredentialsException("username or password invalid");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
