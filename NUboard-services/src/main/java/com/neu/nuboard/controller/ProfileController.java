package com.neu.nuboard.controller;

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

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
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

    @PostMapping("/create")
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