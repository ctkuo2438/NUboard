package com.neu.nuboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.neu.nuboard.service.UserService;
import com.neu.nuboard.dto.UserCreateDTO;
import com.neu.nuboard.model.User;
import org.springframework.http.ResponseEntity;
import com.neu.nuboard.exception.BusinessException;
import com.neu.nuboard.exception.ErrorCode;
import com.neu.nuboard.repository.UserRepository;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing user profile operations.
 * All endpoints require authentication via OAuth2.
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:80"}, allowCredentials = "true")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets the current user's profile.
     * Requires authentication - user can only view their own profile.
     * @param token The OAuth2 authentication token.
     * @return The user's profile information.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttribute("email");
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("program", user.getProgram());
                
                Map<String, Object> location = new HashMap<>();
                location.put("id", user.getLocation().getId());
                location.put("name", user.getLocation().getName());
                response.put("location", location);
                
                Map<String, Object> college = new HashMap<>();
                college.put("id", user.getCollege().getId());
                college.put("name", user.getCollege().getName());
                response.put("college", college);
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(ErrorCode.USER_NOT_FOUND.getHttpStatus())
                    .body(ErrorCode.USER_NOT_FOUND.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.USER_QUERY_FAILED.getHttpStatus())
                .body(ErrorCode.USER_QUERY_FAILED.getMessage());
        }
    }

    /**
     * Creates a new user profile for the authenticated user.
     * Requires authentication - only authenticated users can create their profile.
     * The email is automatically set from the OAuth2 token.
     * @param userDTO The user profile data.
     * @param token The OAuth2 authentication token.
     * @return The created user's profile information.
     */
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createProfile(@RequestBody UserCreateDTO userDTO, OAuth2AuthenticationToken token) {
        try {
            String email = token.getPrincipal().getAttribute("email");
            userDTO.setEmail(email);

            User user = userService.createUser(userDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("program", user.getProgram());
            
            Map<String, Object> location = new HashMap<>();
            location.put("id", user.getLocation().getId());
            location.put("name", user.getLocation().getName());
            response.put("location", location);
            
            Map<String, Object> college = new HashMap<>();
            college.put("id", user.getCollege().getId());
            college.put("name", user.getCollege().getName());
            response.put("college", college);
            
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.UNKNOWN_ERROR.getHttpStatus())
                .body(ErrorCode.UNKNOWN_ERROR.getMessage());
        }
    }
}
