package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;

    @NotBlank(message = "L'objet est obligatoire")
    @Size(min = 3, max = 200, message = "L'objet doit contenir entre 3 et 200 caractères")
    private String objet;

    @NotBlank(message = "Le contenu est obligatoire")
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    private String contenu;

    @NotNull(message = "Le type est obligatoire")
    private TypeFormation type;

    private LocalDate dateEnvoi;

    private Boolean envoyee;

    @NotBlank(message = "Le destinataire est obligatoire")
    private String destinataire; // Email ou numéro de téléphone

    @NotNull(message = "Le canal de notification est obligatoire")
    private CanalNotification canalNotification;
}