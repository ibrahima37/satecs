package com.example.api_auditeur.config;

import com.example.api_auditeur.service.JwtService;
import com.example.api_auditeur.service.UtilisateurService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UtilisateurService utilisateurService) {
        return new JwtAuthenticationFilter(jwtService, utilisateurService);
    }
}
