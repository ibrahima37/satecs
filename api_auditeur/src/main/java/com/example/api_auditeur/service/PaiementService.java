package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // Récupérer tous les paiements
    public List<Paiement> getAllPaiements() {
        return paiementRepo.findAll();
    }

    // Récupérer un paiement par ID
    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepo.findById(id);
    }

    // Récupérer un paiement par numéro de transaction
    public Optional<Paiement> getPaiementByNumTransaction(String numTransaction) {
        return paiementRepo.findByNumTransaction(numTransaction);
    }

    // Créer un nouveau paiement
    public Paiement createPaiement(Paiement paiement) {
        // Générer un numéro de paiement unique
        paiement.setNumPaiement(genererNumeroPaiement());

        // Générer un numéro de transaction unique
        if (paiement.getNumTransaction() == null || paiement.getNumTransaction().isEmpty()) {
            paiement.setNumTransaction(genererNumeroTransaction());
        }

        // Définir le statut initial
        if (paiement.getStatutPaiement() == null) {
            paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        }

        // Définir la date de paiement
        paiement.setDatePaiement(LocalDate.now());

        return paiementRepo.save(paiement);
    }

    // Mettre à jour un paiement
    public Paiement updatePaiement(Long id, Paiement paiementDetails) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setMontant(paiementDetails.getMontant());
        paiement.setStatutPaiement(paiementDetails.getStatutPaiement());
        paiement.setModePaiement(paiementDetails.getModePaiement());
        paiement.setNumTransaction(paiementDetails.getNumTransaction());

        return paiementRepo.save(paiement);
    }

    // Mettre à jour le statut d'un paiement
    public Paiement updateStatutPaiement(Long id, StatutPaiement statut) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setStatutPaiement(statut);
        return paiementRepo.save(paiement);
    }

    // Supprimer un paiement
    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));
        paiementRepo.delete(paiement);
    }

    // Récupérer les paiements par statut
    public List<Paiement> getPaiementsByStatut(StatutPaiement statut) {
        return paiementRepo.findByStatutPaiement(statut);
    }

    // Récupérer les paiements par mode de paiement
    public List<Paiement> getPaiementsByMode(ModePaiement mode) {
        return paiementRepo.findByModePaiement(mode);
    }

    // Récupérer les paiements par période
    public List<Paiement> getPaiementsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return paiementRepo.findByDatePaiementBetween(dateDebut, dateFin);
    }

    // Calculer le montant total des paiements
    public Double getTotalMontant() {
        return paiementRepo.calculateTotalMontant();
    }

    // Calculer le montant total par statut
    public Double getTotalMontantByStatut(StatutPaiement statut) {
        return paiementRepo.calculateTotalMontantByStatut(statut);
    }

    // Valider un paiement
    /*public Paiement validerPaiement(Long id) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        // Logique de validation (vérification, traitement, etc.)
        paiement.setStatutPaiement(StatutPaiement.VALIDE);
        paiement.setDatePaiement(LocalDate.now());

        return paiementRepo.save(paiement);
    }*/

    // Rejeter un paiement
    public Paiement rejeterPaiement(Long id) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setStatutPaiement(StatutPaiement.REJETER);

        return paiementRepo.save(paiement);
    }

    // Rembourser un paiement
  /*  public Paiement rembourserPaiement(Long id) {
        Paiement paiement = paiementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        if (paiement.getStatutPaiement() != StatutPaiement.VALIDE) {
            throw new RuntimeException("Seuls les paiements validés peuvent être remboursés");
        }

        paiement.setStatutPaiement(StatutPaiement.REMBOURSE);

        return paiementRepo.save(paiement);
    }*/

    // Méthodes privées pour générer des numéros uniques
    private String genererNumeroPaiement() {
        return "PAY-" + LocalDate.now().getYear() + "-" +
                String.format("%06d", paiementRepo.count() + 1);
    }

    private String genererNumeroTransaction() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
