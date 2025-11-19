package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.CreateInscriptionRequest;
import com.example.api_auditeur.dto.ErrorDto;
import com.example.api_auditeur.dto.InscriptionAvecPaiementRequest;
import com.example.api_auditeur.dto.InscriptionDto;
import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.EtatInscription;
import com.example.api_auditeur.repository.FormationRepository;
import com.example.api_auditeur.repository.UtilisateurRepository;
import com.example.api_auditeur.service.InscriptionService;
import com.example.api_auditeur.service.SmsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController @AllArgsConstructor
@RequestMapping("/api/inscription")
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private JavaMailSender mailSender;
    private SmsService smsService;
    private UtilisateurRepository utilisateurRepository;
    private FormationRepository formationRepository;

    private void envoyerEmailConfirmation(InscriptionDto inscriptionDto) {
        Utilisateur utilisateur = utilisateurRepository.findById(inscriptionDto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Formation formation = formationRepository.findById(inscriptionDto.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation introuvable"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(utilisateur.getEmail());
        message.setSubject("Confirmation de création de compte");
        message.setText("Bonjour " + utilisateur.getNom() + " " + utilisateur.getPrenom()
                + ",\n\nVotre inscription à la formation (" + formation.getTitre() + ") est bien enregistrée avec l’état : "
                + inscriptionDto.getEtatInscription() + ".");
        mailSender.send(message);
    }

    private void envoyerEmailConfirmationFormation(InscriptionDto inscriptionDto) {
        Utilisateur utilisateur = utilisateurRepository.findById(inscriptionDto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Formation formation = formationRepository.findById(inscriptionDto.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation introuvable"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(utilisateur.getEmail());
        message.setSubject("Confirmation de l'état de votre inscription");
        message.setText("Bonjour " + utilisateur.getNom() + " " + utilisateur.getPrenom()
                + ",\n\nVotre inscription à la formation (" + formation.getTitre() + ") est bien enregistrée avec l’état : "
                + inscriptionDto.getEtatInscription() + ".");
        mailSender.send(message);
    }
    // CREATE - Inscription simple
    @PostMapping
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> creerInscription(@ModelAttribute CreateInscriptionRequest request) {
        try {
            InscriptionDto created = inscriptionService.creerInscription(request);

            envoyerEmailConfirmation(created);

            System.out.println("Payload reçu: " + request);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("Erreur d'inscription", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> updateInscription(
            @PathVariable Long id,
            @Valid @ModelAttribute CreateInscriptionRequest request) {
        try {
            InscriptionDto dto = inscriptionService.updateInscription(id, request);

            envoyerEmailConfirmationFormation(dto);
            System.out.println("Payload reçu: " + request);

            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CREATE - Inscription avec paiement
    @PostMapping("/avec-paiement")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> creerInscriptionAvecPaiement(
            @Valid @RequestBody InscriptionAvecPaiementRequest request) {
        try {
            InscriptionDto created = inscriptionService.creerInscriptionAvecPaiement(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // READ - Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> getInscriptionById(@PathVariable Long id) {
        try {
            InscriptionDto inscription = inscriptionService.getInscriptionById(id);
            return ResponseEntity.ok(inscription);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get All
    @GetMapping
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getAllInscriptions() {
        List<InscriptionDto> inscriptions = inscriptionService.getAllInscriptions();
        return ResponseEntity.ok(inscriptions);
    }

    // READ - Par Formation
    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getInscriptionsByFormation(@PathVariable Long formationId) {
        List<InscriptionDto> inscriptions = inscriptionService.getInscriptionsByFormation(formationId);
        return ResponseEntity.ok(inscriptions);
    }

    // READ - Par Utilisateur
    @GetMapping("/utilisateur/{utilisateurId}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getInscriptionsByUtilisateur(@PathVariable Long utilisateurId) {
        List<InscriptionDto> inscriptions = inscriptionService.getInscriptionsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(inscriptions);
    }

    // READ - Par État
    @GetMapping("/etat/{etat}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getInscriptionsByEtat(@PathVariable EtatInscription etat) {
        List<InscriptionDto> inscriptions = inscriptionService.getInscriptionsByEtat(etat);
        return ResponseEntity.ok(inscriptions);
    }

    // READ - Inscriptions du jour
    @GetMapping("/aujourd-hui")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getInscriptionsDuJour() {
        List<InscriptionDto> inscriptions = inscriptionService.getInscriptionsDuJour();
        return ResponseEntity.ok(inscriptions);
    }

    // UPDATE - Modifier l'état
    @PatchMapping("/{id}/etat")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> modifierEtat(
            @PathVariable Long id,
            @RequestParam EtatInscription etat) {
        try {
            InscriptionDto updated = inscriptionService.modifierEtat(id, etat);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Confirmer
    @PatchMapping("/{id}/confirmer")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> confirmerInscription(@PathVariable Long id) {
        try {
            InscriptionDto confirmed = inscriptionService.confirmerInscription(id);
            return ResponseEntity.ok(confirmed);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Annuler
    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> annulerInscription(@PathVariable Long id) {
        try {
            InscriptionDto annule = inscriptionService.annulerInscription(id);
            return ResponseEntity.ok(annule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Associer un paiement
    @PatchMapping("/{inscriptionId}/paiement/{paiementId}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<InscriptionDto> associerPaiement(
            @PathVariable Long inscriptionId,
            @PathVariable Long paiementId) {
        try {
            InscriptionDto updated = inscriptionService.associerPaiement(inscriptionId, paiementId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> supprimerInscription(@PathVariable Long id) {
        try {
            inscriptionService.supprimerInscription(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // STATISTIQUES

    // Compter les inscriptions d'une formation
    @GetMapping("/formation/{formationId}/count")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Long>> countInscriptionsByFormation(@PathVariable Long formationId) {
        Long count = inscriptionService.countInscriptionsByFormation(formationId);
        Map<String, Long> response = new HashMap<>();
        response.put("nombreInscriptions", count);
        return ResponseEntity.ok(response);
    }

    // Inscriptions par formation et état
    @GetMapping("/formation/{formationId}/etat/{etat}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<InscriptionDto>> getInscriptionsByFormationEtEtat(
            @PathVariable Long formationId,
            @PathVariable EtatInscription etat) {
        List<InscriptionDto> inscriptions = inscriptionService.getInscriptionsByFormationEtEtat(formationId, etat);
        return ResponseEntity.ok(inscriptions);
    }

}
