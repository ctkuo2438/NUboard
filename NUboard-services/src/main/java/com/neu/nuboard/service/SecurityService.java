package com.neu.nuboard.service;

import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for security-related operations.
 * Provides helper methods for authorization checks in @PreAuthorize annotations.
 */
@Service("securityService")
public class SecurityService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks if the current user is the owner of the resource (user profile).
     * Used in @PreAuthorize annotations to allow users to access/modify their own data.
     *
     * @param userId the ID of the user resource being accessed
     * @param principal the OAuth2User principal from the security context
     * @return true if the current user owns the resource, false otherwise
     */
    public boolean isOwner(String userId, OAuth2User principal) {
        if (principal == null || userId == null) {
            return false;
        }
        
        String email = principal.getAttribute("email");
        if (email == null) {
            return false;
        }
        
        try {
            Long id = Long.parseLong(userId);
            Optional<User> userOpt = userRepository.findById(id);
            return userOpt.map(user -> email.equals(user.getEmail())).orElse(false);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the current user is the owner by comparing email directly.
     *
     * @param userEmail the email of the user resource being accessed
     * @param principal the OAuth2User principal from the security context
     * @return true if the current user owns the resource, false otherwise
     */
    public boolean isOwnerByEmail(String userEmail, OAuth2User principal) {
        if (principal == null || userEmail == null) {
            return false;
        }
        
        String currentEmail = principal.getAttribute("email");
        return userEmail.equals(currentEmail);
    }
}
