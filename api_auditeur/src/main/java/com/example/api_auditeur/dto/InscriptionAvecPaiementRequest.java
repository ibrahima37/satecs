package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.ModePaiement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionAvecPaiementRequest {

    @NotNull(message = "L'ID de la formation est obligatoire")
    private Long formationId;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;

    // Informations du paiement
    @NotBlank(message = "Le numéro de transaction est obligatoire")
    private String numTransaction;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;
}