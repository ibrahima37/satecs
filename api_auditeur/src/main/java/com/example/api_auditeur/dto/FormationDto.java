package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.NiveauFormation;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormationDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 200, message = "Le titre doit contenir entre 3 et 200 caractères")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, message = "La description doit contenir au moins 10 caractères")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être au moins 1")
    @Max(value = 1000, message = "La capacité ne peut pas dépasser 1000")
    private Integer capacite;

    @NotNull(message = "Le tarif est obligatoire")
    @DecimalMin(value = "0.0", message = "Le tarif doit être positif")
    private Double tarif;

    @NotNull(message = "Le type de formation est obligatoire")
    private TypeFormation typeFormation;

    @NotNull(message = "Le niveau de formation est obligatoire")
    private NiveauFormation niveauFormation;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    // Nombre d'inscrits (optionnel)
    private Integer nombreInscrits;
}