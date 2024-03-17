package org.example.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig  {
    @Autowired
    private DataSource dataSource;

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, true FROM public.users WHERE username = ?")
            .authoritiesByUsernameQuery("SELECT username, role FROM public.users WHERE username = ?")
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
            .username("a")
            .password(passwordEncoder().encode("a"))
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
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            return delegate.loadUser(userRequest);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
            .requestMatchers("/ws").permitAll()
            .requestMatchers("/ws/**").permitAll()
            .requestMatchers("/static/**").permitAll()
            .requestMatchers("/ITube/**").permitAll()
            .requestMatchers("/ITube/new-user**", "/ITube/register").permitAll()
            .anyRequest().authenticated()
            .and()

            .formLogin()
            .and()

            .oauth2Login()
            .loginPage("/login")
            .defaultSuccessUrl("/googleLogin")
            .userInfoEndpoint()
            .userService(oauth2UserService())
            .and()
            .and()

            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout").permitAll()
            .deleteCookies("JSESSIONID")
            .and()

            .rememberMe()
            .key("uniqueAndSecret");

        return http.build();
    }
}





