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

    public Utilisateur register(Utilisateur u){
        u.setMotDePasse(passwordEncoder.encode(u.getMotDePasse()));
        return utilisateurRepo.save(u);
    }

    //CONNEXION
    public Optional<Utilisateur> login(String email, String motDePasse){
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email incorrect"));
        if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return Optional.of(utilisateur);
    }

    public Utilisateur modifierMotDePasse(String email, String nouveauMDP){
        Utilisateur u = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email utilisateur non trouvable"));
        u.setMotDePasse(passwordEncoder.encode(nouveauMDP));
        return utilisateurRepo.save(u);
    }
}
