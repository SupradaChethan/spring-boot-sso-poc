package com.demo.sso;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Spring Boot SSO POC with Office 365 integration.
 *
 * This application demonstrates Single Sign-On (SSO) using Microsoft Azure AD
 * OAuth2 authentication, allowing users to login with their Office 365 credentials.
 */
@Slf4j
@SpringBootApplication
public class SsoApplication {

    public static void main(String[] args) {
        log.info("Starting Spring Boot SSO POC Application...");
        SpringApplication.run(SsoApplication.class, args);
        log.info("Application started successfully");
    }
}
