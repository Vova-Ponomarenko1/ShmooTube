package org.example.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    private DataSource dataSource;
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, FROM users WHERE username = ? AND is_banned = false")
            .authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?")
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Lazy
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();
        UserDetails seller = User.builder()
            .username("composit")
            .password(passwordEncoder().encode("password"))
            .roles("COMPOSITOR")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN", "USER")
            .build();

        UserDetails superAdmin = User.builder()
            .username("v")
            .password(passwordEncoder().encode("v"))
            .roles("SUPER_ADMIN", "ADMIN", "USER", "SELLER")
            .build();


        return new InMemoryUserDetailsManager(user, seller, admin, superAdmin);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
            .requestMatchers("/static/**").permitAll()
            .requestMatchers("/ITube/**").permitAll()
            .requestMatchers("/new-user**", "/register").permitAll()




            .anyRequest().authenticated()
            .and()

            .formLogin().successHandler(new CustomSuccessHandler())
            .and()

            .logout().deleteCookies("JSESSIONID")
            .and()

            .rememberMe()
            .key("uniqueAndSecret")
            .rememberMeCookieName("remember-me")
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(15).userDetailsService(userDetailsService())
        ;
//                .useSecureCookie(true) // RememberMe only on HTTPS secured
//                .tokenRepository(jdbcTokenRepository())
        ;

        return http .authenticationProvider(authenticationProvider()).build();
    }
}




