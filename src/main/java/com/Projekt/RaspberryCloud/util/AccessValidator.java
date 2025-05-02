package com.Projekt.RaspberryCloud.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AccessValidator {

    public static boolean canAccess(String targetUsername, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String loggedInUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        return loggedInUsername.equals(targetUsername);
    }
}
