package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.*;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.UtilisateurDetails;
import com.example.api_auditeur.service.CustomUserDetailsService;
import com.example.api_auditeur.service.JwtService;
import com.example.api_auditeur.service.UtilisateurService;
import jakarta.mail.Message;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private JavaMailSender mailSender;

    private static final Logger auditLogger = LoggerFactory.getLogger(UtilisateurService.class);


    @PostMapping("/register")
    public ResponseEntity<Utilisateur> inscription(@RequestBody RegisterRequest request){
        Utilisateur utilisateur = utiService.enregistrer(request);

        envoyerEmailConfirmation(utilisateur);

        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    private void envoyerEmailConfirmation(Utilisateur utilisateur) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(utilisateur.getEmail());
        message.setSubject("Confirmation de création de compte");
        message.setText("Bonjour " + utilisateur.getNom() + ' ' + utilisateur.getPrenom() +",\n\nVotre compte a été créé avec succès !");
        mailSender.send(message);
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

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Utilisateur utilisateur = utiService.getUserById(id);
        return ResponseEntity.ok(utilisateur);
    }
}
