package com.demo.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for user-related API endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * Get current authenticated user information.
     *
     * @param principal OAuth2 authenticated user
     * @return user information as JSON
     */
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> userInfo = new HashMap<>();

        if (principal != null) {
            // Try multiple attributes for username (Azure AD may use different claim names)
            String username = getAttributeValue(principal, "preferred_username", "email", "upn", "unique_name");
            String name = getAttributeValue(principal, "name", "given_name");
            String email = getAttributeValue(principal, "email", "preferred_username", "upn");

            log.info("User {} requested their profile information", username);

            userInfo.put("username", username);
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("authenticated", true);

            log.debug("Returning user info for {}", username);
        } else {
            userInfo.put("authenticated", false);
            log.warn("Unauthenticated user tried to access /api/user/me");
        }

        return userInfo;
    }

    /**
     * Get all attributes from OAuth2 user (useful for debugging).
     *
     * @param principal OAuth2 authenticated user
     * @return all user attributes
     */
    @GetMapping("/attributes")
    public Map<String, Object> getUserAttributes(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String username = getAttributeValue(principal, "preferred_username", "email", "upn", "unique_name", "sub");
            log.info("User {} requested all their attributes", username);
            return principal.getAttributes();
        }
        log.warn("Unauthenticated user tried to access /api/user/attributes");
        return new HashMap<>();
    }

    /**
     * Get attribute value with fallback options.
     * Tries each attribute name in order and returns the first non-null value.
     *
     * @param principal OAuth2 user
     * @param attributeNames attribute names to try in order
     * @return first non-null attribute value or null
     */
    private String getAttributeValue(OAuth2User principal, String... attributeNames) {
        for (String attrName : attributeNames) {
            Object value = principal.getAttribute(attrName);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
}
