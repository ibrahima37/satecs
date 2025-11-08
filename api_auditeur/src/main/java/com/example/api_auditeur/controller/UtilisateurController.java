package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.*;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.UtilisateurDetails;
import com.example.api_auditeur.service.CustomUserDetailsService;
import com.example.api_auditeur.service.JwtService;
import com.example.api_auditeur.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/api/utilisateur")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {

    private UtilisateurService utiService;
    private AuthController authController;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger auditLogger = LoggerFactory.getLogger(UtilisateurService.class);


    @PostMapping("/register")
    public ResponseEntity<Utilisateur> inscription(@RequestBody RegisterRequest request){
        Utilisateur utilisateur = utiService.enregistrer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
            );
        } catch (BadCredentialsException ex) {
            auditLogger.info("Bad credentials", ex);
            throw new BadCredentialsException("Identifiants invalides");
        }

        auditLogger.info("Authenticated: " + request.getEmail());

        Utilisateur utilisateur = utiService.loadUserByEmail(request.getEmail());
        String token = jwtService.generateToken(new UtilisateurDetails(utilisateur));
        return new LoginResponse(token, utilisateur);

    }

//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié");
//        }
//
//        Object principal = authentication.getPrincipal();
//
//        // Si tu as injecté un Utilisateur directement
//        if (principal instanceof Utilisateur utilisateur) {
//            return ResponseEntity.ok(utilisateur);
//        }
//
//        // Si tu utilises un UserDetails personnalisé
//        if (principal instanceof UserDetails userDetails) {
//            return ResponseEntity.ok(Map.of(
//                    "email", userDetails.getUsername(),
//                    "roles", userDetails.getAuthorities()
//            ));
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Type d'utilisateur inconnu");
//    }

}
