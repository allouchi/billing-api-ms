package com.sbatec.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity // Configuration spécifique pour les architectures réactives (Gateway/WebFlux)
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // 1. On applique la configuration CORS définie plus bas
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. On désactive le CSRF (nécessaire pour les APIs Stateless / Jetons)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 3. On autorise explicitement les requêtes de Preflight (OPTIONS)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Ajoutez ici vos autres routes publiques si la Gateway valide les tokens (ex: /auth/**)
                        .anyExchange().permitAll()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Configuration identique pour Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Cache la réponse Preflight pendant 1 heure

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Attention à bien utiliser le package org.springframework.web.cors.reactive
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}