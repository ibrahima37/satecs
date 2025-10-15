package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service @AllArgsConstructor
public class PaiementService {

    private PaiementRepository paiementRepo;

    public Paiement enregistrerPaiement(Paiement paiement){
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        paiement.setDatePaiement(LocalDate.now());
        return paiementRepo.save(paiement);
    }

    public Paiement validerPaiement(Long paiementId){
        Paiement paiement = paiementRepo.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paiement.setStatutPaiement(StatutPaiement.VALIDE);

        return paiementRepo.save(paiement);
    }

    public Paiement annulerPaiement(Long paiementId){
        Paiement paiement = paiementRepo.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paiement.setStatutPaiement(StatutPaiement.ANNULER);

        return paiementRepo.save(paiement);
    }

    public Paiement rembourserPaiement(Long paiementId){
        Paiement paiement = paiementRepo.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        if (paiement.getStatutPaiement() != StatutPaiement.VALIDE) {
            throw new IllegalStateException("Seuls les paiements validés peuvent être remboursés.");
        }

        paiement.setStatutPaiement(StatutPaiement.REMBOURSE);
        return paiementRepo.save(paiement);
    }
}
