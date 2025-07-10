package com.neu.nuboard.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:80"}, allowCredentials = "true")
public class AuthController {

    @RequestMapping("/")
    public String home() {
        return "Welcome!";
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> user(OAuth2AuthenticationToken token) {
        if (token == null) {
            return ResponseEntity.ok(null);
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", token.getPrincipal().getAttribute("email"));
        userInfo.put("name", token.getPrincipal().getAttribute("name"));
        
        return ResponseEntity.ok(userInfo);
    }
}