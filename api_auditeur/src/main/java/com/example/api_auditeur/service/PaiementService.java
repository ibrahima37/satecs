package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.CreatePaiementRequest;
import com.example.api_auditeur.dto.PaiementDto;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.PaiementRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class PaiementService {

    private PaiementRepository paiementRepository;

    public Paiement enregistrerPaiement(Paiement paiement){
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        paiement.setDatePaiement(LocalDate.now());
        return paiementRepository.save(paiement);
    }
/*
    public Paiement validerPaiement(Long paiementId){
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paiement.setStatutPaiement(StatutPaiement.VALIDE);

        return paiementRepository.save(paiement);
    }*/

    public Paiement annulerPaiement(Long paiementId){
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paiement.setStatutPaiement(StatutPaiement.ANNULER);

        return paiementRepository.save(paiement);
    }

    public Paiement rembourserPaiement(Long paiementId){
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        if (paiement.getStatutPaiement() != StatutPaiement.VALIDE) {
            throw new IllegalStateException("Seuls les paiements validés peuvent être remboursés.");
        }

        paiement.setStatutPaiement(StatutPaiement.REMBOURSE);
        return paiementRepository.save(paiement);
    }

    // Récupérer tous les paiements
 /*   public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    // Récupérer un paiement par ID
    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    // Récupérer un paiement par numéro de transaction
    public Optional<Paiement> getPaiementByNumTransaction(String numTransaction) {
        return paiementRepository.findByNumTransaction(numTransaction);
    }
*/
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

        return paiementRepository.save(paiement);
    }

    // Mettre à jour un paiement
    public Paiement updatePaiement(Long id, Paiement paiementDetails) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setMontant(paiementDetails.getMontant());
        paiement.setStatutPaiement(paiementDetails.getStatutPaiement());
        paiement.setModePaiement(paiementDetails.getModePaiement());
        paiement.setNumTransaction(paiementDetails.getNumTransaction());

        return paiementRepository.save(paiement);
    }

    // Mettre à jour le statut d'un paiement
    public Paiement updateStatutPaiement(Long id, StatutPaiement statut) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setStatutPaiement(statut);
        return paiementRepository.save(paiement);
    }

    // Supprimer un paiement
    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));
        paiementRepository.delete(paiement);
    }

    // Récupérer les paiements par statut
 /*   public List<Paiement> getPaiementsByStatut(StatutPaiement statut) {
        return paiementRepository.findByStatutPaiement(statut);
    }

    // Récupérer les paiements par mode de paiement
    public List<Paiement> getPaiementsByMode(ModePaiement mode) {
        return paiementRepository.findByModePaiement(mode);
    }

    // Récupérer les paiements par période
    public List<Paiement> getPaiementsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return paiementRepository.findByDatePaiementBetween(dateDebut, dateFin);
    }
*/
    // Calculer le montant total des paiements
    public Double getTotalMontant() {
        return paiementRepository.calculateTotalMontant();
    }

    // Calculer le montant total par statut
    public Double getTotalMontantByStatut(StatutPaiement statut) {
        return paiementRepository.calculateTotalMontantByStatut(statut);
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
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setStatutPaiement(StatutPaiement.REJETER);

        return paiementRepository.save(paiement);
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
                String.format("%06d", paiementRepository.count() + 1);
    }

    private String genererNumeroTransaction() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    //aujourd'hui
    // CREATE
    @Transactional
    public PaiementDto creerPaiement(CreatePaiementRequest request) {
        // Vérifier si le numéro de transaction existe déjà
        if (paiementRepository.findByNumTransaction(request.getNumTransaction()).isPresent()) {
            throw new RuntimeException("Un paiement avec ce numéro de transaction existe déjà");
        }

        Paiement paiement = new Paiement();
        paiement.setNumPaiement(genererNumPaiement());
        paiement.setNumTransaction(request.getNumTransaction());
        paiement.setMontant(request.getMontant());
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE); // Statut par défaut
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(request.getModePaiement());

        Paiement saved = paiementRepository.save(paiement);
        return convertToDto(saved);
    }

    // READ - Get by ID
    public PaiementDto getPaiementById(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));
        return convertToDto(paiement);
    }

    // READ - Get by Numéro de Paiement
    public PaiementDto getPaiementByNumPaiement(String numPaiement) {
        Paiement paiement = paiementRepository.findByNumPaiement(numPaiement)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec le numéro: " + numPaiement));
        return convertToDto(paiement);
    }

    // READ - Get by Numéro de Transaction
    public PaiementDto getPaiementByNumTransaction(String numTransaction) {
        Paiement paiement = paiementRepository.findByNumTransaction(numTransaction)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec la transaction: " + numTransaction));
        return convertToDto(paiement);
    }

    // READ - Get All
    public List<PaiementDto> getAllPaiements() {
        return paiementRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // UPDATE - Modifier un paiement
    @Transactional
    public PaiementDto modifierPaiement(Long id, PaiementDto paiementDto) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setNumTransaction(paiementDto.getNumTransaction());
        paiement.setMontant(paiementDto.getMontant());
        paiement.setStatutPaiement(paiementDto.getStatutPaiement());
        paiement.setModePaiement(paiementDto.getModePaiement());

        Paiement updated = paiementRepository.save(paiement);
        return convertToDto(updated);
    }

    // UPDATE - Valider un paiement
    @Transactional
    public PaiementDto validerPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        if (paiement.getStatutPaiement() == StatutPaiement.VALIDE) {
            throw new RuntimeException("Ce paiement est déjà validé");
        }

        paiement.setStatutPaiement(StatutPaiement.VALIDE);
        Paiement updated = paiementRepository.save(paiement);
        return convertToDto(updated);
    }

    // UPDATE - Annuler un paiement
    @Transactional
    public PaiementDto annulerPaiement(Long id, String motif) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        if (paiement.getStatutPaiement() == StatutPaiement.ANNULER) {
            throw new RuntimeException("Ce paiement est déjà annulé");
        }

        paiement.setStatutPaiement(StatutPaiement.ANNULER);
        Paiement updated = paiementRepository.save(paiement);
        return convertToDto(updated);
    }

    // UPDATE - Marquer comme échoué
    @Transactional
    public PaiementDto marquerEchoue(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        paiement.setStatutPaiement(StatutPaiement.REJETER);
        Paiement updated = paiementRepository.save(paiement);
        return convertToDto(updated);
    }

    // DELETE
    @Transactional
    public void supprimerPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        // Ne pas supprimer un paiement validé
        if (paiement.getStatutPaiement() == StatutPaiement.VALIDE) {
            throw new RuntimeException("Impossible de supprimer un paiement validé");
        }

        paiementRepository.deleteById(id);
    }

    // RECHERCHES AVANCÉES

    public List<PaiementDto> getPaiementsByStatut(StatutPaiement statut) {
        return paiementRepository.findByStatutPaiement(statut).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaiementDto> getPaiementsByMode(ModePaiement mode) {
        return paiementRepository.findByModePaiement(mode).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaiementDto> getPaiementsByPeriode(LocalDate debut, LocalDate fin) {
        return paiementRepository.findByDatePaiementBetween(debut, fin).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaiementDto> getPaiementsDuJour() {
        return paiementRepository.findPaiementsDuJour().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // STATISTIQUES

    public Double calculerTotalPaiementsValides() {
        Double total = paiementRepository.calculerTotalPaiementsValides();
        return total != null ? total : 0.0;
    }

    public Double calculerTotalPeriode(LocalDate debut, LocalDate fin) {
        Double total = paiementRepository.calculerTotalPeriode(debut, fin);
        return total != null ? total : 0.0;
    }

    // Générer un numéro de paiement unique
    private String genererNumPaiement() {
        String prefix = "PAY";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%06d", (int) (Math.random() * 1000000));
        return prefix + date + random;
    }

    // Conversion Entity -> DTO
    private PaiementDto convertToDto(Paiement paiement) {
        PaiementDto dto = new PaiementDto();
        dto.setId(paiement.getId());
        dto.setNumPaiement(paiement.getNumPaiement());
        dto.setNumTransaction(paiement.getNumTransaction());
        dto.setMontant(paiement.getMontant());
        dto.setStatutPaiement(paiement.getStatutPaiement());
        dto.setDatePaiement(paiement.getDatePaiement());
        dto.setModePaiement(paiement.getModePaiement());
        return dto;
    }
}
