package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import jakarta.validation.constraints.DecimalMin;
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
public class PaiementDto {
    private Long id;

    private String numPaiement; // Auto-généré

    @NotBlank(message = "Le numéro de transaction est obligatoire")
    @Size(min = 5, max = 100, message = "Le numéro de transaction doit contenir entre 5 et 100 caractères")
    private String numTransaction;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private Double montant;

    @NotNull(message = "Le statut du paiement est obligatoire")
    private StatutPaiement statutPaiement;

    private LocalDate datePaiement; // Auto-généré si non fourni

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;
}
