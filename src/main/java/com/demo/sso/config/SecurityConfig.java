package com.demo.sso.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Security configuration for OAuth2 authentication with Azure AD.
 *
 * This configuration enables OAuth2 login using Microsoft Azure AD,
 * allowing users to authenticate with their Office 365 credentials.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain for OAuth2 authentication.
     *
     * @param http HttpSecurity to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring security filter chain");

        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to home page and error pages
                .requestMatchers("/", "/login", "/error", "/webjars/**", "/css/**", "/js/**").permitAll()
                // Allow access to actuator health endpoint
                .requestMatchers("/actuator/health").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                // Customize login page if needed
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
            );

        return http.build();
    }

    /**
     * Custom logout success handler to log logout events.
     *
     * @return LogoutSuccessHandler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication != null && authentication.getName() != null) {
                log.info("User {} logged out successfully", authentication.getName());
            }
            response.sendRedirect("/");
        };
    }
}
