package com.neu.nuboard.service;

import com.neu.nuboard.model.User;
import com.neu.nuboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Delegate to the default implementation to load the user from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Extract email from Google's response
        String email = oAuth2User.getAttribute("email");

        // 3. Check if this user exists in our DB
        Optional<User> userOptional = userRepository.findByEmail(email);

        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // 4. If user exists, load their Roles & Permissions from Postgres
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Add ROLES (e.g., "ROLE_ADMIN", "ROLE_USER")
            // Spring Security expects roles to start with "ROLE_" for hasRole() checks
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                
                // Add PERMISSIONS (e.g., "EVENT_CREATE", "USER_DELETE")
                // These allow @PreAuthorize("hasAuthority('EVENT_CREATE')") to work
                role.getPermissions().forEach(permission -> 
                    authorities.add(new SimpleGrantedAuthority(permission.getName()))
                );
            });
        }

        // 5. If authorities is empty (New User), Spring Security will default to SCOPE_email etc.
        // We add the existing attributes from Google + our new DB Authorities
        return new DefaultOAuth2User(
                authorities.isEmpty() ? oAuth2User.getAuthorities() : authorities,
                oAuth2User.getAttributes(),
                "email" // The key used to look up the "name" of the user
        );
    }
}