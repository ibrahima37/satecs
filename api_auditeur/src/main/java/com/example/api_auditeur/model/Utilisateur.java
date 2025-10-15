package com.example.api_auditeur.model;

import com.example.api_auditeur.model.page_enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "utilisateur")
    private List<Inscription> inscriptionList;
}
