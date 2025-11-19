package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.EtatInscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class InscriptionDto {

    private Long id;

    private Long formationId;
    private Long utilisateurId;
    private Long paiementId;

    private String dateInscription;
    private EtatInscription etatInscription;

    private String numeroCni;
    private String dateNaissance;
    private String address;
    private String numeroTel;

    private String fichier;
}