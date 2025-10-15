package com.example.api_auditeur.model;

import com.example.api_auditeur.model.page_enum.NiveauFormation;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Formation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate deteFin;
    private int capacite;
    private Double tarif;

    @Enumerated(EnumType.STRING)
    private TypeFormation typeFormation;

    @Enumerated(EnumType.STRING)
    private NiveauFormation niveauFormation;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    @OneToMany(mappedBy = "formation")
    private List<Inscription> inscriptionList;

}
