package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.*;
import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.EtatInscription;
import com.example.api_auditeur.model.page_enum.Role;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.FormationRepository;
import com.example.api_auditeur.repository.InscriptionRepository;
import com.example.api_auditeur.repository.PaiementRepository;
import com.example.api_auditeur.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InscriptionService {
/*
    private InscriptionRepository repository;

    private UtilisateurRepository uRepository;

    private FormationRepository fRepository;

    private PaiementRepository paiementRepository;

    public void sinscrie(
            String nom,
            String prenom,
            String email,
            String motDePasse,
            Long formationId){

        Utilisateur utilisateur= new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setRole(Role.ADMIN);
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(motDePasse));
        uRepository.save(utilisateur);

        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()->new RuntimeException("Formation introuvable"));

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setDateInscription(LocalDate.now());
        inscription.setEtatInscription(EtatInscription.ANNULEE);
        //inscription.setPaiement();

        repository.save(inscription);
    }

    public Inscription soumettreInscription(Long utilisateurId, Long formationId, Paiement paiement){
        Utilisateur utilisateur = uRepository.findById(utilisateurId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()-> new RuntimeException("Formation introuvable"));

        Paiement paiementEnregistre = paiementRepository.save(paiement);

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setEtatInscription(EtatInscription.DOCUMENTS_EN_ATTENTE);
        inscription.setDateInscription(LocalDate.now());
        inscription.setPaiement(paiementEnregistre);
        return repository.save(inscription);
    }

    public Inscription validerInscription(Long inscriptionId){
        Inscription inscription = repository.findById(inscriptionId)
                .orElseThrow(()-> new RuntimeException("Inscription introuvable"));
        inscription.setEtatInscription(EtatInscription.VALIDER);
        inscription.setDateInscription(LocalDate.now());

        return repository.save(inscription);
    }

    public Inscription rejeterInscription(Long inscriptionId){
        Inscription inscription = repository.findById(inscriptionId)
                .orElseThrow(()-> new RuntimeException("Inscription introuvable"));
        inscription.setEtatInscription(EtatInscription.VALIDER);
        inscription.setDateInscription(LocalDate.now());

        return repository.save(inscription);
    }

    public Inscription mettreEnAttente(Long utilisateurId, Long formationId){
        Utilisateur utilisateur = uRepository.findById(utilisateurId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()-> new RuntimeException("Formation introuvable"));

        Paiement paiement = new Paiement();
        paiement.setMontant(paiement.getMontant());
        paiement.setDatePaiement(LocalDate.now());
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setEtatInscription(EtatInscription.DOCUMENTS_EN_ATTENTE);
        inscription.setDateInscription(LocalDate.now());
        inscription.setPaiement(paiement);
        return repository.save(inscription);
    }*/

    private final InscriptionRepository inscriptionRepository;
    private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementService paiementService;

    // CREATE - Inscription simple
    @Transactional
    public InscriptionDto creerInscription(CreateInscriptionRequest request) {
        // Vérifier que la formation existe
        Formation formation = formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Vérifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur n'est pas déjà inscrit
        if (inscriptionRepository.existsByFormationIdAndUtilisateurId(
                request.getFormationId(), request.getUtilisateurId())) {
            throw new RuntimeException("Cet utilisateur est déjà inscrit à cette formation");
        }

        // Vérifier la capacité
        Long nombreInscrits = inscriptionRepository.countByFormationId(request.getFormationId());
        if (nombreInscrits >= formation.getCapacite()) {
            throw new RuntimeException("Cette formation a atteint sa capacité maximale");
        }

        // Vérifier que la formation n'a pas déjà commencé
        if (formation.getDateDebut().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cette formation a déjà commencé");
        }

        // Créer l'inscription
        Inscription inscription = new Inscription();
        inscription.setFormation(formation);
        inscription.setUtilisateur(utilisateur);
        inscription.setDateInscription(LocalDate.now());
        inscription.setEtatInscription(EtatInscription.DOCUMENTS_EN_ATTENTE);

        // Si un paiement est fourni
        if (request.getPaiementId() != null) {
            Paiement paiement = paiementRepository.findById(request.getPaiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
            inscription.setPaiement(paiement);
        }

        Inscription saved = inscriptionRepository.save(inscription);
        return convertToDto(saved);
    }

    // CREATE - Inscription avec création de paiement
    @Transactional
    public InscriptionDto creerInscriptionAvecPaiement(InscriptionAvecPaiementRequest request) {
        // Vérifier que la formation existe
        Formation formation = formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Vérifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur n'est pas déjà inscrit
        if (inscriptionRepository.existsByFormationIdAndUtilisateurId(
                request.getFormationId(), request.getUtilisateurId())) {
            throw new RuntimeException("Cet utilisateur est déjà inscrit à cette formation");
        }

        // Vérifier la capacité
        Long nombreInscrits = inscriptionRepository.countByFormationId(request.getFormationId());
        if (nombreInscrits >= formation.getCapacite()) {
            throw new RuntimeException("Cette formation a atteint sa capacité maximale");
        }

        // Créer le paiement
        CreatePaiementRequest paiementRequest = new CreatePaiementRequest();
        paiementRequest.setNumTransaction(request.getNumTransaction());
        paiementRequest.setMontant(formation.getTarif());
        paiementRequest.setModePaiement(request.getModePaiement());

        PaiementDto paiementDto = paiementService.creerPaiement(paiementRequest);
        Paiement paiement = paiementRepository.findById(paiementDto.getId())
                .orElseThrow(() -> new RuntimeException("Erreur lors de la création du paiement"));

        // Créer l'inscription
        Inscription inscription = new Inscription();
        inscription.setFormation(formation);
        inscription.setUtilisateur(utilisateur);
        inscription.setDateInscription(LocalDate.now());
        inscription.setEtatInscription(EtatInscription.PAIEMENT_EN_ATTENTE);
        inscription.setPaiement(paiement);

        Inscription saved = inscriptionRepository.save(inscription);
        return convertToDto(saved);
    }

    // READ - Get by ID
    public InscriptionDto getInscriptionById(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'id: " + id));
        return convertToDto(inscription);
    }

    // READ - Get All
    public List<InscriptionDto> getAllInscriptions() {
        return inscriptionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par Formation
    public List<InscriptionDto> getInscriptionsByFormation(Long formationId) {
        return inscriptionRepository.findByFormationId(formationId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par Utilisateur
    public List<InscriptionDto> getInscriptionsByUtilisateur(Long utilisateurId) {
        return inscriptionRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par État
    public List<InscriptionDto> getInscriptionsByEtat(EtatInscription etat) {
        return inscriptionRepository.findByEtatInscription(etat).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Inscriptions du jour
    public List<InscriptionDto> getInscriptionsDuJour() {
        return inscriptionRepository.findInscriptionsDuJour().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // UPDATE - Modifier l'état
    @Transactional
    public InscriptionDto modifierEtat(Long id, EtatInscription nouvelEtat) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        inscription.setEtatInscription(nouvelEtat);
        Inscription updated = inscriptionRepository.save(inscription);
        return convertToDto(updated);
    }

    // UPDATE - Confirmer l'inscription (après paiement validé)
    @Transactional
    public InscriptionDto confirmerInscription(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        // Vérifier que le paiement est validé
        if (inscription.getPaiement() == null) {
            throw new RuntimeException("Aucun paiement associé à cette inscription");
        }

        if (inscription.getPaiement().getStatutPaiement() != StatutPaiement.VALIDE) {
            throw new RuntimeException("Le paiement n'est pas encore validé");
        }

        inscription.setEtatInscription(EtatInscription.VALIDER);
        Inscription updated = inscriptionRepository.save(inscription);
        return convertToDto(updated);
    }

    // UPDATE - Annuler l'inscription
    @Transactional
    public InscriptionDto annulerInscription(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        if (inscription.getEtatInscription() == EtatInscription.REJETER) {
            throw new RuntimeException("Cette inscription est déjà annulée");
        }

        // Vérifier si la formation a déjà commencé
        if (inscription.getFormation().getDateDebut().isBefore(LocalDate.now())) {
            throw new RuntimeException("Impossible d'annuler une inscription pour une formation déjà commencée");
        }

        inscription.setEtatInscription(EtatInscription.REJETER);
        Inscription updated = inscriptionRepository.save(inscription);
        return convertToDto(updated);
    }

    // UPDATE - Associer un paiement
    @Transactional
    public InscriptionDto associerPaiement(Long inscriptionId, Long paiementId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        inscription.setPaiement(paiement);
        Inscription updated = inscriptionRepository.save(inscription);
        return convertToDto(updated);
    }

    // DELETE
    @Transactional
    public void supprimerInscription(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        // Ne pas supprimer une inscription confirmée
        if (inscription.getEtatInscription() == EtatInscription.VALIDER) {
            throw new RuntimeException("Impossible de supprimer une inscription confirmée");
        }

        inscriptionRepository.deleteById(id);
    }

    // STATISTIQUES

    public Long countInscriptionsByFormation(Long formationId) {
        return inscriptionRepository.countByFormationId(formationId);
    }

    public List<InscriptionDto> getInscriptionsByFormationEtEtat(Long formationId, EtatInscription etat) {
        return inscriptionRepository.findByFormationIdAndEtat(formationId, etat).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Conversion Entity -> DTO
    private InscriptionDto convertToDto(Inscription inscription) {
        InscriptionDto dto = new InscriptionDto();
        dto.setId(inscription.getId());
        dto.setDateInscription(inscription.getDateInscription());
        dto.setEtatInscription(inscription.getEtatInscription());

        // Informations de la formation
        if (inscription.getFormation() != null) {
            dto.setFormationId(inscription.getFormation().getId());
            dto.setFormationTitre(inscription.getFormation().getTitre());
            dto.setFormationDateDebut(inscription.getFormation().getDateDebut());
            dto.setFormationDateFin(inscription.getFormation().getDeteFin());
            dto.setFormationTarif(inscription.getFormation().getTarif());
        }

        // Informations de l'utilisateur
        if (inscription.getUtilisateur() != null) {
            dto.setUtilisateurId(inscription.getUtilisateur().getId());
            dto.setUtilisateurNom(inscription.getUtilisateur().getNom());
            dto.setUtilisateurPrenom(inscription.getUtilisateur().getPrenom());
            dto.setUtilisateurEmail(inscription.getUtilisateur().getEmail());
        }

        // Informations du paiement
        if (inscription.getPaiement() != null) {
            dto.setPaiementId(inscription.getPaiement().getId());
            dto.setPaiementNumPaiement(inscription.getPaiement().getNumPaiement());
            dto.setPaiementMontant(inscription.getPaiement().getMontant());
            dto.setPaiementStatut(inscription.getPaiement().getStatutPaiement().name());
        }

        return dto;
    }
}
