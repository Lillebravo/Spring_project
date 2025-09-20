package com.jerry.school_project.config;

import com.jerry.school_project.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security configuration
 * Sets up JWT authentication, authorization rules, and security components
 */
@Configuration
@EnableMethodSecurity // Enables @PreAuthorize and similar annotations on methods
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // Creates the JWT authentication filter bean
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // Main security configuration - defines the security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPointJwt authEntryPointJwt, AuthAccessDeniedHandler authAccessDeniedHandler) throws Exception {
        http
                .cors().and()
                .csrf().disable() // Disable CSRF since JWT is used (stateless)
                // Configure custom exception handlers
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPointJwt)   // Handle 401 Unauthorized
                        .accessDeniedHandler(authAccessDeniedHandler) // Handle 403 Forbidden
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // ADMIN-only endpoints - only users with ADMIN role can access
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/loans/{id}/extend**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // USER or ADMIN for all other loan operations - both roles can access
                        .requestMatchers("/api/loans/**").hasAnyRole("USER", "ADMIN")

                        // Everything else is publicly accessible (no authentication required)
                        .anyRequest().permitAll()
                );

        // Set up authentication provider and JWT filter
        http.authenticationProvider(authenticationProvider());
        // Add JWT filter before the standard username/password authentication filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Authentication manager bean - handles authentication process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // DAO authentication provider - connects user details service with password encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // How to load user details
        authProvider.setPasswordEncoder(passwordEncoder); // How to encode/verify passwords
        return authProvider;
    }

}
