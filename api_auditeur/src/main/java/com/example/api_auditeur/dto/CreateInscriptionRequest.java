package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.EtatInscription;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInscriptionRequest {

    @NotNull(message = "L'ID de la formation est obligatoire")
    private Long formationId;
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;
    private Long paiementId;

    private LocalDate dateInscription;
    private EtatInscription etatInscription;

    private String numeroCni;
    private LocalDate dateNaissance;
    private String address;
    private String numeroTel;

    private MultipartFile fichier;

}