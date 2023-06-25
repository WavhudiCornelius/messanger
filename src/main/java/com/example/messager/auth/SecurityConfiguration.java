package com.example.messager.auth;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.messager.user.service.UserService;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
    public static final String DEFAULT_SUCCESS_URL = "/home";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String[] ENDPOINTS_WHITELIST = {
            "/css/**",
            "/",
            LOGIN_URL,
            "/home",
            "**/auth/**",
            "**/signup"
    };
    private final JwtAthFilter jwtAthFilter;
    private final UserService userService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests(request ->
//                request.antMatchers(ENDPOINTS_WHITELIST).permitAll()
//                        .anyRequest().authenticated()
//                        .and()
//                        .authenticationProvider(authenticationProvider())
//                        .addFilterBefore(jwtAthFilter, UsernamePasswordAuthenticationFilter.class))
//                .csrf().disable()
//                .formLogin(form -> form
//                        .loginPage(LOGIN_URL)
//                        .loginProcessingUrl(LOGIN_URL)
//                        .failureUrl(LOGIN_FAIL_URL)
//                        .usernameParameter(USERNAME)
//                        .passwordParameter(PASSWORD)
//                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL))
//                .logout(logout -> logout
//                        .logoutUrl(LOGOUT_URL)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .logoutSuccessUrl(LOGIN_URL + "?/logout"))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .invalidSessionUrl("/invalidSession.htm")
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(true));
//
//        return http.build();
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/authenticate", "/users/signup")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
}
