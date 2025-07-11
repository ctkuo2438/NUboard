package com.neu.nuboard.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import com.neu.nuboard.repository.UserRepository;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringConfig {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("=== CONFIGURING SECURITY FILTER CHAIN ===");

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    session.maximumSessions(1);
                    session.sessionFixation().migrateSession();
                })
                .authorizeHttpRequests(registry -> {
                    // *** IMPORTANT: Order matters! Most specific rules first ***

                    // Public endpoints (no authentication required)
                    registry.requestMatchers("/", "/login", "/oauth2/**", "/error").permitAll();
                    registry.requestMatchers("/api/oauth2/code/*").permitAll();

                    // API endpoints
                    registry.requestMatchers("/api/locations", "/api/colleges").permitAll();
                    registry.requestMatchers("/api/test/public").permitAll();

                    // TEST ENDPOINTS
                    registry.requestMatchers("/api/test/**").permitAll();

                    // Admin endpoints
                    registry.requestMatchers("/api/admin/**").hasRole("ADMIN");

                    // Protected API endpoints (requires authentication)
                    registry.requestMatchers("/api/**").authenticated();

                    // Everything else requires authentication
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                    .successHandler((request, response, authentication) -> {
                        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                        String email = oauthToken.getPrincipal().getAttribute("email");
                        if (userRepository.findByEmail(email).isEmpty()) {
                            response.sendRedirect("http://localhost:5173/create-profile");
                        } else {
                            response.sendRedirect("http://localhost:5173/nuboard");
                        }
                    })
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}