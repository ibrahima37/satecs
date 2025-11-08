package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.EtatInscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionDto {
    private Long id;
    private LocalDate dateInscription;
    private EtatInscription etatInscription;

    // Informations de la formation
    private Long formationId;
    private String formationTitre;
    private LocalDate formationDateDebut;
    private LocalDate formationDateFin;
    private Double formationTarif;

    // Informations de l'utilisateur
    private Long utilisateurId;
    private String utilisateurNom;
    private String utilisateurPrenom;
    private String utilisateurEmail;

    // Informations du paiement
    private Long paiementId;
    private String paiementNumPaiement;
    private Double paiementMontant;
    private String paiementStatut;
}