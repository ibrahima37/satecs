package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInscriptionRequest {

    @NotNull(message = "L'ID de la formation est obligatoire")
    private Long formationId;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;

    // Optionnel : si le paiement est déjà créé
    private Long paiementId;

    private Role role;
}