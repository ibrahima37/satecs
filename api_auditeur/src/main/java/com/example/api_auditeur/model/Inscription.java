package com.example.api_auditeur.model;

import com.example.api_auditeur.model.page_enum.EtatInscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Inscription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne      //@OneToOne
    @JoinColumn(name = "formation_id")
    //@JsonBackReference
    @JsonIgnore
    private Formation formation;
    private LocalDate dateInscription;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

    @Enumerated(EnumType.STRING)
    private EtatInscription etatInscription;
}
