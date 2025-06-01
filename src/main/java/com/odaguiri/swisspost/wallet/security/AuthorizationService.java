package com.odaguiri.swisspost.wallet.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {

    /**
     * Checks if the authenticated user matches the provided email
     * @param email Email to validate against the authenticated user
     * @return true if the authenticated user matches the email
     */
    public boolean isCurrentUser(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !email.equals(authentication.getName())) {
            return false;
        }
        return email.equals(authentication.getName());
    }
}
