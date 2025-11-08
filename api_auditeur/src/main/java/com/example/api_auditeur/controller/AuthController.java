package com.example.api_auditeur.controller;

import com.example.api_auditeur.config.JwtUtil;
import com.example.api_auditeur.dto.RegisterRequest;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;

    public AuthController(AuthenticationManager authenticationManager, UtilisateurService utilisateurService) {
        this.authenticationManager = authenticationManager;
        this.utilisateurService = utilisateurService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Utilisateur utilisateur = utilisateurService.enregistrer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Utilisateur enregistré avec succès",
                "email", utilisateur.getEmail(),
                "role", utilisateur.getRole()
        ));
    }

//    @PostMapping("/token")
//    public ResponseEntity<?> generateToken(@RequestBody Utilisateur request) {
//        System.out.println(">>> Tentative d'authentification pour : " + request.getEmail());
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
//        );
//
//        List<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .map(r -> r.replace("", "")) //ajout
//                .toList();
//
//        String token = JwtUtil.generateToken(request.getEmail(), roles); // ou "ADMIN"
//        return ResponseEntity.ok(Map.of("token", token, "roles",roles));
//    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities());
    }

}

