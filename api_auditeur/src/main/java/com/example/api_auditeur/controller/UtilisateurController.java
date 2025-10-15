package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/utilisateur")
@AllArgsConstructor
@CrossOrigin("*")
public class UtilisateurController {

    private UtilisateurService utiService;

    @PostMapping
    public ResponseEntity<Utilisateur> inscription(@RequestBody Utilisateur ut){
        Utilisateur utilisateur = utiService.login(ut);
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    @PostMapping("/login")
    public ResponseEntity<Utilisateur> connexion(@RequestBody Utilisateur ut){
    Utilisateur utilisateur = utiService.authentifier(ut.getEmail(), ut.getMotDePasse())
            .orElseThrow(()-> new RuntimeException("Email ou mot de passe incorrect"));
    return ResponseEntity.ok(ut);
    }
}
