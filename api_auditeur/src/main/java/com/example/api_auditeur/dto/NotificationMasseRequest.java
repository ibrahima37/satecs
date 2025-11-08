package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMasseRequest {

    @NotBlank(message = "L'objet est obligatoire")
    private String objet;

    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;

    @NotNull(message = "Le type est obligatoire")
    private TypeFormation type;

    @NotEmpty(message = "La liste des destinataires ne peut pas être vide")
    private List<String> destinataires;

    @NotNull(message = "Le canal de notification est obligatoire")
    private CanalNotification canalNotification;
}
