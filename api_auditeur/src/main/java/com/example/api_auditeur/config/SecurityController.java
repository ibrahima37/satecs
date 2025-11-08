package com.example.api_auditeur.config;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @GetMapping("/utilisateur")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }
}