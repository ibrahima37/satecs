package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.*;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.Role;
import com.example.api_auditeur.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service @AllArgsConstructor
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepo;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private static final Logger auditLogger = LoggerFactory.getLogger(UtilisateurService.class);

    public Utilisateur register(Utilisateur u){
        if (utilisateurRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        u.setMotDePasse(passwordEncoder.encode(u.getMotDePasse()));
        return utilisateurRepo.save(u);
    }

    public Utilisateur enregistrer(RegisterRequest request) {
        if (utilisateurRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.getEmail());
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(request.getRole() != null ? request.getRole() : Role.AUDITEUR);

        return utilisateurRepo.save(utilisateur);
    }


    public Utilisateur modifierMotDePasse(String email, String nouveauMDP){
        Utilisateur u = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email utilisateur non trouvable"));
        u.setMotDePasse(passwordEncoder.encode(nouveauMDP));
        return utilisateurRepo.save(u);
    }

    public Utilisateur loadUserByEmail(String email) {
        return utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }

    public Utilisateur getUserById(Long id) {
        return utilisateurRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + id));
    }
}
