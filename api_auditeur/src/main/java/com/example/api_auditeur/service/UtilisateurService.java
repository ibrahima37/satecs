package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @AllArgsConstructor
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepo;

    private final PasswordEncoder passwordEncoder;

    public Utilisateur login(Utilisateur u){
        u.setMotDePasse(new BCryptPasswordEncoder().encode(u.getMotDePasse()));
        return utilisateurRepo.save(u);
    }

    public Optional<Utilisateur> authentifier(String email, String motDePasse){
        return Optional.ofNullable(utilisateurRepo.findByEmailAndMotDePass(email, motDePasse)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect")));
    }

    public Utilisateur modifierMotDePasse(String email, String nouveauMDP){
        Utilisateur u = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email utilisateur non trouvable"));
        u.setMotDePasse(passwordEncoder.encode(nouveauMDP));
        return utilisateurRepo.save(u);
    }
}
