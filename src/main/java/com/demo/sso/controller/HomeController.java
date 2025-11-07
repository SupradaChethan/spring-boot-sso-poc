package com.demo.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller for handling public and authenticated home pages.
 */
@Slf4j
@Controller
public class HomeController {

    /**
     * Public landing page.
     *
     * @return view name
     */
    @GetMapping("/")
    public String index() {
        log.debug("Accessing public landing page");
        return "index";
    }

    /**
     * Custom login page.
     *
     * @return view name
     */
    @GetMapping("/login")
    public String login() {
        log.debug("Accessing login page");
        return "login";
    }

    /**
     * Authenticated home page showing user information.
     *
     * @param principal OAuth2 authenticated user
     * @param model Spring MVC model
     * @return view name
     */
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            // Try multiple attributes for username (Azure AD may use different claim names)
            String username = getAttributeValue(principal, "preferred_username", "email", "upn", "unique_name");
            String name = getAttributeValue(principal, "name", "given_name");
            String email = getAttributeValue(principal, "email", "preferred_username", "upn");

            log.info("User {} accessed home page", username);

            model.addAttribute("username", username != null ? username : "Unknown");
            model.addAttribute("name", name != null ? name : "Unknown User");
            model.addAttribute("email", email != null ? email : "No email");
            model.addAttribute("attributes", principal.getAttributes());
        }
        return "home";
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
