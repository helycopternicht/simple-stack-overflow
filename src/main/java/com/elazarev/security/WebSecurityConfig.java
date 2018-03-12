package com.elazarev.security;

import com.elazarev.domain.User;
import com.elazarev.exceptions.UserNotFoundException;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring security configuration class.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 17.02.18
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * User service.
     */
    @Autowired
    private UserService userService;

    /**
     * Configures unsecured and secured endpoints. Also specifies login and logout urls.
     * @param http configuration element.
     * @throws Exception if error ocur.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] freeRotes = new String[]{
                "/",
                "/questions",
                "/questions/searchPage",
                "/questions/{id}",
                "/users",
                "/users/{name}",
                "/tags",
                "/tags/{name}",
                "/login",
                "/logout",
                "/registration" };


        http.authorizeRequests()
                .antMatchers(freeRotes).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successForwardUrl("/").permitAll()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/login");
    }

    /**
     * UserDetailsService bean.
     * @return service based on user domain.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return s -> {
            User u = userService.findByLogin(s);
            if (u != null) {
                return u;
            }
            throw new UserNotFoundException("User not found: " + s);
        };
    }

    /**
     * Password encoder bean using to encode user passwords by {@link BCryptPasswordEncoder}.
     * @return encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
