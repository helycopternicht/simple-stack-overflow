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
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 17.02.18
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] freeRotes = new String[]{
                "/",
                "/questions",
                "/questions/search",
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
