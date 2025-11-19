package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.*;
import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.EtatInscription;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.FormationRepository;
import com.example.api_auditeur.repository.InscriptionRepository;
import com.example.api_auditeur.repository.PaiementRepository;
import com.example.api_auditeur.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementService paiementService;

    // CREATE - Inscription simple

    public String enregistrerFichier(MultipartFile fichier) {
        if (fichier == null || fichier.isEmpty()) return null;

        try {
            // 📌 Nom unique : timestamp + nom original
            String nomFichier = System.currentTimeMillis() + "_" + fichier.getOriginalFilename();

            // 📁 Chemin absolu
            Path chemin = Paths.get("fichier").resolve(nomFichier);
            Files.createDirectories(chemin.getParent());

            // 💾 Sauvegarde physique
            Files.copy(fichier.getInputStream(), chemin, StandardCopyOption.REPLACE_EXISTING);

            // 🌐 Générer une URL d’accès (exemple)
            return "/fichier/" + nomFichier;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    public InscriptionDto creerInscription(CreateInscriptionRequest request) {
        Inscription inscription = new Inscription();

        // Vérifier que la formation existe
        Formation formation = formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Vérifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier la capacité
        Long nombreInscrits = inscriptionRepository.countByFormationId(request.getFormationId());
        if (nombreInscrits >= formation.getCapacite()) {
            throw new RuntimeException("Cette formation a atteint sa capacité maximale");
        }

        // Vérifier que la formation n'a pas déjà commencé
        if (formation.getDateDebut().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cette formation a déjà commencé");
        }

        if (request.getDateInscription() == null) {
            request.setDateInscription(LocalDate.now());
        }
        if (request.getEtatInscription() == null) {
            request.setEtatInscription(EtatInscription.EN_COURS_VALIDATION);
        }

        if (request.getFichier() != null && !request.getFichier().isEmpty()) {
            String urlFichier = enregistrerFichier(request.getFichier());
            inscription.setFichier(urlFichier); // stocke l’URL dans la base
        }


        Paiement paiement = null;
        if (request.getPaiementId() != null && request.getPaiementId() != 0) {
            paiement = paiementRepository.findById(request.getPaiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
        }

        // Créer l'inscription

        inscription.setFormation(formation);
        inscription.setUtilisateur(utilisateur);
        inscription.setPaiement(paiement);
        inscription.setDateInscription(request.getDateInscription() != null ? request.getDateInscription() : LocalDate.now());
        inscription.setEtatInscription(request.getEtatInscription());
        inscription.setNumeroCni(request.getNumeroCni());
        inscription.setDateNaissance(request.getDateNaissance());
        inscription.setAddress(request.getAddress());
        inscription.setNumeroTel(request.getNumeroTel());
        inscription.setFichier(request.getFichier() != null ? request.getFichier().getOriginalFilename() : null);

        Inscription saved = inscriptionRepository.save(inscription);
        return convertToDto(saved);
    }

    public InscriptionDto updateInscription(Long id, CreateInscriptionRequest request) {

        // Vérifier que l'inscription existe
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));

        // Vérifier que la formation existe
        Formation formation = formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Vérifier que l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier le paiement si fourni
        Paiement paiement = null;
        if (request.getPaiementId() != null && request.getPaiementId() != 0) {
            paiement = paiementRepository.findById(request.getPaiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
        }

        // Mettre à jour les champs
        inscription.setFormation(formation);
        inscription.setUtilisateur(utilisateur);
        inscription.setPaiement(paiement);

        if (request.getDateInscription() != null) {
            inscription.setDateInscription(request.getDateInscription());
        }

        if (request.getEtatInscription() != null) {
            inscription.setEtatInscription(request.getEtatInscription());
        }

        if (request.getPaiementId() != null && request.getPaiementId() != 0) {
             paiement = paiementRepository.findById(request.getPaiementId())
                    .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
            inscription.setPaiement(paiement);
        }

        inscription.setNumeroCni(request.getNumeroCni());
        inscription.setDateNaissance(request.getDateNaissance());
        inscription.setAddress(request.getAddress());
        inscription.setNumeroTel(request.getNumeroTel());

        // Gestion du fichier
        if (request.getFichier() != null && !request.getFichier().isEmpty()) {
            String urlFichier = enregistrerFichier(request.getFichier());
            inscription.setFichier(urlFichier);
        }

        // 7. Sauvegarder
        Inscription saved = inscriptionRepository.save(inscription);

        // 8. Retourner le DTO
        return new InscriptionDto(
                saved.getId(),
                saved.getFormation().getId(),
                saved.getUtilisateur().getId(),
                saved.getPaiement() != null ? saved.getPaiement().getId() : null,
                saved.getDateInscription().toString(),
                saved.getEtatInscription(),
                saved.getNumeroCni(),
                saved.getDateNaissance().toString(),
                saved.getAddress(),
                saved.getNumeroTel(),
                saved.getFichier()
        );
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
        return new InscriptionDto(
                inscription.getId(),
                inscription.getFormation() != null ? inscription.getFormation().getId() : null,
                inscription.getUtilisateur() != null ? inscription.getUtilisateur().getId() : null,
                inscription.getPaiement() != null ? inscription.getPaiement().getId() : 0L, // 0 = non payé
                inscription.getDateInscription() != null ? inscription.getDateInscription().toString() : null,
                inscription.getEtatInscription() != null ? inscription.getEtatInscription(): null,
                inscription.getNumeroCni(),
                inscription.getDateNaissance() != null ? inscription.getDateNaissance().toString() : null,
                inscription.getAddress(),
                inscription.getNumeroTel(),
                inscription.getFichier()
        );
    }

    public Inscription findById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable avec l'id : " + id));
    }
}
