package com.example.api_auditeur.model;

import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Paiement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numPaiement;
    private String numTransaction;
    private Double montant;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statutPaiement;

    private LocalDate DatePaiement;

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;
}
