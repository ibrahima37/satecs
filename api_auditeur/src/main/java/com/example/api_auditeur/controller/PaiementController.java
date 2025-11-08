package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.CreatePaiementRequest;
import com.example.api_auditeur.dto.PaiementDto;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.service.PaiementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/paiement")
@AllArgsConstructor @RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PaiementController {

    private final PaiementService paiementService;

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> creerPaiement(@Valid @RequestBody CreatePaiementRequest request) {
        try {
            PaiementDto created = paiementService.creerPaiement(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // READ - Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> getPaiementById(@PathVariable Long id) {
        try {
            PaiementDto paiement = paiementService.getPaiementById(id);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get by Numéro de Paiement
    @GetMapping("/numero/{numPaiement}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> getPaiementByNumPaiement(@PathVariable String numPaiement) {
        try {
            PaiementDto paiement = paiementService.getPaiementByNumPaiement(numPaiement);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get by Numéro de Transaction
    @GetMapping("/transaction/{numTransaction}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> getPaiementByNumTransaction(@PathVariable String numTransaction) {
        try {
            PaiementDto paiement = paiementService.getPaiementByNumTransaction(numTransaction);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get All
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PaiementDto>> getAllPaiements() {
        List<PaiementDto> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> modifierPaiement(
            @PathVariable Long id,
            @Valid @RequestBody PaiementDto paiementDto) {
        try {
            PaiementDto updated = paiementService.modifierPaiement(id, paiementDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // UPDATE - Valider
    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> validerPaiement(@PathVariable Long id) {
        try {
            PaiementDto validated = paiementService.validerPaiement(id);
            return ResponseEntity.ok(validated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Annuler
    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> annulerPaiement(
            @PathVariable Long id,
            @RequestParam(required = false) String motif) {
        try {
            PaiementDto annule = paiementService.annulerPaiement(id, motif);
            return ResponseEntity.ok(annule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Marquer échoué
    @PatchMapping("/{id}/echoue")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PaiementDto> marquerEchoue(@PathVariable Long id) {
        try {
            PaiementDto echoue = paiementService.marquerEchoue(id);
            return ResponseEntity.ok(echoue);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> supprimerPaiement(@PathVariable Long id) {
        try {
            paiementService.supprimerPaiement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // RECHERCHES AVANCÉES

    // Filtrer par statut
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PaiementDto>> getPaiementsByStatut(@PathVariable StatutPaiement statut) {
        List<PaiementDto> paiements = paiementService.getPaiementsByStatut(statut);
        return ResponseEntity.ok(paiements);
    }

    // Filtrer par mode de paiement
    @GetMapping("/mode/{mode}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PaiementDto>> getPaiementsByMode(@PathVariable ModePaiement mode) {
        List<PaiementDto> paiements = paiementService.getPaiementsByMode(mode);
        return ResponseEntity.ok(paiements);
    }

    // Filtrer par période
    @GetMapping("/periode")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PaiementDto>> getPaiementsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<PaiementDto> paiements = paiementService.getPaiementsByPeriode(debut, fin);
        return ResponseEntity.ok(paiements);
    }

    // Paiements du jour
    @GetMapping("/aujourd-hui")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PaiementDto>> getPaiementsDuJour() {
        List<PaiementDto> paiements = paiementService.getPaiementsDuJour();
        return ResponseEntity.ok(paiements);
    }

    // STATISTIQUES

    // Total des paiements validés
    @GetMapping("/statistiques/total-valides")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Double>> getTotalPaiementsValides() {
        Double total = paiementService.calculerTotalPaiementsValides();
        Map<String, Double> response = new HashMap<>();
        response.put("totalValides", total);
        return ResponseEntity.ok(response);
    }

    // Total pour une période
    @GetMapping("/statistiques/total-periode")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Double>> getTotalPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Double total = paiementService.calculerTotalPeriode(debut, fin);
        Map<String, Double> response = new HashMap<>();
        response.put("totalPeriode", total);
        response.put("debut", (double) debut.toEpochDay());
        response.put("fin", (double) fin.toEpochDay());
        return ResponseEntity.ok(response);
    }
}
