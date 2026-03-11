package com.neu.nuboard.Config;

import com.neu.nuboard.repository.UserRepository;
import com.neu.nuboard.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Explicitly enable @PreAuthorize
public class SpringConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                // Use IF_REQUIRED so the session persists after login
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(registry -> {
                    // Public endpoints
                    registry.requestMatchers("/", "/login", "/oauth2/**").permitAll();
                    registry.requestMatchers("/api/locations/**", "/api/colleges/**").permitAll();
                    
                    // Specific API Endpoints - Rely on @PreAuthorize annotations in Controllers primarily,
                    // but we can keep these as broad safeguards.
                    registry.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/api/**").authenticated();

                    // Catch-all
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                    // 1. Hook in the Custom Service here
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                    )
                    // 2. Success Handler determines where they go next
                    .successHandler((request, response, authentication) -> {
                        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                        String email = oauthToken.getPrincipal().getAttribute("email");
                        
                        // Simple check: If user not in DB, go to create profile
                        if (userRepository.findByEmail(email).isEmpty()) {
                            response.sendRedirect(frontendUrl + "/create-profile");
                        } else {
                            response.sendRedirect(frontendUrl + "/nuboard");
                        }
                    })
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow the frontend URL (configurable via environment variable)
        configuration.setAllowedOrigins(Arrays.asList(frontendUrl, "http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
