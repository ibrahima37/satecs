package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
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
public class CreateNotificationRequest {

    @NotBlank(message = "L'objet est obligatoire")
    private String objet;

    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;

    @NotNull(message = "Le type est obligatoire")
    private TypeFormation type;

    @NotBlank(message = "Le destinataire est obligatoire")
    private String destinataire;

    @NotNull(message = "Le canal de notification est obligatoire")
    private CanalNotification canalNotification;

    // Optionnel : envoyer immédiatement ou planifier
    private Boolean envoyerImmediatement = true;
}